package org.intellij.sdk.toolwindow

import com.github.cmnzs.metaj.services.*
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBList
import com.intellij.ui.layout.panel
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridLayoutManager
import com.jetbrains.rd.swing.textProperty
import java.awt.Component
import javax.swing.*



// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
class MyToolWindowFactory : ToolWindowFactory, DumbAware {

    /**
     * Create the tool window content.
     *
     * @param project    current project
     * @param toolWindow current tool window
     */
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val content = toolWindow.contentManager.factory.createContent(IdeaGarden().createDialogPanel(), null, false)
        toolWindow.contentManager.addContent(content)
    }
}

val LOG = Logger.getInstance("MetaJUI")

class IdeaGarden {
    private val goalListModel = DefaultListModel<GardenItem>()
    private val actionListModel = DefaultListModel<GardenItemAction>()

    companion object {
        fun service(): MyApplicationService {
            return MyApplicationService.getInstance()
        }
    }
    init {

        if (service().myState.items.isEmpty()) {
            val list = IdeaGardenState.items
            goalListModel.addAll(list)
            updateState(list)
        } else {
            val list = service().myState.items
            goalListModel.addAll(list)
        }
    }

    private fun updateState(data: List<GardenItem> ) {
        service().myState = service().myState.copy(items = data)
    }

    fun createDialogPanel(): DialogPanel = panel {

        row("""Welcome to the IDEA Garden"""){}
        row{
            createMainPanel()()
        }
    }

    fun createMainPanel(): JComponent {

        val topPanel = JPanel(GridLayoutManager(1,2))

        val goalsPanel = JPanel(GridLayoutManager(4,1))
        val goalsLabel = JLabel("Goals")
        val addGoalButton = JButton("Add Goal")
        val goalEditor = JEditorPane()

        val goalList = JBList(goalListModel)

        val customRenderer = LabelRenderer()
        goalList.cellRenderer = customRenderer
        goalList.selectionMode = ListSelectionModel.SINGLE_SELECTION

        goalList.addListSelectionListener { e ->
            if (!e.valueIsAdjusting) {
                val gardenItem = service().myState.items.elementAt(goalList.selectedIndex)
                actionListModel.removeAllElements()
                actionListModel.addAll(gardenItem.actions)
            }
        }

        addGoalButton.addActionListener {
            val newItem = GardenItem(goalEditor.text)
            goalListModel.add(goalListModel.size, newItem)
            val l = goalListModel.elements().toList()
            updateState(l)
            goalEditor.text = "Enter new goal here..."
        }


        goalEditor.text = "Enter new goal here..."
        goalEditor.background = goalEditor.background.darker().darker()

        goalsPanel.add(goalsLabel, glh(0,0))
        goalsPanel.add(goalList, glh(1, 0))
        goalsPanel.add(goalEditor, glh(2, 0))
        goalsPanel.add(addGoalButton, glh(3, 0))

        // Actions panel
        val actionsPanel = JPanel(GridLayoutManager(4,1))
        val actionsLabel = JLabel("Actions")
        val addActionButton = JButton("Add Action")
        val actionEditor = JEditorPane()

        actionEditor.text = "Enter new action..."
        actionEditor.background = goalEditor.background.darker().darker()

        val actionList = JBList(actionListModel)
        actionList.cellRenderer = customRenderer

        addActionButton.addActionListener {
            val i = goalList.selectedIndex
            val gardenItem = goalListModel[i]
            val newAction = GardenItemAction(actionEditor.text, ActionStatus.NEW)
            val newActions = gardenItem.actions + newAction

            actionListModel.add(actionListModel.size, newAction)
            val newItem = gardenItem.copy(actions = newActions)
            goalListModel[i] = newItem
            val l = goalListModel.elements().toList()
            updateState(l)
            actionEditor.text = "Enter new action..."
        }

        actionsPanel.add(actionsLabel, glh(0, 0))
        actionsPanel.add(actionList, glh(1, 0))
        actionsPanel.add(actionEditor, glh(2, 0))
        actionsPanel.add(addActionButton, glh(3, 0))

        topPanel.add(goalsPanel, glh(0,0))
        topPanel.add(actionsPanel, glh(0, 1))
        return topPanel
    }
}

class LabelRenderer: DefaultListCellRenderer() {

    override fun getListCellRendererComponent(
        list: JList<*>?,
        value: Any?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        if (value is GardenItem) {
            super.getListCellRendererComponent(list, "[${value.status}] ${value.name}", index, isSelected, cellHasFocus)
        } else if (value is GardenItemAction) {
            super.getListCellRendererComponent(list, "[${value.status}] ${value.description}", index, isSelected, cellHasFocus)
        } else {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
        }

        if (index % 2 == 0) {
            this.background = this.background.darker()
        }
        return this
    }
}

fun gridLayoutHelper(row: Int, col: Int): GridConstraints {
    val x = GridConstraints()
    x.row = row
    x.column = col
    return x
}
fun glh(row: Int, col: Int): GridConstraints {
    return gridLayoutHelper(row, col)
}