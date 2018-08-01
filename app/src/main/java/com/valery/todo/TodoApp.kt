package com.valery.todo

import android.app.Application

class TodoApp: Application () {

    override fun onCreate() {
        super.onCreate()
        instance = this

    }

    companion object {
        lateinit var instance: TodoApp
    }
}