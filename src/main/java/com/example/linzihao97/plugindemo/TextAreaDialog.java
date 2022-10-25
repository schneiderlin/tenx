package com.example.linzihao97.plugindemo;

import com.intellij.ide.highlighter.HighlighterFactory;
import com.intellij.json.JsonFileType;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.JBDimension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class TextAreaDialog extends DialogWrapper {
    private final ContentPanel contentPanel;
    private Runnable okAction;

    private Project project;

    public TextAreaDialog(Project project, String title, String text) {
        super(project);
        this.project = project;
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

    private class ContentPanel extends JBPanel<ContentPanel> {
        final EditorTextField textArea;

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

            textArea = new JSONEditor(text, project);
            add(textArea, constraints);
        }
    }

    private class JSONEditor extends EditorTextField {
        public JSONEditor(String text, Project project) {
            super(text, project, JsonFileType.INSTANCE);
            super.setDocument(super.createDocument());
        }

        @Override
        protected @NotNull EditorEx createEditor() {
            final EditorEx ex = super.createEditor();

            ex.setHighlighter(HighlighterFactory.createHighlighter(project, JsonFileType.INSTANCE));
            ex.setEmbeddedIntoDialogWrapper(true);
            ex.setOneLineMode(false);

            return ex;
        }
    }
}
