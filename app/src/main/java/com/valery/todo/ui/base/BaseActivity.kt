package com.valery.todo.ui.base

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

abstract class BaseActivity<ViewModel: BaseViewModel>(private val viewModelClass: Class<ViewModel>? = null): AppCompatActivity() {
    private var TAG = javaClass.simpleName
    var viewModel: ViewModel? = null
        get() {
            if (field == null) {
                Log.d(TAG, "View model for type $viewModelClass is null...")
            }
            return field
        }

    abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        viewModelClass?.let {
            viewModel = ViewModelProviders.of(this).get(viewModelClass)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        for (data in liveData()) {
            data.removeObservers (this)
        }
    }


    open fun liveData (): List<MutableLiveData<*>> = emptyList()

    fun <Fragment: BaseFragment<*>> restoreOrCreate (contentId: Int, tag: String, createClosure: () -> Fragment) {
        val fragment = supportFragmentManager.findFragmentByTag(tag) ?: createClosure.invoke()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(contentId, fragment, tag)
        transaction.commit()
    }
}