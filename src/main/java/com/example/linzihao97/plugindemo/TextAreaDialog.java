package com.example.linzihao97.plugindemo;

import com.intellij.json.JsonLanguage;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.LanguageTextField;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.JBDimension;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class TextAreaDialog extends DialogWrapper {
    private final ContentPanel contentPanel;
    private Runnable okAction;

    public TextAreaDialog(Project project, String title, String text) {
        super(project);
        setTitle(title);
        contentPanel = new ContentPanel(text, project);
        init();
        getOKAction().putValue(Action.NAME, "Ok");
    }

    public void setOkAction(Runnable runnable) {
        okAction = runnable;
    }

    public String getText() {
        return contentPanel.getText();
    }

    @Override
    protected void doOKAction() {
        okAction.run();
        super.doOKAction();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return contentPanel;
    }

    private static class ContentPanel extends JBPanel<ContentPanel> {
        final LanguageTextField textArea;

        public String getText() {
            return textArea.getText();
        }

        public ContentPanel(String text, Project project) {
            super(new GridBagLayout());
            setPreferredSize(new JBDimension(600, 500));

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.anchor = GridBagConstraints.WEST;
            constraints.weightx = 1;
            constraints.weighty = 1;

            textArea = new LanguageTextField(JsonLanguage.INSTANCE, project, text, false);
            add(textArea, constraints);
        }
    }
}
