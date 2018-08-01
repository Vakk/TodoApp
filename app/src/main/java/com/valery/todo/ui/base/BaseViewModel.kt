package com.valery.todo.ui.base

import android.arch.lifecycle.ViewModel
import android.content.Context
import io.reactivex.disposables.CompositeDisposable


abstract class BaseViewModel : ViewModel(), com.valery.todo.ui.base.ViewModel {

    protected val disposableBag = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposableBag.clear()
    }
}