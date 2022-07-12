package com.example.linzihao97.plugindemo.intension;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@NonNls
public class GenIntention extends PsiElementBaseIntentionAction implements IntentionAction {

    @NotNull
    public String getText() {
        return "Generate clojure generator";
    }

    @NotNull
    public String getFamilyName() {
        return "ConditionalOperatorIntention";
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
        PsiClass psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class, false);
        Arrays.stream(psiClass.getAllFields())
                .map(psiField -> {
                    String fieldName = psiField.getName();
                    String fieldTypeName = psiField.getType().getCanonicalText();

                    return 1;
                });
        System.out.println("generate");
    }

    public boolean startInWriteAction() {
        return true;
    }

}