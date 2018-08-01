package com.valery.todo.ui.screens.create.section

import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import com.valery.todo.R
import com.valery.todo.ui.base.BaseFragment

class CreateSectionFragment: BaseFragment<CreateSectionViewModel>(CreateSectionViewModel::class.java) {
    override val layoutId: Int = R.layout.fragment_create_section

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun liveData(): List<MutableLiveData<*>> {
        return listOfNotNull(viewModel?.completeLiveData)
    }

}