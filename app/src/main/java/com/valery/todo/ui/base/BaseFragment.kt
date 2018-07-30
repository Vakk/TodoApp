package com.valery.todo.ui.base

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseFragment<ViewModel: BaseViewModel>(private val viewModelClass: Class<ViewModel>): Fragment() {
    private var TAG = javaClass.simpleName
    var viewModel: ViewModel? = null
    get() {
        if (field == null) {
            Log.d(TAG, "View model for type $viewModelClass is null...")
        }
        return field
    }

    abstract val layoutId: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layoutId, container, false)
        viewModel = ViewModelProviders.of(this).get(viewModelClass)
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        for (data in liveData()) {
            data.removeObservers (this)
        }
    }


    abstract fun liveData (): List<MutableLiveData<*>>
}