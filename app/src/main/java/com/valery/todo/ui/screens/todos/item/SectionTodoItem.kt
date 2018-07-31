package com.valery.todo.ui.screens.todos.item

data class SectionTodoItem(override val id: Long, val title: String, var isExpanded: Boolean, val validator: (todoItem: TodoItem) -> Boolean): BaseTodoItem(id)