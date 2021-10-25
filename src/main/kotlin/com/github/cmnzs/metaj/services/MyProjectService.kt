package com.github.cmnzs.metaj.services

import com.intellij.openapi.project.Project
import com.github.cmnzs.metaj.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
