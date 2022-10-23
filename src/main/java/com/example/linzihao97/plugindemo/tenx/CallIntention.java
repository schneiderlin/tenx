package com.example.linzihao97.plugindemo.tenx;

import com.example.linzihao97.plugindemo.TextAreaDialog;
import com.example.linzihao97.plugindemo.runcode.Core;
import com.example.linzihao97.plugindemo.utils.PsiUtils;
import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.lang.jvm.JvmNamedElement;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@NonNls
public class CallIntention extends PsiElementBaseIntentionAction implements IntentionAction {

    private static final Gson gson = new Gson();

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

        // 获取静态信息
        String className = psiClass.getQualifiedName();
        String methodName = method.getName();
        PsiParameterList parameterList = method.getParameterList();
        List<String> parameterTypes = new ArrayList<>();
        for (int i = 0; i < parameterList.getParametersCount(); i++) {
            PsiParameter parameter = Objects.requireNonNull(parameterList.getParameter(i));
            String canonicalText = parameter.getType().getCanonicalText();
            parameterTypes.add(canonicalText);
        }
        List<String> parameterNames = Arrays.stream(method.getParameters())
                .map(JvmNamedElement::getName)
                .collect(Collectors.toList());

        // 生成默认的 json
        JsonObject jsonObject = new JsonObject();
        parameterNames.forEach(name -> {
            jsonObject.add(name, JsonNull.INSTANCE);
        });
        String placeHolder = jsonObject.toString();

        TextAreaDialog dialog = new TextAreaDialog(project, "Generate call code",placeHolder);
        dialog.setOkAction(() -> {
            try {
                String content = dialog.getText();
                // 初始化代码
                ReplClient.evalClient("(import repl.R)");
                ReplClient.evalClient("(defn bean-by-class-name [class-name]\n" +
                        "  (let [class (Class/forName class-name)]\n" +
                        "    (R/getBean class)))");
                ReplClient.evalClient("(defn class-str->class [class-str]\n" +
                        "  (cond (= class-str \"long\") Long/TYPE\n" +
                        "        (= class-str \"int\") Integer/TYPE\n" +
                        "        :else (Class/forName class-str)))");
                ReplClient.evalClient("(defn call-with-json [class-name method-name arg-names arg-class-names json]\n" +
                        "  (let [instance (bean-by-class-name class-name)\n" +
                        "        arg-classes (map #(class-str->class %) arg-class-names)]\n" +
                        "    (R/callMethod\n" +
                        "           instance\n" +
                        "           method-name\n" +
                        "           arg-names\n" +
                        "           arg-classes\n" +
                        "           json)))");

                ReplClient.evalClient(Core.callWithJsonCode(className, methodName, parameterNames, parameterTypes, content));
            } catch (Exception e) {
                String msg = "Call method Exception: " + e.getMessage();
                MyNotifier.notifyError(project, msg);
            }
        });
        dialog.show();
    }

    public boolean startInWriteAction() {
        return false;
    }

}