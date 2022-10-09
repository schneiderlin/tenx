package com.example.linzihao97.plugindemo.findEntry;

import com.example.linzihao97.plugindemo.TextAreaDialog;
import com.example.linzihao97.plugindemo.runcode.Core;
import com.example.linzihao97.plugindemo.utils.PsiUtils;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.Query;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Collectors;

@NonNls
public class FindEntryIntention extends PsiElementBaseIntentionAction implements IntentionAction {

    @NotNull
    public String getText() {
        return "找到调用入口, http / 事件消费 / xxl";
    }

    @NotNull
    public String getFamilyName() {
        return "Find Code";
    }

    public boolean isAvailable(@NotNull Project project, Editor editor, @Nullable PsiElement element) {
        if (element == null) {
            return false;
        }

        PsiMethod method = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
        return method != null;
    }

    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element)
            throws IncorrectOperationException {
        PsiMethod method = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
        assert method != null;

        Query<PsiMethod> caller = PsiUtils.findCallerRecur(method, 30);
        Query<PsiMethod> entryCaller = caller
                .filtering(m -> PsiUtils.hasAnnotation(m, "com.shopcider.plutus.component.annotation.CiderResponseBody") ||
                        PsiUtils.hasAnnotation(m, "com.shopcider.plutus.component.eventbus.CiderSubscribe") ||
                        PsiUtils.hasAnnotation(m, "com.xxl.job.core.handler.annotation.XxlJob"));

        String result = entryCaller.findAll()
                .stream()
                .map(PsiMethod::getName)
                .collect(Collectors.joining("\n"));

        TextAreaDialog dialog = new TextAreaDialog(project, "Import Fields From DTO", result);
        dialog.show();
    }


    public boolean startInWriteAction() {
        return false;
    }

}