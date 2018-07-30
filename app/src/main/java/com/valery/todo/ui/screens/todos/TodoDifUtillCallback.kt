package com.valery.todo.ui.screens.todos

import android.os.Bundle
import com.valery.todo.ui.base.BaseDiffUtillCallback

class TodoDifUtillCallback : BaseDiffUtillCallback<TodoItemViewModel>(mutableListOf(), mutableListOf()) {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldItems[oldItemPosition].id == newItems[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]

        return oldItem.isDone != newItem.isDone && oldItem.title != newItem.title
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int) = Bundle().apply {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]

        if (oldItem.isDone != newItem.isDone) {
            putBoolean(TodoAdapterContract.checked, newItem.isDone)
        }

        if (oldItem.title != newItem.title) {
            putString(TodoAdapterContract.title, newItem.title)
        }
    }
}