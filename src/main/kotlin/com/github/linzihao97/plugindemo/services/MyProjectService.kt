package com.github.linzihao97.plugindemo.services

import com.intellij.openapi.project.Project
import com.github.linzihao97.plugindemo.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
