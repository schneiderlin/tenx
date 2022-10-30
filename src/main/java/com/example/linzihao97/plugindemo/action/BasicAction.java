package com.example.linzihao97.plugindemo.action;

import com.example.linzihao97.plugindemo.tenx.ReplClientService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class BasicAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        System.out.println("action performed");
        Project project = e.getProject();

        ReplClientService service = project.getService(ReplClientService.class);
        String key1 = service.getCache("key1");
        System.out.println(1);

        //refMethod.getInReferences()

        //CallHierarchyBrowser browser = new CallHierarchyBrowser(project, null);
        //browser.addHierarchyListener();

        //ProjectLevelVcsManager manager;
        //FileAnnotation fileAnnotation;
        //LineAnnotationAspect lineAnnotationAspect;
        //VcsRevisionDescription vcsRevisionDescription;

        // git4idea 包里面有些能用的
        //GitFileAnnotation;
        //LineInfo
        //GitUtil;
        //GitRepositoryManager;
        //GitRepositoryFiles;
        //GitFileAnnotation;
    }
}
