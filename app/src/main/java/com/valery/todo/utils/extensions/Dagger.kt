package com.valery.todo.utils.extensions

import android.content.Context
import com.valery.todo.TodoApp

fun Context.getAppInstance(): TodoApp {
    return applicationContext as TodoApp
}