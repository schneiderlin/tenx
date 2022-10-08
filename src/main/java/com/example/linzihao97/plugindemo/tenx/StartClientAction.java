package com.example.linzihao97.plugindemo.tenx;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class StartClientAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        ReplClientService service = project.getService(ReplClientService.class);
        service.start();

        //// 初始化代码
        //ReplClient.evalClient("(import repl.R)");
        //ReplClient.evalClient("(defn bean-by-class-name [class-name]\n" +
        //        "  (let [class (Class/forName class-name)]\n" +
        //        "    (R/getBean class)))");
        //ReplClient.evalClient("(defn class-str->class [class-str]\n" +
        //        "  (cond (= class-str \"long\") Long/TYPE\n" +
        //        "        (= class-str \"int\") Integer/TYPE\n" +
        //        "        :else (Class/forName class-str)))");
        //ReplClient.evalClient("(defn call-with-json [class-name method-name arg-names arg-class-names json]\n" +
        //        "  (let [instance (bean-by-class-name class-name)\n" +
        //        "        arg-classes (map #(class-str->class %) arg-class-names)]\n" +
        //        "    (R/callMethod\n" +
        //        "           instance\n" +
        //        "           method-name\n" +
        //        "           arg-names\n" +
        //        "           arg-classes\n" +
        //        "           json)))");
    }
}