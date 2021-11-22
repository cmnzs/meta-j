// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.intellij.sdk.toolwindow

import com.github.cmnzs.metaj.services.GardenItem
import com.github.cmnzs.metaj.services.IdeaGardenState
import com.github.cmnzs.metaj.services.MyApplicationService
import com.intellij.openapi.wm.ToolWindow
import java.awt.event.ActionEvent
import javax.swing.*


class MyToolWindow(toolWindow: ToolWindow) {

    lateinit var goalHeader: JLabel
    lateinit var planHeader: JLabel

    lateinit var myToolWindowContent: JPanel
    lateinit var goalContainer: JPanel
    lateinit var actionContainer: JPanel

    lateinit var actionEditor: JEditorPane
    lateinit var goalEditor: JEditorPane

    lateinit var addAction: JButton
    lateinit var addGoal: JButton
    lateinit var removeGoal: JButton
    lateinit var removeAction: JButton

    lateinit var gardenItems: JList<String>
    lateinit var actionList: JList<String>

    init {
        val listModel = DefaultListModel<String>()
        println("Initializing Idea Garden tool window component.")

        val myApplicationService = MyApplicationService.getInstance()
        val myState = myApplicationService.myState
        goalEditor.text = "Enter your goal"

        if (myState.items.isEmpty()) {
            val list = IdeaGardenState.items.map { it.name }
            myApplicationService.myState =
                myApplicationService.myState.copy(items = list.map { GardenItem(it) })
            listModel.addAll(list)
        } else {
            val list = myState.items.map { it.name }
            listModel.addAll(list)
        }


        gardenItems.model = listModel

        addAction.addActionListener { e: ActionEvent? ->
            val newItem = GardenItem(goalEditor.text)
            // add to list Model
            listModel.add(listModel.size(), newItem.name)
            // add to persisted state
            // should listModel instead have event listener that updates the applicationService state?
            myApplicationService.myState =
                myApplicationService.myState.copy(items = myApplicationService.myState.items + newItem)
        }

    }
}