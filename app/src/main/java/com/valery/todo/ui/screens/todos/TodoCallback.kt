package com.valery.todo.ui.screens.todos

import com.valery.todo.ui.screens.todos.item.SectionTodoItemViewModel
import com.valery.todo.ui.screens.todos.item.TodoItemViewModel

interface TodoCallback {
    fun setDone (todoItemViewModel: TodoItemViewModel)

    fun switchExpandState (section: SectionTodoItemViewModel)

    fun addTodo (section: SectionTodoItemViewModel)

    fun removeTodo (todo: TodoItemViewModel)

    fun removeSection (seciton: SectionTodoItemViewModel)
}