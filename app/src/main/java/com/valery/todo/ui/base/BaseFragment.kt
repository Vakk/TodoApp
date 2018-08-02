package com.valery.todo.ui.base

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.valery.todo.ui.DataStatus
import com.valery.todo.utils.safeLet

abstract class BaseFragment<ViewModel: BaseViewModel>(private val viewModelClass: Class<ViewModel>): Fragment() {
    private var TAG = javaClass.simpleName
    var viewModel: ViewModel? = null
    get() {
        if (field == null) {
            Log.d(TAG, "View model for value $viewModelClass is null...")
        }
        return field
    }

    abstract val layoutId: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layoutId, container, false)
        viewModel = ViewModelProviders.of(this).get(viewModelClass)
        viewModel?.dataStatusLiveData?.observe(this, Observer {
            it?.let {
                when (it.status) {
                    DataStatus.TypeEnum.LOADING -> onShowProgress(it.data)
                    DataStatus.TypeEnum.ERROR -> onShowError(it.data)
                    DataStatus.TypeEnum.SUCCESS -> onShowSuccess(it.data)
                }
            }
        })
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        for (data in liveData()) {
            data.removeObservers (this)
        }
        viewModel?.dataStatusLiveData?.removeObservers (this)
    }

    protected open fun onShowProgress (data: Any?) {

    }

    protected open fun onShowSuccess (data: Any?) {

    }

    protected open fun onShowError (data: Any?) {
        safeLet(view, data) { view, data ->
            Snackbar.make(view, data.toString(), Snackbar.LENGTH_SHORT).show()
        }
    }


    abstract fun liveData (): List<MutableLiveData<*>>
}