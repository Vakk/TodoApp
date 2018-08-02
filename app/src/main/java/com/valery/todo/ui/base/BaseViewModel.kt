package com.valery.todo.ui.base

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import com.valery.todo.TodoApp
import com.valery.todo.dagger.DaggerManager
import com.valery.todo.dagger.components.ViewModelComponent
import com.valery.todo.ui.DataStatus
import com.valery.todo.ui.screens.todos.TodosViewModel
import com.valery.todo.utils.extensions.default
import io.reactivex.disposables.CompositeDisposable


abstract class BaseViewModel : ViewModel(), com.valery.todo.ui.base.ViewModel {

    protected val disposableBag = CompositeDisposable()
    open val dataStatusLiveData = MutableLiveData<DataStatus>().default(DataStatus(DataStatus.TypeEnum.LOADING))


    override fun onCleared() {
        super.onCleared()
        disposableBag.clear()
    }
}