package org.intellij.sdk.toolwindow

import com.intellij.openapi.diagnostic.Logger
import com.github.cmnzs.metaj.services.GardenItem
import com.github.cmnzs.metaj.services.IdeaGardenState
import com.github.cmnzs.metaj.services.MyApplicationService
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBList
import com.intellij.ui.layout.panel
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridLayoutManager
import org.intellij.lang.annotations.JdkConstants
import java.awt.Color
import java.awt.Component
import java.awt.FlowLayout
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
        val myToolWindow = MyToolWindow(toolWindow)
//        val contentFactory = ContentFactory.SERVICE.getInstance()
//        val content = contentFactory.createContent(myToolWindow.myToolWindowContent, "", false)
//        val content = contentFactory.createContent(createDialogPanel(), "Hello World", false)
        val content = toolWindow.contentManager.factory.createContent(IdeaGarden().createDialogPanel(), null, false)
        toolWindow.contentManager.addContent(content)
    }
}

val LOG = Logger.getInstance("MetaJUI")

class IdeaGarden {
    val listModel = DefaultListModel<String>()

    companion object {
        fun service(): MyApplicationService {
            return MyApplicationService.getInstance()
        }
    }
    init {

        if (service().myState.items.isEmpty()) {
            val list = IdeaGardenState.items.map { it.name }
            listModel.addAll(list)
            updateState(list)
        } else {
            val list = service().myState.items.map { it.name }
            listModel.addAll(list)
        }
    }

    fun updateState(data: List<String> ) {
        service().myState = service().myState.copy(items = data.map { GardenItem(it) })

    }

    fun createDialogPanel(): DialogPanel = panel {

        row("""Welcome to the IDEA Garden"""){}
        row{
            createMainPanel(listModel)()
        }
    }

    fun createMainPanel(goalListModel: DefaultListModel<String>): JComponent {

        val topPanel = JPanel(GridLayoutManager(1,2))

        val goalsPanel = JPanel(GridLayoutManager(4,1))


        val goalsLabel = JLabel("Goals")
        val addGoalButton = JButton("Add Goal")
        val goalEditor = JEditorPane()

        val goalList = JBList(goalListModel)
        val customRenderer = LabelRenderer()
        goalList.cellRenderer = customRenderer

        addGoalButton.addActionListener {
            val newItem = GardenItem(goalEditor.text)
            listModel.add(listModel.size, newItem.name)
            val l = listModel.elements().toList()
            updateState(l)
        }

        goalsPanel.add(goalsLabel, glh(0,0))
        goalsPanel.add(goalList, glh(1, 0))
        goalsPanel.add(addGoalButton, glh(2, 0))

        val actionsPanel = JPanel(GridLayoutManager(2,1))
        val actionsLabel = JLabel("Actions")
        val addActionButton = JButton("Add Action")
        addActionButton.addActionListener { LOG.info("Action event listen : $it") }

        actionsPanel.add(actionsLabel, glh(0, 0))

        actionsPanel.add(addActionButton, glh(1, 0))

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
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)

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
    x.vSizePolicy = GridConstraints.SIZEPOLICY_WANT_GROW
    x.hSizePolicy = GridConstraints.SIZEPOLICY_WANT_GROW
    return x
}
fun glh(row: Int, col: Int): GridConstraints {
    return gridLayoutHelper(row, col)
}