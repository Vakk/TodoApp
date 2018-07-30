package com.valery.todo.ui.screens.main

import android.os.Bundle
import com.valery.todo.R
import com.valery.todo.ui.base.BaseActivity
import com.valery.todo.ui.base.BaseViewModel
import com.valery.todo.ui.screens.todos.TodosFragment


class MainActivity : BaseActivity<BaseViewModel>() {

    override val layoutId: Int = R.layout.activity_main

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        restoreOrCreate(R.id.frameLayout, TodosFragment::class.java.simpleName) { TodosFragment() }
    }
}
