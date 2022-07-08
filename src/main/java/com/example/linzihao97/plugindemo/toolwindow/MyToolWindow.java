package com.example.linzihao97.plugindemo.toolwindow;

import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;

public class MyToolWindow {
    private JButton someButton;
    private JLabel someLabel;
    private JPanel content;

    public MyToolWindow(ToolWindow toolWindow) {
        someButton.setText("some action");
        someLabel.setText("some label");
    }

    public JPanel getContent() {
        return content;
    }
}
