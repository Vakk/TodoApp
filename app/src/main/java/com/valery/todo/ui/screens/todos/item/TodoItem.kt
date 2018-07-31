package com.valery.todo.ui.screens.todos.item

data class TodoItem(override val id: Long, var isDone: Boolean, val title: String): BaseTodoItem(id)