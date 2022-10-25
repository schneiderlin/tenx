package com.example.linzihao97.plugindemo;

import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.ide.highlighter.HighlighterFactory;
import com.intellij.json.JsonFileType;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.Condition;
import com.intellij.psi.*;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.LocalTimeCounter;
import com.intellij.util.TextFieldCompletionProvider;
import com.intellij.util.textCompletion.TextCompletionUtil;
import com.intellij.util.textCompletion.TextFieldWithCompletion;
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
        private final FileType fileType = JsonFileType.INSTANCE;

        public JSONEditor(String text, Project project) {
            super(text, project, JsonFileType.INSTANCE);
            super.setDocument(createDocument(text));
        }

        protected Document createDocument(String initText) {
            final PsiFileFactory factory = PsiFileFactory.getInstance(project);
            final long stamp = LocalTimeCounter.currentTime();
            final PsiFile psiFile = factory.createFileFromText("Dummy." + fileType.getDefaultExtension(), fileType, initText, stamp, true, false);

            //TextCompletionUtil.installProvider(psiFile, new MyTextFieldCompletionProvider(), true);
            return PsiDocumentManager.getInstance(project).getDocument(psiFile);
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

    private static class MyTextFieldCompletionProvider extends TextFieldCompletionProvider implements DumbAware {
        @Override
        protected void addCompletionVariants(@NotNull String text, int offset, @NotNull String prefix, @NotNull CompletionResultSet result) {
            result.addElement(LookupElementBuilder.create("123456"));
            result.addElement(LookupElementBuilder.create("234567"));
        }
    }
}
