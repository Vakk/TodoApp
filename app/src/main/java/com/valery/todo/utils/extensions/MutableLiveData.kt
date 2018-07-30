package com.valery.todo.utils.extensions

import android.arch.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.default(value: T): MutableLiveData<T> {
    postValue(value)
    return this
}