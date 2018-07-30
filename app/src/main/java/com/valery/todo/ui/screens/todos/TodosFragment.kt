package com.valery.todo.ui.screens.todos

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.View
import com.valery.todo.R
import com.valery.todo.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_todos.*

class TodosFragment : BaseFragment<TodosViewModel>(TodosViewModel::class.java) {

    override val layoutId: Int = R.layout.fragment_todos

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel?.items?.observe(this, Observer { item ->
            textView.text = item
        })
    }

    override fun liveData(): List<MutableLiveData<*>> {
        return listOfNotNull(viewModel?.items)
    }
}