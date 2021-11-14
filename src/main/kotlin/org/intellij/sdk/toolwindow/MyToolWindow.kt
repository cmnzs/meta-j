// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.intellij.sdk.toolwindow

import com.github.cmnzs.metaj.services.MyApplicationService
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import java.awt.event.ActionEvent
import javax.swing.*


data class GardenItem(
    val name: String,
    val description: String,
    val status: GardenItemStatus,
    val actions: Collection<GardenItemAction>
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

class MyToolWindow(toolWindow: ToolWindow) {

    lateinit var myToolWindowContent: JPanel
    lateinit var editorPane1: JEditorPane
    lateinit var submitButton: JButton
    lateinit var gardenItems: JList<String>

    init {
        val listModel = DefaultListModel<String>()
        listModel.addAll(IdeaGardenState.items.map { it.name })
        gardenItems.model = listModel

        println("Hello world")
        val myApplicationService = MyApplicationService.getInstance()

        var myState = myApplicationService.myState

        editorPane1.text = if (myState.myField.isNotBlank()) myState.myField else "please enter a value for the prompt"

//        submitButton!!.addActionListener { e: ActionEvent? -> listModel.add(listModel.size(), editorPane1.text) }
        submitButton!!.addActionListener { e: ActionEvent? -> myApplicationService.myState = MyApplicationService.State(editorPane1.text) }
    }
}