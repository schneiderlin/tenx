package com.example.linzihao97.plugindemo.tenx;

import com.example.linzihao97.plugindemo.TextAreaDialog;
import com.example.linzihao97.plugindemo.utils.PsiUtils;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
            String content = dialog.getText();
            String className = psiClass.getQualifiedName();
            String methodName = method.getName();
            List<String> parameterTypes = new ArrayList<>();
            PsiParameterList parameterList = method.getParameterList();
            for (int i = 0; i < parameterList.getParametersCount(); i++) {
                PsiParameter parameter = Objects.requireNonNull(parameterList.getParameter(i));
                String canonicalText = parameter.getType().getCanonicalText();
                parameterTypes.add(canonicalText);
            }

//            String varName = Core.to_kebab_case(psiClass.getName());
//            String defCode = Core.getBeanCode(psiClass, varName);
//            String methodCallCode = Core.methodCallCode(method, Optional.of(varName), params);
//
//            ReplClient.evalClient("(import repl.R)");
//            ReplClient.evalClient(defCode);
//            ReplClient.evalClient(methodCallCode);
        });
        dialog.show();
        System.out.println(1);
    }

//    private Map<String, Class> toJabaClass(PsiType psiType) {
//        GlobalSearchScope scope = GlobalSearchScope.projectScope(project);
//        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
//        String parameterType = method.getParameterList().getParameter(0).getType().getCanonicalText();
//        PsiClass userClass = javaPsiFacade.findClass(parameterType, scope);
//        String canonicalText = userClass.getFields()[0].getType().getCanonicalText();
//    }

    public boolean startInWriteAction() {
        return false;
    }

}