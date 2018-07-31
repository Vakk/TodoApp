package com.valery.todo.ui.screens.todos

import android.arch.lifecycle.MutableLiveData
import com.valery.todo.model.db.Todo
import com.valery.todo.model.db.TodoSection
import com.valery.todo.ui.base.BaseViewModel
import com.valery.todo.ui.screens.todos.item.BaseTodoItemViewModel
import com.valery.todo.ui.screens.todos.item.SectionTodoItemViewModel
import com.valery.todo.ui.screens.todos.item.TodoItemViewModel
import com.valery.todo.utils.extensions.default
import java.util.*

class TodosViewModel : BaseViewModel() {
    val itemsLiveData: MutableLiveData<MutableList<BaseTodoItemViewModel>> = MutableLiveData<MutableList<BaseTodoItemViewModel>>().default(mutableListOf())

    var sections: MutableList<SectionTodoItemViewModel> = mutableListOf()
    var itemViewModels: MutableList<TodoItemViewModel> = mutableListOf()

    private var lastValue: Int = 0

    init {
        sections.add(SectionTodoItemViewModel(TodoSection(lastValue++.toLong(), "Shopping", 0), true))
        sections.add(SectionTodoItemViewModel(TodoSection(lastValue++.toLong(), "Work", 1), true))
        loadToView()
    }

    fun addValue(done: Boolean, title: String) {
        itemViewModels.add(TodoItemViewModel(Todo(lastValue++.toLong(), title, done, Random().nextInt(2))))
        loadToView()
    }

    fun removeValue(todoItemViewModel: TodoItemViewModel) {
        itemViewModels.remove(todoItemViewModel)
        loadToView()
    }

    fun changeStatus(todoItemViewModel: TodoItemViewModel) {
        itemViewModels.firstOrNull { it.id == todoItemViewModel.id }?.item?.isDone = !todoItemViewModel.item.isDone
        loadToView()
    }

    fun addValue() {
        addValue(false, "Check box #$lastValue")
    }

    fun expandSection(section: SectionTodoItemViewModel) {
        sections.firstOrNull { it.id == section.id }?.isExpanded = !section.isExpanded
        loadToView()
    }

    private fun loadToView() {
        val dataToView = mutableListOf<BaseTodoItemViewModel>()
        for (section in sections) {
            dataToView.add(section.copy(section = section.section.copy()))
            if (section.isExpanded) {
                val todosForSection = itemViewModels
                        .filter { it.item.sectionType == section.section.type }
                        .map { it.copy(item = it.item.copy()) }
                        .sortedBy { it.id }
                        .sortedBy { it.item.isDone }
                dataToView.addAll(todosForSection)
            }
        }
        itemsLiveData.postValue(dataToView)
    }
}