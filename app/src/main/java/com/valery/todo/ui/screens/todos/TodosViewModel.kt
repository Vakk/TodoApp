package com.valery.todo.ui.screens.todos

import android.arch.lifecycle.MutableLiveData
import com.valery.todo.ui.base.BaseViewModel
import com.valery.todo.ui.screens.todos.item.BaseTodoItem
import com.valery.todo.ui.screens.todos.item.SectionTodoItem
import com.valery.todo.ui.screens.todos.item.TodoItem
import com.valery.todo.utils.extensions.default

class TodosViewModel : BaseViewModel() {
    val itemsLiveData: MutableLiveData<MutableList<BaseTodoItem>> = MutableLiveData<MutableList<BaseTodoItem>>().default(mutableListOf())

    var sections: MutableList<SectionTodoItem> = mutableListOf()
    var items: MutableList<TodoItem> = mutableListOf()

    private var lastValue: Int = 0

    init {
        sections.add(SectionTodoItem(lastValue++.toLong(), "Done", true) { it.isDone })
        sections.add(SectionTodoItem(lastValue++.toLong(), "Undone", true) { !it.isDone })
    }

    fun addValue(checked: Boolean, title: String) {
        items.add(TodoItem(lastValue++.toLong(), checked, title))
        loadToView()
    }

    fun removeValue(todoItem: TodoItem) {
        items.remove(todoItem)
        loadToView()
    }

    fun changeStatus(todoItem: TodoItem) {
        todoItem.isDone = !todoItem.isDone
        loadToView()
    }

    fun addValue() {
        addValue(true, "Check box #$lastValue")
    }

    fun expandSection(section: SectionTodoItem) {
        sections.firstOrNull { it.id == section.id }?.isExpanded = !section.isExpanded
        loadToView()
    }

    private fun loadToView() {
        val dataToView = mutableListOf<BaseTodoItem>()
        for (section in sections) {
            dataToView.add(section.copy())
            if (section.isExpanded) {
                for (todo in items) {
                    if (section.validator.invoke(todo)) {
                        dataToView.add(todo)
                    }
                }
            }
        }
        itemsLiveData.postValue(dataToView)
    }
}