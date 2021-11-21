// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.intellij.sdk.toolwindow

import com.github.cmnzs.metaj.services.GardenItem
import com.github.cmnzs.metaj.services.IdeaGardenState
import com.github.cmnzs.metaj.services.MyApplicationService
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import java.awt.event.ActionEvent
import javax.swing.*


class MyToolWindow(toolWindow: ToolWindow) {

    lateinit var myToolWindowContent: JPanel
    lateinit var editorPane1: JEditorPane
    lateinit var submitButton: JButton
    lateinit var gardenItems: JList<String>

    init {
        val listModel = DefaultListModel<String>()
        println("Initializing Idea Garden tool window component.")

        val myApplicationService = MyApplicationService.getInstance()
        var myState = myApplicationService.myState
        editorPane1.text = "Enter your goal"

        listModel.addAll(if (myState.items.isEmpty()) {
            IdeaGardenState.items.map { it.name }
        } else {
            myState.items.map { it.name }
        })

        gardenItems.model = listModel

        submitButton!!.addActionListener { e: ActionEvent? ->
            val newItem = GardenItem(editorPane1.text)
            listModel.add(listModel.size(), newItem.name)
            myApplicationService.myState = myApplicationService.myState.copy(myApplicationService.myState.items + newItem)
        }

    }
}