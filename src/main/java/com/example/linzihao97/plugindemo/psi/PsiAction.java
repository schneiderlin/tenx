package com.example.linzihao97.plugindemo.psi;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

public class PsiAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        GlobalSearchScope scope = GlobalSearchScope.projectScope(project);

//        String filename = "R.java";
//        @NotNull PsiFile[] filesByName = FilenameIndex.getFilesByName(project, filename, scope);

        JavaPsiFacade instance = JavaPsiFacade.getInstance(project);
        PsiClass aClass = instance.findClass("repl.R", scope);

        System.out.println("action performed");
    }
}