package com.codealpha.hotel.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AnimatedButton extends JButton {
    private Color normalColor;
    private Color hoverColor;

    public AnimatedButton(String text, Color normalColor, Color hoverColor) {
        super(text);
        this.normalColor = normalColor;
        this.hoverColor = hoverColor;
        setBackground(normalColor);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setFont(new Font("Segoe UI", Font.BOLD, 13));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(AnimatedButton.this.hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(AnimatedButton.this.normalColor);
            }
        });
    }
}