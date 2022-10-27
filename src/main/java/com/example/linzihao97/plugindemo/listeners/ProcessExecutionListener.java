//package com.example.linzihao97.plugindemo.listeners;
//
//import com.example.linzihao97.plugindemo.settings.AayDoorSettingsState;
//import com.example.linzihao97.plugindemo.tenx.MyNotifier;
//import com.intellij.execution.ExecutionListener;
//import com.intellij.execution.application.ApplicationConfiguration;
//import com.intellij.execution.runners.ExecutionEnvironment;
//import com.intellij.openapi.module.Module;
//import com.intellij.openapi.project.Project;
//import com.intellij.openapi.roots.ModuleRootManager;
//import com.intellij.openapi.roots.ModuleRootModificationUtil;
//import org.apache.commons.io.FileUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.idea.maven.project.MavenProjectsManager;
//
//import java.io.File;
//import java.net.URL;
//import java.util.List;
//import java.util.Optional;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//public class ProcessExecutionListener implements ExecutionListener {
//
//    private Project project;
//
//    public ProcessExecutionListener(@NotNull Project project) {
//        this.project = project;
//    }
//
//    @Override
//    public void processStartScheduled(@NotNull String executorId, @NotNull ExecutionEnvironment env) {
//        try {
//            fillJar(env);
//        } catch (Exception e) {
//            MyNotifier.notifyError(project, "fill ModuleLibrary fail: " + e.getMessage());
//        }
//        ExecutionListener.super.processStartScheduled(executorId, env);
//    }
//
//    private void fillJar(@NotNull ExecutionEnvironment env) throws Exception {
//        Object moduleObj = Optional.ofNullable(env.getDataContext()).map(o -> o.getData("module")).orElseGet(() -> {
//            return ((ApplicationConfiguration) env.getRunProfile()).getModules()[0];
//        });
//        boolean b = moduleObj instanceof Module;
//        if (!b) {
//            throw new RuntimeException("get run module error");
//        }
//
//        Module module = (Module) moduleObj;
//        String jarName = "any-door";
//        AtomicBoolean alreadyHave = new AtomicBoolean(false);
//
//        String version = project.getService(AayDoorSettingsState.class).version;
//
//        ModuleRootManager.getInstance(module).orderEntries().forEachLibrary(library -> {
//            if (!alreadyHave.get() && StringUtils.contains(library.getName(), jarName)) {
//                alreadyHave.set(true);
//            }
//            return true;
//        });
//        if (alreadyHave.get()) {
//            MyNotifier.notifyInfo(project, "ModuleLibrary already exists");
//            return;
//        }
//
//
//        File localRepository = MavenProjectsManager.getInstance(project).getLocalRepository();
//        String jarPath = "/io/github/lgp547/any-door/" + version + "/any-door-" + version + ".jar";
//        String localPath = localRepository.getPath() + jarPath;
//        File file = new File(localPath);
//        if (!file.isFile()) {
//            String httpPath = "https://s01.oss.sonatype.org/content/repositories/releases" + jarPath;
//            FileUtils.copyURLToFile(new URL(httpPath), file);
//        }
//        if (file.isFile()) {
//            ModuleRootModificationUtil.addModuleLibrary(module, jarName + version, List.of("file://" + file.getPath()), List.of());
//            MyNotifier.notifyInfo(project, "fill ModuleLibrary success " + file.getPath());
//        }
//    }
//
//}
