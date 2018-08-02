package com.valery.todo.dagger.components

import com.valery.todo.dagger.SessionScope
import com.valery.todo.dagger.modules.DbModule
import com.valery.todo.dagger.modules.ManagerModule
import com.valery.todo.ui.screens.todos.TodosViewModel
import dagger.Subcomponent

@SessionScope
@Subcomponent(modules = [(DbModule::class), (ManagerModule::class)])
interface ViewModelComponent {
    fun inject(injector: TodosViewModel)
}