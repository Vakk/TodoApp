package com.valery.todo

import android.app.Application
import com.valery.todo.dagger.DaggerManager
import com.valery.todo.dagger.components.DaggerAppComponent
import com.valery.todo.dagger.modules.DbModule
import com.valery.todo.dagger.modules.ManagerModule

class TodoApp: Application () {

    val daggerManger = DaggerManager()

    override fun onCreate() {
        super.onCreate()
        instance = this
        daggerManger.appComponent = DaggerAppComponent.builder().context(this).build()
        daggerManger.viewModelComponent = daggerManger.appComponent?.addViewModelComponent(DbModule(), ManagerModule())
    }

    companion object {
        lateinit var instance: TodoApp
    }
}