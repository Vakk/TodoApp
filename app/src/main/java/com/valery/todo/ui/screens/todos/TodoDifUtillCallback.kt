package com.valery.todo.ui.screens.todos

import android.os.Bundle
import com.valery.todo.ui.base.BaseDiffUtillCallback
import com.valery.todo.ui.screens.todos.item.BaseTodoItem
import com.valery.todo.ui.screens.todos.item.SectionTodoItem
import com.valery.todo.ui.screens.todos.item.TodoItem

class TodoDifUtillCallback : BaseDiffUtillCallback<BaseTodoItem>(mutableListOf(), mutableListOf()) {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldItems[oldItemPosition].id == newItems[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]

        return when {
            oldItem is TodoItem && newItem is TodoItem -> {
                return oldItem.isDone != newItem.isDone && oldItem.title != newItem.title
            }
            oldItem is SectionTodoItem && newItem is SectionTodoItem -> {
                return oldItem.isExpanded != newItem.isExpanded && oldItem.title != newItem.title
            }
            else -> false
        }
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int) = Bundle().apply {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]

        when {
            oldItem is TodoItem && newItem is TodoItem -> {
                if (oldItem.isDone != newItem.isDone) {
                    putBoolean(TodoAdapterContract.EXTRA_IS_DONE, newItem.isDone)
                }

                if (oldItem.title != newItem.title) {
                    putString(TodoAdapterContract.EXTRA_TITLE, newItem.title)
                }
            }
            oldItem is SectionTodoItem && newItem is SectionTodoItem -> {
                if (oldItem.isExpanded != newItem.isExpanded) {
                    putBoolean(TodoAdapterContract.EXTRA_EXPANDED, newItem.isExpanded)
                }

                if (oldItem.title != newItem.title) {
                    putString(TodoAdapterContract.EXTRA_TITLE, newItem.title)
                }
            }
        }
    }
}