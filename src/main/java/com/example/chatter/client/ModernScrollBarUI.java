package com.example.chatter.client;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class ModernScrollBarUI extends BasicScrollBarUI {
    private static final int THUMB_RADIUS = 6;
    private static final int TRACK_RADIUS = 4;
    private static final int ANIMATION_SPEED = 150; // milliseconds
    private static final int THUMB_MIN_SIZE = 30;
    
    // Color scheme
    private static final Color TRACK_COLOR = new Color(248, 249, 250);
    private static final Color TRACK_HOVER_COLOR = new Color(241, 243, 244);
    private static final Color THUMB_COLOR = new Color(201, 203, 207);
    private static final Color THUMB_HOVER_COLOR = new Color(174, 176, 180);
    private static final Color THUMB_PRESSED_COLOR = new Color(138, 143, 152);
    
    // Animation state
    private float hoverAlpha = 0.0f;
    private Timer hoverTimer;
    private boolean isHovering = false;
    private boolean isPressed = false;
    private Point mousePosition = new Point();

    @Override
    protected void installDefaults() {
        super.installDefaults();
        scrollbar.setOpaque(false);
        scrollbar.setUnitIncrement(16);
        scrollbar.setBlockIncrement(64);
    }

    @Override
    protected void installListeners() {
        super.installListeners();
        
        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                startHoverAnimation(true);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                startHoverAnimation(false);
            }
            
            @Override
            public void mouseMoved(MouseEvent e) {
                mousePosition = e.getPoint();
                scrollbar.repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                scrollbar.repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                scrollbar.repaint();
            }
        };
        
        scrollbar.addMouseListener(mouseHandler);
        scrollbar.addMouseMotionListener(mouseHandler);
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createInvisibleButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createInvisibleButton();
    }

    private JButton createInvisibleButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        button.setVisible(false);
        button.setBorder(null);
        button.setFocusable(false);
        return button;
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        // Create gradient for track
        Color trackColor = interpolateColor(TRACK_COLOR, TRACK_HOVER_COLOR, hoverAlpha);
        
        // Paint rounded track
        RoundRectangle2D track = new RoundRectangle2D.Float(
            trackBounds.x + 2, trackBounds.y + 2, 
            trackBounds.width - 4, trackBounds.height - 4, 
            TRACK_RADIUS, TRACK_RADIUS
        );
        
        g2.setColor(trackColor);
        g2.fill(track);
        
        // Add subtle inner shadow
        g2.setColor(new Color(0, 0, 0, 10));
        g2.drawRoundRect(
            trackBounds.x + 2, trackBounds.y + 2, 
            trackBounds.width - 5, trackBounds.height - 5, 
            TRACK_RADIUS, TRACK_RADIUS
        );
        
        g2.dispose();
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
            return;
        }
        
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        // Ensure minimum thumb size
        Rectangle adjustedBounds = new Rectangle(thumbBounds);
        if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
            adjustedBounds.height = Math.max(adjustedBounds.height, THUMB_MIN_SIZE);
        } else {
            adjustedBounds.width = Math.max(adjustedBounds.width, THUMB_MIN_SIZE);
        }
        
        // Calculate thumb color based on state
        Color thumbColor = THUMB_COLOR;
        if (isPressed) {
            thumbColor = THUMB_PRESSED_COLOR;
        } else if (isHovering) {
            thumbColor = interpolateColor(THUMB_COLOR, THUMB_HOVER_COLOR, hoverAlpha);
        }
        
        // Create rounded thumb with padding
        int padding = 3;
        RoundRectangle2D thumb = new RoundRectangle2D.Float(
            adjustedBounds.x + padding, 
            adjustedBounds.y + padding,
            adjustedBounds.width - (padding * 2), 
            adjustedBounds.height - (padding * 2),
            THUMB_RADIUS, THUMB_RADIUS
        );
        
        // Paint thumb with gradient
        GradientPaint gradient = new GradientPaint(
            adjustedBounds.x, adjustedBounds.y, brighten(thumbColor, 0.1f),
            adjustedBounds.x, adjustedBounds.y + adjustedBounds.height, darken(thumbColor, 0.1f)
        );
        g2.setPaint(gradient);
        g2.fill(thumb);
        
        // Add subtle highlight
        g2.setColor(new Color(255, 255, 255, 30));
        RoundRectangle2D highlight = new RoundRectangle2D.Float(
            adjustedBounds.x + padding, 
            adjustedBounds.y + padding,
            adjustedBounds.width - (padding * 2), 
            2,
            THUMB_RADIUS, THUMB_RADIUS
        );
        g2.fill(highlight);
        
        // Add subtle shadow
        g2.setColor(new Color(0, 0, 0, 20));
        g2.drawRoundRect(
            adjustedBounds.x + padding, 
            adjustedBounds.y + padding,
            adjustedBounds.width - (padding * 2) - 1, 
            adjustedBounds.height - (padding * 2) - 1,
            THUMB_RADIUS, THUMB_RADIUS
        );
        
        g2.dispose();
    }

    private void startHoverAnimation(boolean hover) {
        if (hoverTimer != null && hoverTimer.isRunning()) {
            hoverTimer.stop();
        }
        
        isHovering = hover;
        float targetAlpha = hover ? 1.0f : 0.0f;
        float startAlpha = hoverAlpha;
        
        hoverTimer = new Timer(10, e -> {
            float progress = Math.min(1.0f, (System.currentTimeMillis() % ANIMATION_SPEED) / (float) ANIMATION_SPEED);
            hoverAlpha = startAlpha + (targetAlpha - startAlpha) * easeInOutCubic(progress);
            
            if (Math.abs(hoverAlpha - targetAlpha) < 0.01f) {
                hoverAlpha = targetAlpha;
                hoverTimer.stop();
            }
            
            scrollbar.repaint();
        });
        
        hoverTimer.start();
    }
    
    private float easeInOutCubic(float t) {
        return t < 0.5f ? 4 * t * t * t : 1 - (float) Math.pow(-2 * t + 2, 3) / 2;
    }
    
    private Color interpolateColor(Color color1, Color color2, float factor) {
        factor = Math.max(0, Math.min(1, factor));
        return new Color(
            (int) (color1.getRed() + factor * (color2.getRed() - color1.getRed())),
            (int) (color1.getGreen() + factor * (color2.getGreen() - color1.getGreen())),
            (int) (color1.getBlue() + factor * (color2.getBlue() - color1.getBlue())),
            (int) (color1.getAlpha() + factor * (color2.getAlpha() - color1.getAlpha()))
        );
    }
    
    private Color brighten(Color color, float factor) {
        return new Color(
            Math.min(255, (int) (color.getRed() + 255 * factor)),
            Math.min(255, (int) (color.getGreen() + 255 * factor)),
            Math.min(255, (int) (color.getBlue() + 255 * factor)),
            color.getAlpha()
        );
    }
    
    private Color darken(Color color, float factor) {
        return new Color(
            Math.max(0, (int) (color.getRed() - 255 * factor)),
            Math.max(0, (int) (color.getGreen() - 255 * factor)),
            Math.max(0, (int) (color.getBlue() - 255 * factor)),
            color.getAlpha()
        );
    }

    @Override
    protected Dimension getMinimumThumbSize() {
        return new Dimension(THUMB_MIN_SIZE, THUMB_MIN_SIZE);
    }
    
    // Usage example
    public static void applyToScrollPane(JScrollPane scrollPane) {
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
    }
}