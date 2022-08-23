package com.example.linzihao97.plugindemo.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.ProjectLevelVcsManager;
import com.intellij.openapi.vcs.VcsActions;
import com.intellij.openapi.vcs.annotate.FileAnnotation;
import com.intellij.openapi.vcs.annotate.LineAnnotationAspect;
import com.intellij.openapi.vcs.history.VcsRevisionDescription;
import git4idea.GitUtil;
import git4idea.annotate.GitFileAnnotation;
import git4idea.repo.GitRepositoryFiles;
import git4idea.repo.GitRepositoryManager;
import org.apache.batik.gvt.flow.LineInfo;
import org.jetbrains.annotations.NotNull;

public class BasicAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        System.out.println("action performed");
        Project project = e.getProject();

        ProjectLevelVcsManager manager;
        FileAnnotation fileAnnotation;
        LineAnnotationAspect lineAnnotationAspect;
        VcsRevisionDescription vcsRevisionDescription;

        // git4idea 包里面有些能用的
        //GitFileAnnotation;
        //LineInfo
        GitUtil;
        GitRepositoryManager;
        GitRepositoryFiles;
        GitFileAnnotation;

        System.out.println("a");
    }
}
