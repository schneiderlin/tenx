package com.example.linzihao97.plugindemo.tenx;

import clojure.lang.Obj;
import com.example.linzihao97.plugindemo.TextAreaDialog;
import com.example.linzihao97.plugindemo.runcode.Core;
import com.example.linzihao97.plugindemo.utils.PsiUtils;
import com.google.gson.Gson;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

@NonNls
public class CallIntention extends PsiElementBaseIntentionAction implements IntentionAction {

    @NotNull
    public String getText() {
        return "Call this method";
    }

    @NotNull
    public String getFamilyName() {
        return "Clojure";
    }

    public boolean isAvailable(@NotNull Project project, Editor editor, @Nullable PsiElement element) {
        if (element == null) {
            return false;
        }

        PsiMethod method = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
        return method != null && !PsiUtils.isStatic(method);
    }

    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element)
            throws IncorrectOperationException {
        PsiMethod method = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
        assert method != null;
        PsiClass psiClass = PsiUtils.getDeclaringClass(method);

        TextAreaDialog dialog = new TextAreaDialog(project, "{}");
        dialog.setOkAction(() -> {
            String json = dialog.getText();
            Gson gson = new Gson();
            Map<String, Object> params = gson.fromJson(json, Map.class);

            long companyId = ((Double) params.get("companyId")).longValue();
            params.put("companyId", companyId);

            String phone = (String) params.get("phone");
            phone = "\"" + phone + "\"";
            params.put("phone", phone);

            String varName = Core.to_kebab_case(psiClass.getName());
            String defCode = Core.getBeanCode(psiClass, varName);
            String methodCallCode = Core.methodCallCode(method, Optional.of(varName), params);

            ReplClient.evalClient("(import repl.R)");
            ReplClient.evalClient(defCode);
            ReplClient.evalClient(methodCallCode);
        });
        dialog.show();
        System.out.println(1);
    }

    public boolean startInWriteAction() {
        return false;
    }

}