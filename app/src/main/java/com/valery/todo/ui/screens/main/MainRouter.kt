package com.valery.todo.ui.screens.main

interface MainRouter {
    fun goBack()

    fun openTodosList(putToBackStack: Boolean = false)

    fun openCreateTodo(putToBackStack: Boolean = false)

    fun openCreateSection(putToBackStack: Boolean = false)
}