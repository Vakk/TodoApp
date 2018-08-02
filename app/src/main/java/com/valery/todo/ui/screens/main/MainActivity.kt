package com.valery.todo.ui.screens.main

import android.os.Bundle
import android.support.v4.app.Fragment
import com.valery.todo.R
import com.valery.todo.ui.base.BaseActivity
import com.valery.todo.ui.base.BaseViewModel
import com.valery.todo.ui.screens.create.section.CreateSectionFragment
import com.valery.todo.ui.screens.todos.TodosFragment


class MainActivity : BaseActivity<BaseViewModel>(), MainRouter {

    override val layoutId: Int = R.layout.activity_main

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        restoreOrCreate(R.id.frameLayout, TodosFragment::class.java.simpleName) { TodosFragment.newInstance() }
    }

    override fun openTodosList(putToBackStack: Boolean) {
        openFragment(TodosFragment.newInstance(), TodosFragment::class.java.simpleName)
    }

    override fun openCreateTodo(putToBackStack: Boolean) {
        openFragment(TodosFragment.newInstance(), TodosFragment::class.java.simpleName)
    }

    override fun goBack() {
        onBackPressed()
    }

    override fun openCreateSection(putToBackStack: Boolean) {
        openFragment(CreateSectionFragment.newInstance(), CreateSectionFragment::class.java.simpleName)
    }

    private fun openFragment(fragment: Fragment, tag: String, backStackAllowed: Boolean = true) {
        val fragment = supportFragmentManager.findFragmentByTag(tag) ?: fragment
        val transaction = supportFragmentManager.beginTransaction()
        if (backStackAllowed) {
            transaction.addToBackStack(tag)
        }
        transaction.replace(R.id.frameLayout, fragment, tag)
        transaction.commit()
    }

}
