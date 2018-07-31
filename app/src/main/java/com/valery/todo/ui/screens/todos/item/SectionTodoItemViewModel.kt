package com.valery.todo.ui.screens.todos.item

import com.valery.todo.model.db.TodoSection

data class SectionTodoItemViewModel(
        val section: TodoSection,
        var isExpanded: Boolean,
        var allCount: Int = 0,
        var doneCount: Int = 0): BaseTodoItemViewModel(section.id)