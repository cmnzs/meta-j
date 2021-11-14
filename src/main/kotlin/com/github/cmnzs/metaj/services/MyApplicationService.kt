package com.github.cmnzs.metaj.services

import com.github.cmnzs.metaj.MyBundle
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project


@State(name = "idea-garden", storages = [Storage("idea-garden.xml")])
class MyApplicationService : PersistentStateComponent<MyApplicationService.State> {
    var myState: State = State("")

    companion object {
        fun getInstance(): MyApplicationService {
            return service()
        }
    }

    data class State(val myField: String)

    init {
        println(">> ${MyBundle.message("applicationService")} starting up")
    }

    override fun getState(): State {
        return myState
    }

    override fun loadState(state: State) {
        myState = state
    }
}