package com.valery.todo.ui.screens.todos

import com.valery.todo.ui.screens.todos.item.SectionTodoItem
import com.valery.todo.ui.screens.todos.item.TodoItem

interface TodoCallback {
    fun setDone (todoItem: TodoItem)

    fun switchExpandState (section: SectionTodoItem)
}