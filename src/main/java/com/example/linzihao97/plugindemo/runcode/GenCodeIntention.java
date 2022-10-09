package com.example.linzihao97.plugindemo.runcode;

import com.example.linzihao97.plugindemo.TextAreaDialog;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@NonNls
public class GenCodeIntention extends PsiElementBaseIntentionAction implements IntentionAction {

    @NotNull
    public String getText() {
        return "Generate Clojure create class instance code";
    }

    @NotNull
    public String getFamilyName() {
        return "Clojure";
    }

    public boolean isAvailable(@NotNull Project project, Editor editor, @Nullable PsiElement element) {
        if (element == null) {
            return false;
        }

        PsiElement parent = element.getParent();
        return parent instanceof PsiClass;
    }

    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element)
            throws IncorrectOperationException {
        PsiClass psiClass = (PsiClass) element.getParent();
        String createCode = Core.createCode(psiClass);

        TextAreaDialog dialog = new TextAreaDialog(project, "Generate dto code", createCode);
        dialog.show();
    }

    public boolean startInWriteAction() {
        return false;
    }

}