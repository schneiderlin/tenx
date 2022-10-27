package com.example.linzihao97.plugindemo.anydoor;

import com.example.linzihao97.plugindemo.settings.AayDoorSettingsState;
import com.example.linzihao97.plugindemo.tenx.MyNotifier;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.impl.ModuleImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ModuleRootModificationUtil;
import com.intellij.openapi.util.Key;
import com.intellij.util.keyFMap.KeyFMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.idea.maven.project.MavenProjectsManager;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class AnyDoorImportUtil {

    public static void fillJar(Project project, String runModuleName) {
        try {
            Module module = getMainModule(project, runModuleName);
            String jarName = "any-door";
            AtomicBoolean alreadyHave = new AtomicBoolean(false);

            String version = project.getService(AayDoorSettingsState.class).version;

            ModuleRootManager.getInstance(module).orderEntries().forEachLibrary(library -> {
                if (!alreadyHave.get() && StringUtils.contains(library.getName(), jarName)) {
                    alreadyHave.set(true);
                }
                return true;
            });
            if (alreadyHave.get()) {
                MyNotifier.notifyInfo(project, module.getName() + " ModuleLibrary already exists");
                return;
            }


            File localRepository = MavenProjectsManager.getInstance(project).getLocalRepository();
            String jarPath = "/io/github/lgp547/any-door/" + version + "/any-door-" + version + ".jar";
            String localPath = localRepository.getPath() + jarPath;
            File file = new File(localPath);
            if (!file.isFile()) {
                String httpPath = "https://s01.oss.sonatype.org/content/repositories/releases" + jarPath;
                FileUtils.copyURLToFile(new URL(httpPath), file);
                if (!file.isFile()) {
                    throw new RuntimeException("get jar file fail");
                }
            }
            ModuleRootModificationUtil.addModuleLibrary(module, jarName + "-" + version, List.of("file://" + file.getPath()), List.of());
            MyNotifier.notifyInfo(project, module.getName() + " fill ModuleLibrary success " + file.getPath());
        } catch (Exception e) {
            MyNotifier.notifyError(project, "fill ModuleLibrary fail: " + e.getMessage());
        }

    }

    @SuppressWarnings("rawtypes")
    public static Module getMainModule(Project project, String runModuleName) {
        Module[] modules = ModuleManager.getInstance(project).getModules();
        if (StringUtils.isNotBlank(runModuleName)) {
            for (Module module : modules) {
                if (module.getName().equals(runModuleName)) {
                    return module;
                }
            }
        }
        List<Module> modulesList = new ArrayList<>();
        for (Module module : modules) {
            if (module instanceof ModuleImpl) {
                KeyFMap keyFMap = ((ModuleImpl) module).get();
                if (null != keyFMap) {
                    Key[] keys = keyFMap.getKeys();
                    for (Key key : keys) {
                        if (key.toString().contains("org.jetbrains.idea.maven.project.MavenProjectsManager")) {
                            modulesList.add(module);
                            break;
                        }
                    }
                }
            }
        }
        if (modulesList.size() == 1) {
            return modulesList.get(0);
        }
        throw new RuntimeException("MainModule could not find. size " + modulesList.size());
    }
}
