package com.valery.todo.utils.extensions

import io.reactivex.Observable
import io.reactivex.Single

fun <T> Observable<T>.toMutableList (): Single<MutableList<T>> = toList().map { it.toMutableList() }