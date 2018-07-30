package com.valery.todo.ui.screens.todos

import android.arch.lifecycle.MutableLiveData
import com.valery.todo.ui.base.BaseViewModel
import com.valery.todo.utils.extensions.default
import io.reactivex.Observable
import java.util.*
import java.util.concurrent.TimeUnit

class TodosViewModel : BaseViewModel() {
    val itemsLiveData: MutableLiveData<MutableList<TodoItemViewModel>> = MutableLiveData<MutableList<TodoItemViewModel>>().default(mutableListOf())
    private var lastValue: Int = 0

    init {
        Observable.interval(10000, 1000, TimeUnit.MILLISECONDS)
                .subscribe{
                    synchronized(itemsLiveData) {
                        itemsLiveData.value?.let { items ->
                            val size = items.size
                            if (size >= 0) {
                                val random = Random()
                                if (random.nextBoolean()) {
                                    replaceRandomItems(random, items, size)
                                } else {
                                    removeRandomItem(random, items, size)
                                }
                            }
                        }
                    }
                }
    }

    fun addValue(checked: Boolean, title: String) {
        itemsLiveData.postValue(itemsLiveData.value?.apply { add(TodoItemViewModel(lastValue++.toLong(), checked, title)) })
    }

    fun removeValue(todoItemViewModel: TodoItemViewModel) {
        itemsLiveData.postValue(itemsLiveData.value?.apply { remove(todoItemViewModel) })
    }

    fun addValue() {
        addValue(true, "Check box #$lastValue")
    }

    private fun replaceRandomItems (random: Random, items: MutableList<TodoItemViewModel>, size: Int) {
        if (size == 0) {
            return
        }
        val firstPosition = random.nextInt(size)
        val secondPosition = random.nextInt(size)
        if (firstPosition != secondPosition) {
            val firstValue = items[firstPosition]
            val secondValue = items[secondPosition]
            items[firstPosition] = secondValue
            items[secondPosition] = firstValue
            itemsLiveData.postValue(items)
        }
    }

    private fun removeRandomItem (random: Random, items: MutableList<TodoItemViewModel>, size: Int) {
        if (size == 0) {
            return
        }
        items.removeAt(random.nextInt(size))
        itemsLiveData.postValue(items)
    }
}