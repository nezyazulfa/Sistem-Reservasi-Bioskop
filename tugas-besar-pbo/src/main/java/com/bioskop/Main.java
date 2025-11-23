package com.bioskop;

import com.bioskop.data.CinemaData;
import com.bioskop.model.Film;
import com.bioskop.tiket.*;
import com.bioskop.strategy.*;

// Imports untuk GUI
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

// Imports untuk PDF (iText)
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.BaseColor;

public class Main extends JFrame {

    // Color Palette Modern
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 73, 94);
    private static final Color ACCENT_COLOR = new Color(46, 204, 113);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(44, 62, 80);
    private static final Color TEXT_SECONDARY = new Color(127, 140, 141);

    // Komponen GUI
    private JTable tableFilm;
    private DefaultTableModel tableModel;
    private JSpinner spinnerTiket, spinnerPopcorn, spinnerSoftdrink, spinnerVip;
    private JComboBox<String> comboStrategy;
    private JTextArea areaStruk;
    private JLabel labelFilmTerpilih, labelHargaPreview;
    private JButton btnHitung, btnCetak;

    // Data Backend
    private CinemaData database;
    private Film selectedFilm;

    public Main() {
        database = CinemaData.getInstance();

        setTitle("Cinema Booking System - OOP Kelas 2C");
        setSize(1280, 850); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // HEADER
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        // CENTER
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setBackground(BACKGROUND_COLOR);
        centerWrapper.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setOpaque(false);
        contentPanel.add(createFilmCatalogPanel());
        contentPanel.add(createBookingPanel());
        
        centerWrapper.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(centerWrapper, BorderLayout.CENTER);

        // SOUTH - Receipt & Print
        JPanel southWrapper = new JPanel(new BorderLayout());
        southWrapper.setBackground(BACKGROUND_COLOR);
        southWrapper.setBorder(BorderFactory.createEmptyBorder(0, 20, 15, 20));
        southWrapper.add(createReceiptPanel(), BorderLayout.CENTER);
        
        mainPanel.add(southWrapper, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_COLOR);
        header.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        JLabel title = new JLabel("CINEMA BOOKING SYSTEM");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        
        header.add(title, BorderLayout.WEST);
        return header;
    }

    private JPanel createFilmCatalogPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JPanel headerPanel = new JPanel(new BorderLayout(0, 5));
        headerPanel.setOpaque(false);
        
        JLabel lblHeader = new JLabel("Katalog Film");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblHeader.setForeground(TEXT_PRIMARY);
        
        JLabel lblInfo = new JLabel("Pilih film dari daftar di bawah");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblInfo.setForeground(TEXT_SECONDARY);
        
        headerPanel.add(lblHeader, BorderLayout.NORTH);
        headerPanel.add(lblInfo, BorderLayout.SOUTH);
        panel.add(headerPanel, BorderLayout.NORTH);

        String[] columnNames = {"Judul Film", "Genre", "Harga Dasar"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tableFilm = new JTable(tableModel);
        styleTable(tableFilm);
        loadDataFilm();

        tableFilm.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableFilm.getSelectedRow() != -1) {
                int index = tableFilm.getSelectedRow();
                selectedFilm = database.getAll().get(index);
                updateFilmSelection();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableFilm);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(35);
        table.setSelectionBackground(new Color(52, 152, 219, 80));
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setShowGrid(true);
        table.setGridColor(new Color(220, 220, 220));
        table.setIntercellSpacing(new Dimension(1, 1));
        
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);
        
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(SECONDARY_COLOR);
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 12));
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return this;
            }
        };
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
        
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 249, 250));
                    setForeground(TEXT_PRIMARY);
                } else {
                    setForeground(TEXT_PRIMARY);
                }
                setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
                return this;
            }
        };
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
        
        table.getColumnModel().getColumn(0).setPreferredWidth(220);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
    }

    private JPanel createBookingPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // --- Header ---
        JLabel lblHeader = new JLabel("Kustomisasi Tiket");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblHeader.setForeground(TEXT_PRIMARY);
        panel.add(lblHeader, BorderLayout.NORTH);

        // --- Form (Center) ---
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        // Film Info Box
        JPanel filmInfoPanel = new JPanel();
        filmInfoPanel.setLayout(new BoxLayout(filmInfoPanel, BoxLayout.Y_AXIS));
        filmInfoPanel.setBackground(new Color(240, 248, 255));
        filmInfoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        filmInfoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        filmInfoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60)); // Batasi tinggi box info
        
        labelFilmTerpilih = new JLabel("Belum ada film dipilih");
        labelFilmTerpilih.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelFilmTerpilih.setForeground(TEXT_PRIMARY);
        
        labelHargaPreview = new JLabel(" ");
        labelHargaPreview.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        labelHargaPreview.setForeground(TEXT_SECONDARY);
        
        filmInfoPanel.add(labelFilmTerpilih);
        filmInfoPanel.add(Box.createVerticalStrut(3));
        filmInfoPanel.add(labelHargaPreview);
        
        contentPanel.add(filmInfoPanel);
        contentPanel.add(Box.createVerticalStrut(10)); // Jarak dikurangi agar muat

        // Decorator Section
        JLabel lblDecorator = new JLabel("Tambahan (Decorator Pattern):");
        lblDecorator.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblDecorator.setForeground(TEXT_PRIMARY);
        lblDecorator.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(lblDecorator);
        contentPanel.add(Box.createVerticalStrut(5));

        // Spinner Inputs (Jarak antar elemen dirapatkan)
        contentPanel.add(createSpinnerPanel("Jumlah Tiket:", spinnerTiket = createSpinner()));
        contentPanel.add(Box.createVerticalStrut(5));
        
        contentPanel.add(createSpinnerPanel("Popcorn (+25rb):", spinnerPopcorn = createSpinner()));
        contentPanel.add(Box.createVerticalStrut(5));
        
        contentPanel.add(createSpinnerPanel("Softdrink (+15rb):", spinnerSoftdrink = createSpinner()));
        contentPanel.add(Box.createVerticalStrut(5));
        
        contentPanel.add(createSpinnerPanel("Upgrade VIP (+50rb):", spinnerVip = createSpinner()));
        contentPanel.add(Box.createVerticalStrut(10));

        // Strategy Section
        JLabel lblStrategy = new JLabel("Kalkulasi Harga (Strategy Pattern):");
        lblStrategy.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStrategy.setForeground(TEXT_PRIMARY);
        lblStrategy.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(lblStrategy);
        contentPanel.add(Box.createVerticalStrut(5));

        String[] options = {"Harga Normal", "Weekend (+Rp 10.000)", "Member (Diskon 10%)"};
        comboStrategy = new JComboBox<>(options);
        comboStrategy.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboStrategy.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        comboStrategy.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(comboStrategy);
        
        // Membungkus contentPanel dengan ScrollPane opsional jika layar terlalu kecil
        // Tapi kita pakai lem (glue) di bawah untuk mendorong elemen ke atas
        contentPanel.add(Box.createVerticalGlue()); 

        panel.add(contentPanel, BorderLayout.CENTER);

        // --- Tombol Hitung (South) ---
        JPanel buttonPanel = new JPanel(new GridLayout(1, 1));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        btnHitung = new JButton("HITUNG & PESAN TIKET");
        styleButton(btnHitung, ACCENT_COLOR, new Color(39, 174, 96), Color.WHITE);
        btnHitung.addActionListener(e -> prosesPemesanan());
        btnHitung.setPreferredSize(new Dimension(100, 40));
        
        buttonPanel.add(btnHitung);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Helper membuat JSpinner
    private JSpinner createSpinner() {
        SpinnerNumberModel model = new SpinnerNumberModel(0, 0, 99, 1);
        JSpinner spinner = new JSpinner(model);
        spinner.setFont(new Font("Segoe UI", Font.BOLD, 13));
        ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setHorizontalAlignment(JTextField.CENTER);
        return spinner;
    }

    private JPanel createSpinnerPanel(String labelText, JSpinner spinner) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(true);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35)); // Tinggi diperkecil
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(TEXT_PRIMARY);
        
        spinner.setPreferredSize(new Dimension(60, 25));
        
        panel.add(label, BorderLayout.WEST);
        panel.add(spinner, BorderLayout.EAST);
        return panel;
    }

    // UPDATE: Menambahkan parameter textColor
    private void styleButton(JButton btn, Color normalColor, Color hoverColor, Color textColor) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(normalColor);
        btn.setForeground(textColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Memastikan warna font tetap saat di-hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(hoverColor);
                btn.setForeground(textColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(normalColor);
                btn.setForeground(textColor);
            }
        });
    }

    private JPanel createReceiptPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setPreferredSize(new Dimension(0, 200));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CARD_COLOR);
        
        JLabel lblHeader = new JLabel("Struk Otomatis");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblHeader.setForeground(TEXT_PRIMARY);
        
        // FIX: Tombol Simpan PDF Warna Putih
        btnCetak = new JButton("Simpan Struk PDF");
        styleButton(btnCetak, new Color(52, 152, 219), new Color(41, 128, 185), Color.WHITE);
        
        btnCetak.setPreferredSize(new Dimension(160, 30));
        btnCetak.setEnabled(false); 
        btnCetak.addActionListener(e -> simpanPDF());

        headerPanel.add(lblHeader, BorderLayout.WEST);
        headerPanel.add(btnCetak, BorderLayout.EAST);

        areaStruk = new JTextArea();
        areaStruk.setEditable(false);
        areaStruk.setFont(new Font("Courier New", Font.PLAIN, 12));
        areaStruk.setBackground(new Color(250, 250, 250));
        areaStruk.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        areaStruk.setText("Silakan pilih film dari katalog dan lakukan pemesanan...");
        
        JScrollPane scroll = new JScrollPane(areaStruk);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void loadDataFilm() {
        List<Film> films = database.getAll();
        for (Film f : films) {
            tableModel.addRow(new Object[]{f.getTitle(), f.getGenre(), formatRupiah(f.getPrice())});
        }
    }

    private void updateFilmSelection() {
        if (selectedFilm != null) {
            labelFilmTerpilih.setText(selectedFilm.getTitle());
            labelHargaPreview.setText("Genre: " + selectedFilm.getGenre() + " | Harga: " + formatRupiah(selectedFilm.getPrice()));
            spinnerTiket.setValue(1);
            spinnerPopcorn.setValue(0);
            spinnerSoftdrink.setValue(0);
            spinnerVip.setValue(0);
            btnCetak.setEnabled(false); 
        }
    }

    private PricingStrategy getSelectedStrategy() {
        String tipeBayar = (String) comboStrategy.getSelectedItem();
        if (tipeBayar.contains("Weekend")) {
            return new HargaWeekend();
        } else if (tipeBayar.contains("Member")) {
            return new DiskonMember();
        } else {
            return new HargaNormal();
        }
    }

    private void prosesPemesanan() {
        if (selectedFilm == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih film dari katalog!", "Perhatian", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int jumlahTiket = (Integer) spinnerTiket.getValue();
        int jumlahPopcorn = (Integer) spinnerPopcorn.getValue();
        int jumlahSoftdrink = (Integer) spinnerSoftdrink.getValue();
        int jumlahVip = (Integer) spinnerVip.getValue();

        if (jumlahTiket == 0) {
            JOptionPane.showMessageDialog(this, "Jumlah tiket minimal 1!", "Perhatian", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (jumlahVip > jumlahTiket) {
             JOptionPane.showMessageDialog(this, "Jumlah VIP Seat tidak boleh melebihi jumlah tiket!", "Validasi Error", JOptionPane.WARNING_MESSAGE);
             return;
        }

        double hargaDasar = selectedFilm.getPrice() * jumlahTiket;
        double hargaPopcorn = 25000 * jumlahPopcorn;
        double hargaSoftdrink = 15000 * jumlahSoftdrink;
        double hargaVip = 50000 * jumlahVip;
        
        double hargaTotal = hargaDasar + hargaPopcorn + hargaSoftdrink + hargaVip;

        PricingStrategy strategy = getSelectedStrategy();
        String tipeBayar = (String) comboStrategy.getSelectedItem();
        double hargaAkhir = strategy.hitungHargaAkhir(hargaTotal);

        StringBuilder struk = new StringBuilder();
        struk.append("==================================================\n");
        struk.append("          CINEMA BOOKING SYSTEM - KELAS 2C        \n");
        struk.append("       Tugas Besar Pemrograman Berorientasi Objek \n");
        struk.append("==================================================\n");
        struk.append(String.format(" Film        : %-30s\n", truncate(selectedFilm.getTitle(), 30)));
        struk.append(String.format(" Genre       : %-30s\n", truncate(selectedFilm.getGenre(), 30)));
        struk.append("==================================================\n");
        struk.append(" DETAIL PESANAN:\n");
        struk.append(String.format(" Tiket Reguler        : %2d x %-15s\n", jumlahTiket, formatRupiah(selectedFilm.getPrice())));
        
        if (jumlahPopcorn > 0) struk.append(String.format(" Popcorn Large        : %2d x %-15s\n", jumlahPopcorn, "Rp25.000,00"));
        if (jumlahSoftdrink > 0) struk.append(String.format(" Softdrink            : %2d x %-15s\n", jumlahSoftdrink, "Rp15.000,00"));
        if (jumlahVip > 0) struk.append(String.format(" Upgrade VIP Seat     : %2d x %-15s\n", jumlahVip, "Rp50.000,00"));
        
        struk.append("==================================================\n");
        struk.append(String.format(" Subtotal    : %-30s\n", formatRupiah(hargaTotal)));
        struk.append("--------------------------------------------------\n");
        struk.append(String.format(" Metode      : %-30s\n", truncate(tipeBayar, 30)));
        struk.append(String.format(" TOTAL BAYAR : %-30s\n", formatRupiah(hargaAkhir)));
        struk.append("==================================================\n");
        struk.append(" Terima kasih! Happy Watching!\n");

        areaStruk.setText(struk.toString());
        btnCetak.setEnabled(true); 
        
        JOptionPane.showMessageDialog(this, "Pemesanan berhasil!\nTotal: " + formatRupiah(hargaAkhir), "Sukses", JOptionPane.INFORMATION_MESSAGE);
    }

    private void simpanPDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan Struk sebagai PDF");
        fileChooser.setSelectedFile(new java.io.File("Struk_Bioskop.pdf"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".pdf")) {
                filePath += ".pdf";
            }

            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();

                com.itextpdf.text.Font fontMono = FontFactory.getFont(FontFactory.COURIER, 10, BaseColor.BLACK);
                
                Paragraph p = new Paragraph(areaStruk.getText(), fontMono);
                document.add(p);

                document.close();
                JOptionPane.showMessageDialog(this, "Struk berhasil disimpan ke:\n" + filePath, "Sukses PDF", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gagal menyimpan PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void resetForm() {
        tableFilm.clearSelection();
        spinnerTiket.setValue(0);
        spinnerPopcorn.setValue(0);
        spinnerSoftdrink.setValue(0);
        spinnerVip.setValue(0);
        comboStrategy.setSelectedIndex(0);
        selectedFilm = null;
        labelFilmTerpilih.setText("Belum ada film dipilih");
        labelHargaPreview.setText(" ");
        areaStruk.setText("...");
        btnCetak.setEnabled(false);
    }

    private String truncate(String str, int length) {
        return str.length() > length ? str.substring(0, length - 3) + "..." : str;
    }

    private String formatRupiah(double number) {
        return NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format(number);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}