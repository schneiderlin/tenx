package com.example.linzihao97.plugindemo;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.JBDimension;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class TextAreaDialog extends DialogWrapper {
    private final ContentPanel contentPanel;

    public TextAreaDialog(Project project, String text) {
        super(project);
        setTitle("Import Fields From DTO");
        contentPanel = new ContentPanel(text);
        init();
        getOKAction().putValue(Action.NAME, "Create");
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return contentPanel;
    }

    private static class ContentPanel extends JBPanel<ContentPanel> {
        final JBTextArea textArea;

        public ContentPanel(String text) {
            super(new GridBagLayout());
            setPreferredSize(new JBDimension(600, 500));

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.anchor = GridBagConstraints.WEST;
            constraints.weightx = 1;

            textArea = new JBTextArea();
            textArea.setText(text);
            add(textArea, constraints);
        }
    }
}
