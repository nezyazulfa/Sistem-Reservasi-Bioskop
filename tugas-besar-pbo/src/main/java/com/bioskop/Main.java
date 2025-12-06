package com.bioskop;

import com.bioskop.data.CinemaData;
import com.bioskop.model.Film;
import com.bioskop.strategy.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.BaseColor;

public class Main extends JFrame {

    // Modern Color Palette - Dark Theme
    private static final Color DARK_BG = new Color(15, 23, 42);
    private static final Color CARD_BG = new Color(30, 41, 59, 230);
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color ACCENT_GREEN = new Color(46, 204, 113);
    private static final Color ACCENT_PINK = new Color(236, 72, 153);
    private static final Color TEXT_WHITE = new Color(248, 250, 252);
    private static final Color TEXT_GRAY = new Color(148, 163, 184);
    private static final Color BORDER_COLOR = new Color(71, 85, 105);

    // GUI Components
    private DefaultTableModel tableModel;
    private JSpinner spinnerTiket;
    private JSpinner spinnerPopcorn;
    private JSpinner spinnerSoftdrink;
    private JSpinner spinnerVip;
    private JComboBox<String> comboStrategy;
    private JTextArea areaStruk;
    private JLabel labelFilmTerpilih;
    private JLabel labelHargaPreview;
    private JButton btnCetak;

    // Backend
    private transient CinemaData database;
    private Film selectedFilm;

    public Main() {
        database = CinemaData.getInstance();
        setTitle("Cinema Booking System - OOP Kelas 2C");
        setSize(1400, 900);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Main Panel dengan Gradient Background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, DARK_BG,
                    getWidth(), getHeight(), new Color(30, 41, 59)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Decorative glow circles
                drawGlowCircle(g2d, 200, 150, 300, PRIMARY_COLOR, 0.08f);
                drawGlowCircle(g2d, getWidth() - 200, 200, 350, ACCENT_PINK, 0.06f);
                drawGlowCircle(g2d, getWidth() / 2, getHeight() - 150, 400, ACCENT_GREEN, 0.05f);
            }
            
            private void drawGlowCircle(Graphics2D g2d, int x, int y, int size, Color color, float alpha) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                GradientPaint glow = new GradientPaint(
                    x, y, new Color(color.getRed(), color.getGreen(), color.getBlue(), 150),
                    x + size, y + size, new Color(color.getRed(), color.getGreen(), color.getBlue(), 0)
                );
                g2d.setPaint(glow);
                g2d.fillOval(x - size / 2, y - size / 2, size, size);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            }
        };
        
        mainPanel.setLayout(new BorderLayout(0, 0));

        // HEADER dengan Gradient
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        // CENTER CONTENT
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 25, 0));
        contentPanel.setOpaque(false);
        contentPanel.add(createFilmCatalogPanel());
        contentPanel.add(createBookingPanel());

        centerWrapper.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(centerWrapper, BorderLayout.CENTER);

        // SOUTH - Receipt
        JPanel southWrapper = new JPanel(new BorderLayout());
        southWrapper.setOpaque(false);
        southWrapper.setBorder(BorderFactory.createEmptyBorder(0, 30, 25, 30));
        southWrapper.add(createReceiptPanel(), BorderLayout.CENTER);
        
        mainPanel.add(southWrapper, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Gradient header
                GradientPaint gradient = new GradientPaint(
                    0, 0, PRIMARY_COLOR,
                    getWidth(), 0, new Color(52, 152, 219)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        header.setLayout(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        header.setPreferredSize(new Dimension(0, 90));
        
        JLabel title = new JLabel("CINEMA BOOKING SYSTEM");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(TEXT_WHITE);
        
        JLabel subtitle = new JLabel("Politeknik Negeri Bandung - Kelas 2C");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(220, 230, 240));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);
        
        header.add(titlePanel, BorderLayout.WEST);
        
        return header;
    }

    private JPanel createFilmCatalogPanel() {
        JPanel panel = createGlassPanel("KATALOG FILM", "Pilih film dari daftar di bawah");

        String[] columnNames = {"Judul Film", "Genre", "Harga", "Stok"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        JTable tableFilm = new JTable(tableModel);
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
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(40);
        table.setBackground(new Color(30, 41, 59));
        table.setForeground(TEXT_WHITE);
        table.setSelectionBackground(new Color(41, 128, 185, 100));
        table.setSelectionForeground(TEXT_WHITE);
        table.setShowGrid(true);
        table.setGridColor(new Color(71, 85, 105));
        
        styleTableHeader(table);
        applyHeaderRenderer(table);
        applyCellRenderer(table);
    }
    
    private void styleTableHeader(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        header.setReorderingAllowed(false);
        header.setBackground(new Color(15, 23, 42));
        header.setForeground(TEXT_WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
    }
    
    private void applyHeaderRenderer(JTable table) {
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(new Color(15, 23, 42));
                setForeground(TEXT_WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setHorizontalAlignment(SwingConstants.CENTER);
                setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                return this;
            }
        };
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
    }
    
    private void applyCellRenderer(JTable table) {
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    setBackground(row % 2 == 0 ? new Color(30, 41, 59) : new Color(40, 51, 69));
                }
                setForeground(TEXT_WHITE);
                setHorizontalAlignment(column == 3 ? SwingConstants.CENTER : SwingConstants.LEFT);
                setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
                
                applyStockHighlight(value, column);
                
                return this;
            }
        };
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
    }
    
    private void applyStockHighlight(Object value, int column) {
        if (column == 3) {
            try {
                int stok = Integer.parseInt(value.toString());
                if (stok <= 0) {
                    setForeground(new Color(239, 68, 68));
                    setFont(getFont().deriveFont(Font.BOLD));
                }
            } catch (Exception e) {
                // Ignore
            }
        }
    }

    private JPanel createBookingPanel() {
        JPanel panel = createGlassPanel("KUSTOMISASI TIKET", "Sesuaikan pesanan Anda");

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        // Film Info dengan gradient
        JPanel filmInfoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(41, 128, 185, 100),
                    getWidth(), getHeight(), new Color(46, 204, 113, 80)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                g2d.setColor(new Color(41, 128, 185, 150));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            }
        };
        
        filmInfoPanel.setLayout(new BoxLayout(filmInfoPanel, BoxLayout.Y_AXIS));
        filmInfoPanel.setOpaque(false);
        filmInfoPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        filmInfoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        filmInfoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        labelFilmTerpilih = new JLabel("Belum ada film dipilih");
        labelFilmTerpilih.setFont(new Font("Segoe UI", Font.BOLD, 15));
        labelFilmTerpilih.setForeground(TEXT_WHITE);
        
        labelHargaPreview = new JLabel(" ");
        labelHargaPreview.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labelHargaPreview.setForeground(TEXT_GRAY);
        
        filmInfoPanel.add(labelFilmTerpilih);
        filmInfoPanel.add(Box.createVerticalStrut(5));
        filmInfoPanel.add(labelHargaPreview);
        
        contentPanel.add(filmInfoPanel);
        contentPanel.add(Box.createVerticalStrut(20));

        // Spinners dengan style modern
        contentPanel.add(createStyledLabel("Tambahan:"));
        contentPanel.add(Box.createVerticalStrut(8));

        spinnerTiket = createSpinner();
        contentPanel.add(createSpinnerPanel("Jumlah Tiket", spinnerTiket));
        contentPanel.add(Box.createVerticalStrut(8));
        
        spinnerPopcorn = createSpinner();
        contentPanel.add(createSpinnerPanel("Popcorn (+25rb)", spinnerPopcorn));
        contentPanel.add(Box.createVerticalStrut(8));
        
        spinnerSoftdrink = createSpinner();
        contentPanel.add(createSpinnerPanel("Softdrink (+15rb)", spinnerSoftdrink));
        contentPanel.add(Box.createVerticalStrut(8));
        
        spinnerVip = createSpinner();
        contentPanel.add(createSpinnerPanel("Upgrade VIP (+50rb)", spinnerVip));
        contentPanel.add(Box.createVerticalStrut(20));

        contentPanel.add(createStyledLabel("Kalkulasi Harga:"));
        contentPanel.add(Box.createVerticalStrut(8));

        String[] options = {"Harga Normal", "Weekend (+Rp 10.000)", "Member (Diskon 10%)"};
        comboStrategy = new JComboBox<>(options);
        comboStrategy.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboStrategy.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        comboStrategy.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // --- PERBAIKAN DROPDOWN ---
        // Menggunakan Warna Putih untuk Background dan Hitam untuk Text agar kontras
        comboStrategy.setBackground(Color.WHITE); 
        comboStrategy.setForeground(Color.BLACK);
        
        // Custom renderer untuk dropdown
        comboStrategy.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                if (isSelected) {
                    setBackground(PRIMARY_COLOR);
                    setForeground(Color.WHITE);
                } else {
                    setBackground(Color.WHITE);
                    setForeground(Color.BLACK);
                }
                
                setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
                return this;
            }
        });
        
        contentPanel.add(comboStrategy);
        
        contentPanel.add(Box.createVerticalGlue());

        JScrollPane scrollForm = new JScrollPane(contentPanel);
        scrollForm.setBorder(null);
        scrollForm.setOpaque(false);
        scrollForm.getViewport().setOpaque(false);
        scrollForm.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        panel.add(scrollForm, BorderLayout.CENTER);

        // Button dengan gradient
        JPanel buttonPanel = new JPanel(new GridLayout(1, 1));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JButton btnHitung = createGradientButton("HITUNG & PESAN TIKET");
        btnHitung.addActionListener(e -> prosesPemesanan());
        
        buttonPanel.add(btnHitung);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createReceiptPanel() {
        JPanel panel = createGlassPanel("[STRUK] STRUK PEMESANAN", "Detail transaksi Anda");
        panel.setPreferredSize(new Dimension(0, 220));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        btnCetak = createGradientButton("Simpan Struk PDF");
        btnCetak.setPreferredSize(new Dimension(200, 40));
        btnCetak.setEnabled(false);
        btnCetak.addActionListener(e -> simpanPDF());

        headerPanel.add(btnCetak, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        areaStruk = new JTextArea();
        areaStruk.setEditable(false);
        areaStruk.setFont(new Font("Courier New", Font.PLAIN, 12));
        areaStruk.setBackground(new Color(15, 23, 42));
        areaStruk.setForeground(TEXT_WHITE);
        areaStruk.setCaretColor(TEXT_WHITE);
        areaStruk.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        areaStruk.setText("Silakan pilih film dari katalog dan lakukan pemesanan...");
        
        JScrollPane scroll = new JScrollPane(areaStruk);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createGlassPanel(String title, String subtitle) {
        JPanel panel = new JPanel(new BorderLayout(0, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                g2d.setColor(new Color(71, 85, 105, 150));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            }
        };
        
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(TEXT_WHITE);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblSubtitle = new JLabel(subtitle);
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitle.setForeground(TEXT_GRAY);
        lblSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        headerPanel.add(lblTitle);
        headerPanel.add(Box.createVerticalStrut(3));
        headerPanel.add(lblSubtitle);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        
        return panel;
    }

    private JLabel createStyledLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(TEXT_WHITE);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JSpinner createSpinner() {
        SpinnerNumberModel model = new SpinnerNumberModel(0, 0, 99, 1);
        JSpinner spinner = new JSpinner(model);
        spinner.setFont(new Font("Segoe UI", Font.BOLD, 14));
        spinner.setBackground(new Color(15, 23, 42));
        spinner.setForeground(TEXT_WHITE);
        
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinner.getEditor();
        editor.getTextField().setHorizontalAlignment(SwingConstants.CENTER);
        editor.getTextField().setBackground(new Color(15, 23, 42));
        editor.getTextField().setForeground(TEXT_WHITE);
        editor.getTextField().setCaretColor(TEXT_WHITE);
        
        return spinner;
    }

    private JPanel createSpinnerPanel(String labelText, JSpinner spinner) {
        JPanel panel = new JPanel(new BorderLayout(15, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(new Color(15, 23, 42, 200));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                g2d.setColor(BORDER_COLOR);
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            }
        };
        
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(TEXT_WHITE);
        
        spinner.setPreferredSize(new Dimension(70, 30));
        
        panel.add(label, BorderLayout.WEST);
        panel.add(spinner, BorderLayout.EAST);
        return panel;
    }

    private JButton createGradientButton(String text) {
        JButton btn = new JButton(text) {
            private boolean hover = false;
            
            @Override
            public void addNotify() {
                super.addNotify();
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hover = true;
                        repaint();
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        hover = false;
                        repaint();
                    }
                });
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (isEnabled()) {
                    if (hover) {
                        GradientPaint gradient = new GradientPaint(
                            0, 0, PRIMARY_COLOR,
                            getWidth(), 0, ACCENT_GREEN
                        );
                        g2d.setPaint(gradient);
                    } else {
                        g2d.setColor(ACCENT_GREEN);
                    }
                } else {
                    g2d.setColor(new Color(71, 85, 105));
                }
                
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent()) / 2 - 2;
                g2d.drawString(getText(), textX, textY);
            }
        };
        
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(100, 50));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return btn;
    }

    private void loadDataFilm() {
        tableModel.setRowCount(0);
        List<Film> films = database.getAll();
        for (Film f : films) {
            tableModel.addRow(new Object[]{
                f.getTitle(), 
                f.getGenre(), 
                formatRupiah(f.getPrice()),
                f.getStock()
            });
        }
    }

    private void updateFilmSelection() {
        if (selectedFilm != null) {
            labelFilmTerpilih.setText(selectedFilm.getTitle());
            labelHargaPreview.setText("Genre: " + selectedFilm.getGenre() + " | Harga: " + formatRupiah(selectedFilm.getPrice()) + " | Stok: " + selectedFilm.getStock());
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
            showStyledDialog("Silakan pilih film dari katalog!", "Perhatian", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int jumlahTiket = (Integer) spinnerTiket.getValue();
        int jumlahPopcorn = (Integer) spinnerPopcorn.getValue();
        int jumlahSoftdrink = (Integer) spinnerSoftdrink.getValue();
        int jumlahVip = (Integer) spinnerVip.getValue();

        if (jumlahTiket == 0) {
            showStyledDialog("Jumlah tiket minimal 1!", "Perhatian", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (jumlahVip > jumlahTiket) {
            showStyledDialog("Jumlah VIP Seat tidak boleh melebihi jumlah tiket!", "Validasi Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedFilm.getStock() < jumlahTiket) {
            showStyledDialog("Stok kursi habis! Sisa: " + selectedFilm.getStock(), "Stok Habis", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (database.getItemStock("Popcorn") < jumlahPopcorn) {
            showStyledDialog("Stok Popcorn habis! Sisa: " + database.getItemStock("Popcorn"), "Stok Habis", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (database.getItemStock("Softdrink") < jumlahSoftdrink) {
            showStyledDialog("Stok Softdrink habis! Sisa: " + database.getItemStock("Softdrink"), "Stok Habis", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double hargaDasar = selectedFilm.getPrice() * jumlahTiket;
        double hargaPopcorn = 25000.0 * jumlahPopcorn;
        double hargaSoftdrink = 15000.0 * jumlahSoftdrink;
        double hargaVip = 50000.0 * jumlahVip;
        
        double hargaTotal = hargaDasar + hargaPopcorn + hargaSoftdrink + hargaVip;

        PricingStrategy strategy = getSelectedStrategy();
        String tipeBayar = (String) comboStrategy.getSelectedItem();
        double hargaAkhir = strategy.hitungHargaAkhir(hargaTotal);

        selectedFilm.reduceStock(jumlahTiket);
        database.reduceItemStock("Popcorn", jumlahPopcorn);
        database.reduceItemStock("Softdrink", jumlahSoftdrink);
        database.updateFilmStock();
        
        loadDataFilm();
        updateFilmSelection();

        StringBuilder struk = new StringBuilder();
        struk.append(String.format("╔════════════════════════════════════════════════╗%n"));
        struk.append(String.format("║  [FILM] CINEMA BOOKING SYSTEM - KELAS 2C      ║%n"));
        struk.append(String.format("║   Tugas Besar Pemrograman Berorientasi Objek  ║%n"));
        struk.append(String.format("╚════════════════════════════════════════════════╝%n"));
        struk.append(String.format("%n"));
        struk.append(String.format(" Film        : %-35s%n", truncate(selectedFilm.getTitle(), 35)));
        struk.append(String.format(" Genre       : %-35s%n", truncate(selectedFilm.getGenre(), 35)));
        struk.append(String.format("───────────────────────────────────────────────────%n"));
        struk.append(String.format(" DETAIL PESANAN:%n"));
        struk.append(String.format(" [TIKET] Tiket Reguler : %2d x %s%n", jumlahTiket, formatRupiah(selectedFilm.getPrice())));
        
        if (jumlahPopcorn > 0) struk.append(String.format(" [FOOD] Popcorn Large  : %2d x Rp25.000,00%n", jumlahPopcorn));
        if (jumlahSoftdrink > 0) struk.append(String.format(" [DRINK] Softdrink     : %2d x Rp15.000,00%n", jumlahSoftdrink));
        if (jumlahVip > 0) struk.append(String.format(" [VIP] Upgrade VIP Seat: %2d x Rp50.000,00%n", jumlahVip));
        
        struk.append(String.format("═══════════════════════════════════════════════════%n"));
        struk.append(String.format(" Subtotal     : %s%n", formatRupiah(hargaTotal)));
        struk.append(String.format("───────────────────────────────────────────────────%n"));
        struk.append(String.format(" Metode       : %-35s%n", truncate(tipeBayar, 35)));
        struk.append(String.format(" TOTAL BAYAR  : %s%n", formatRupiah(hargaAkhir)));
        struk.append(String.format("═══════════════════════════════════════════════════%n"));
        struk.append(String.format("%n      [OK] Terima kasih! Happy Watching! [OK]%n"));

        areaStruk.setText(struk.toString());
        btnCetak.setEnabled(true);
        
        showStyledDialog("[OK] Pemesanan berhasil!\nStok telah disimpan permanen.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
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
                showStyledDialog("[OK] Struk berhasil disimpan ke:\n" + filePath, "Sukses PDF", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
                showStyledDialog("[ERROR] Gagal menyimpan PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showStyledDialog(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    private String truncate(String str, int length) {
        return str.length() > length ? str.substring(0, length - 3) + "..." : str;
    }

    private String formatRupiah(double number) {
        return NumberFormat.getCurrencyInstance(Locale.of("id", "ID")).format(number);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}