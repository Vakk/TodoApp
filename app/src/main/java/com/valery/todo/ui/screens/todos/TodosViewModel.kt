package com.valery.todo.ui.screens.todos

import android.arch.lifecycle.MutableLiveData
import com.valery.todo.ui.base.BaseViewModel
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class TodosViewModel: BaseViewModel() {
    val items: MutableLiveData<String> = MutableLiveData()

    var lastValue: Int = 0
    init {
        Observable.interval(100, 3000, TimeUnit.MILLISECONDS)
                .subscribe {updateValue()}
    }

    private fun updateValue () {
        items.postValue(lastValue++.toString())
    }
}