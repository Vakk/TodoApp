package com.valery.todo.ui.screens.create.section

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.os.Bundle
import android.view.View
import com.valery.todo.R
import com.valery.todo.ui.base.BaseFragment
import com.valery.todo.ui.screens.main.MainRouter
import kotlinx.android.synthetic.main.fragment_create_section.*

class CreateSectionFragment: BaseFragment<CreateSectionViewModel>(CreateSectionViewModel::class.java) {

    companion object {
        fun newInstance () = CreateSectionFragment()
    }

    override val layoutId: Int = R.layout.fragment_create_section
    private var router: MainRouter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnConfirm.setOnClickListener {
            viewModel?.createSection(etSectionName.text.toString())
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        router = context as MainRouter
    }

    override fun liveData(): List<MutableLiveData<*>> {
        return emptyList()
    }

    override fun onShowSuccess(data: Any?) {
        router?.goBack()
    }

}