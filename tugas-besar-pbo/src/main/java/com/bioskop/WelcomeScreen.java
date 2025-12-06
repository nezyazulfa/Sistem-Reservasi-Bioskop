package com.bioskop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WelcomeScreen extends JFrame {
    
    // Color Palette - MATCHING dengan Main.java
    private static final Color DARK_BG = new Color(15, 23, 42);
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185); 
    private static final Color ACCENT_COLOR = new Color(46, 204, 113);  
    private static final Color ACCENT_PINK = new Color(236, 72, 153);
    private static final Color TEXT_WHITE = new Color(248, 250, 252);
    private static final Color TEXT_GRAY = new Color(148, 163, 184);
    
    private JPanel contentPanel;
    private Timer fadeTimer;
    private float opacity = 0.0f;
    private double iconAngle = 0;

    public WelcomeScreen() {
        setTitle("Cinema Booking System");
        setSize(1000, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        
        // Set opacity untuk window
        try {
            setOpacity(0.0f);
        } catch (Exception e) {
            // Ignore if not supported
        }
        
        // Panel utama dengan gradient background
        contentPanel = new JPanel() {
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
                
                // Decorative circles (blur effect simulation)
                drawGlowCircle(g2d, 150, 100, 200, PRIMARY_COLOR, 0.1f);
                drawGlowCircle(g2d, getWidth() - 150, getHeight() - 100, 250, ACCENT_PINK, 0.08f);
                drawGlowCircle(g2d, getWidth() / 2, getHeight() / 2, 300, ACCENT_COLOR, 0.05f);
            }
            
            private void drawGlowCircle(Graphics2D g2d, int x, int y, int size, Color color, float alpha) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                GradientPaint glow = new GradientPaint(
                    x, y, new Color(color.getRed(), color.getGreen(), color.getBlue(), 150),
                    x + (float)size, y + (float)size, new Color(color.getRed(), color.getGreen(), color.getBlue(), 0)
                );
                g2d.setPaint(glow);
                g2d.fillOval(x - size / 2, y - size / 2, size, size);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            }
        };
        
        contentPanel.setLayout(null);
        add(contentPanel);
        
        createContent();
        startFadeInAnimation();
    }
    
    private void createContent() {
        // Close button (top right)
        JButton closeBtn = createIconButton("✕", 950, 20, 30, 30);
        closeBtn.addActionListener(e -> System.exit(0));
        contentPanel.add(closeBtn);
        
        // Center Card Container
        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Card background with rounded corners
                g2d.setColor(new Color(30, 41, 59, 230));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                
                // Border glow
                g2d.setColor(new Color(41, 128, 185, 100));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
            }
        };
        
        cardPanel.setLayout(null);
        cardPanel.setBounds(150, 100, 700, 500);
        cardPanel.setOpaque(false);
        
        // Film icon (animated)
        JLabel iconLabel = new JLabel("🎬", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        iconLabel.setBounds(250, 60, 200, 100);
        cardPanel.add(iconLabel);
        
        // Title
        JLabel titleLabel = new JLabel("CINEMA BOOKING", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        titleLabel.setForeground(TEXT_WHITE);
        titleLabel.setBounds(50, 200, 600, 50);
        cardPanel.add(titleLabel);
        
        // Subtitle with gradient effect
        JLabel subtitleLabel = new JLabel("SYSTEM", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        subtitleLabel.setForeground(PRIMARY_COLOR);
        subtitleLabel.setBounds(50, 245, 600, 50);
        cardPanel.add(subtitleLabel);
        
        // Start Button dengan hover effect
        JButton startBtn = new JButton("MULAI PEMESANAN") {
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
                    
                    @Override
                    public void mousePressed(MouseEvent e) {
                        setBounds(201, 371, 298, 53);
                    }
                    
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        setBounds(200, 370, 300, 55);
                    }
                });
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (hover) {
                    GradientPaint gradient = new GradientPaint(
                        0, 0, PRIMARY_COLOR,
                        getWidth(), 0, ACCENT_COLOR
                    );
                    g2d.setPaint(gradient);
                } else {
                    g2d.setColor(ACCENT_COLOR);
                }
                
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Button text
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent()) / 2 - 2;
                g2d.drawString(getText(), textX, textY);
            }
            
            @Override
            protected void paintBorder(Graphics g) {
                // No border
            }
        };
        
        startBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        startBtn.setForeground(TEXT_WHITE);
        startBtn.setBounds(200, 370, 300, 55);
        startBtn.setFocusPainted(false);
        startBtn.setContentAreaFilled(false);
        startBtn.setBorderPainted(false);
        startBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startBtn.addActionListener(e -> openMainApplication());
        
        cardPanel.add(startBtn);
        
        contentPanel.add(cardPanel);
        
        // Footer text
        JLabel footerLabel = new JLabel("© 2025 Cinema Booking System", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(TEXT_GRAY);
        footerLabel.setBounds(0, 640, 1000, 30);
        contentPanel.add(footerLabel);
        
        Timer iconTimer = new Timer(30, e -> {
            iconAngle += 0.02; 
            double scale = 1 + Math.sin(iconAngle) * 0.05;
            iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, (int)(80 * scale)));
        });
        iconTimer.start();
    }
    
    private JButton createIconButton(String text, int x, int y, int width, int height) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2d.setColor(new Color(239, 68, 68));
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(248, 113, 113));
                } else {
                    g2d.setColor(new Color(71, 85, 105));
                }
                
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 16));
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent()) / 2 - 2;
                g2d.drawString(getText(), textX, textY);
            }
        };
        
        btn.setBounds(x, y, width, height);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    private void startFadeInAnimation() {
        fadeTimer = new Timer(20, e -> {
            opacity += 0.05f;
            if (opacity >= 1.0f) {
                opacity = 1.0f;
                fadeTimer.stop();
            }
            try {
                setOpacity(opacity);
            } catch (Exception ex) {
                // Ignore
            }
        });
        fadeTimer.start();
    }
    
    private void openMainApplication() {
        // Fade out animation
        Timer fadeOutTimer = new Timer(20, e -> {
            opacity -= 0.08f;
            if (opacity <= 0.0f) {
                opacity = 0.0f;
                ((Timer) e.getSource()).stop();
                
                dispose();
                
                SwingUtilities.invokeLater(() -> {
                    Main mainFrame = new Main();
                    mainFrame.setVisible(true);
                });
            }
            try {
                setOpacity(opacity);
            } catch (Exception ex) {
                // Ignore
            }
        });
        fadeOutTimer.start();
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            WelcomeScreen welcome = new WelcomeScreen();
            welcome.setVisible(true);
        });
    }
}