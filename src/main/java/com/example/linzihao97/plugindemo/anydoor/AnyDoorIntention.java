package com.example.linzihao97.plugindemo.anydoor;

import com.example.linzihao97.plugindemo.TextAreaDialog;
import com.example.linzihao97.plugindemo.settings.AayDoorSettingsState;
import com.example.linzihao97.plugindemo.utils.PsiUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;

@NonNls
public class AnyDoorIntention extends PsiElementBaseIntentionAction implements IntentionAction {

    @NotNull
    public String getText() {
        return "Open any door";
    }

    @NotNull
    public String getFamilyName() {
        return "Any door";
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

        TextAreaDialog dialog = new TextAreaDialog(project, "Generate call code","{}");
        dialog.setOkAction(() -> {
            String content = dialog.getText();
            String className = psiClass.getQualifiedName();
            String methodName = method.getName();
//            List<String> parameterTypes = new ArrayList<>();
            JsonArray parameterTypes = new JsonArray();
            PsiParameterList parameterList = method.getParameterList();
            for (int i = 0; i < parameterList.getParametersCount(); i++) {
                PsiParameter parameter = Objects.requireNonNull(parameterList.getParameter(i));
                String canonicalText = parameter.getType().getCanonicalText();
                parameterTypes.add(canonicalText);
            }
            Integer port = project.getService(AayDoorSettingsState.class).port;

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("content", content);
            jsonObject.addProperty("methodName", methodName);
            jsonObject.addProperty("className", className);
            jsonObject.add("parameterTypes", parameterTypes);

            // http 请求
            Thread thread = new Thread(() -> post("http://127.0.0.1:" + port + "/any_door/run", jsonObject.toString()));
            thread.start();


        });
        dialog.show();
    }

    public boolean startInWriteAction() {
        return false;
    }

//    private static <T> T using(Supplier<T> f) {
//        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
//        try {
//            ClassLoader loader = AnyDoorIntention.class.getClassLoader();
//            Thread.currentThread().setContextClassLoader(loader);
//            return f.get();
//        } finally {
//            Thread.currentThread().setContextClassLoader(oldLoader);
//        }
//    }

    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    /*
     *  POST请求
     *  参数是：URL，jsonObject(请求参数封装成json对象)
     *
     * */
    public static String post(String url, String body) {
        //创建HttpClients对象

        //创建post请求对象
        HttpPost httpPost = new HttpPost(url);
        //创建封装请求参数对象，设置post请求参数
        httpPost.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
        try {
            //执行POST请求，获取请求结果
            HttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                // 发送成功
                return EntityUtils.toString(httpResponse.getEntity());
            } else {
                // 发送失败
                return null;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}