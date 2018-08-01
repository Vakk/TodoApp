package com.valery.todo.utils.extensions

import android.content.Context
import com.valery.todo.TodoApp
import com.valery.todo.model.dagger.DaggerManger

fun Context.getAppInstance(): TodoApp {
    return applicationContext as TodoApp
}