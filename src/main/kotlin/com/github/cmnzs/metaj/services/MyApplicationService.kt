package com.github.cmnzs.metaj.services

import com.github.cmnzs.metaj.MyBundle
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project

data class GardenItem(
    val name: String,
    val description: String = "",
    val status: GardenItemStatus = GardenItemStatus.NEW,
    val actions: Collection<GardenItemAction> = emptyList()
)

data class GardenItemAction(
    val description: String,
    val status: ActionStatus
)

enum class ActionStatus {
    NEW,
    PARTIAL,
    DONE
}

enum class GardenItemStatus {
    NEW,
    IN_PROGRESS,
    COMPLETE
}

object IdeaGardenState {
    val items = listOf(
        GardenItem(
            "Learn Kotlin Coroutines",
            "",
            GardenItemStatus.NEW,
            listOf(
                GardenItemAction("Prototype parallel file reader", ActionStatus.PARTIAL)
            )
        ),
        GardenItem(
            "Understand Postgres indexing",
            "",
            GardenItemStatus.NEW,
            listOf(
                GardenItemAction("Prototype parallel file reader", ActionStatus.PARTIAL)
            )
        ),
        GardenItem(
            "Learn kafka metadata topics",
            "",
            GardenItemStatus.NEW,
            listOf(
                GardenItemAction("Prototype parallel file reader", ActionStatus.PARTIAL)
            )
        )
    )
}


@State(name = "idea-garden", storages = [Storage("idea-garden.xml")])
class MyApplicationService : PersistentStateComponent<MyApplicationService.State> {
    var myState: State = State(emptyList())

    companion object {
        fun getInstance(): MyApplicationService {
            return service()
        }
    }

    data class State(val items: Collection<GardenItem>)

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