package com.example.linzihao97.plugindemo.psi;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

public class PsiAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        GlobalSearchScope scope = GlobalSearchScope.projectScope(project);

//        String filename = "R.java";
//        @NotNull PsiFile[] filesByName = FilenameIndex.getFilesByName(project, filename, scope);

        ToolWindowManager toolWindowManager;
        //toolWindowManager.getActiveToolWindowId()
        //toolWindowManager.notifyByBalloon();
        ActionManager actionManager = ActionManager.getInstance();
        Application application = ApplicationManager.getApplication();
        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
        PsiClass aClass = javaPsiFacade.findClass("repl.R", scope);
        PsiDocumentManager psiDocumentManager = PsiDocumentManager.getInstance(project);
        FileDocumentManager fileDocumentManager = FileDocumentManager.getInstance();
        LocalFileSystem localFileSystem = LocalFileSystem.getInstance();
        VirtualFile virtualFile = localFileSystem.findFileByIoFile(null);
        PsiManager psiManager;
        //psiManager.findFile();
        Document document = fileDocumentManager.getDocument(virtualFile);
        document.insertString(0, "asd");
        WriteAction.run(() -> System.out.println(1));
        System.out.println("action performed");
    }
}