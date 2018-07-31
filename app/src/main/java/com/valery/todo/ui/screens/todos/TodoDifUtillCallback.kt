package com.valery.todo.ui.screens.todos

import android.os.Bundle
import com.valery.todo.ui.base.BaseDiffUtillCallback
import com.valery.todo.ui.screens.todos.item.BaseTodoItemViewModel
import com.valery.todo.ui.screens.todos.item.EmptyItemViewModel
import com.valery.todo.ui.screens.todos.item.SectionTodoItemViewModel
import com.valery.todo.ui.screens.todos.item.TodoItemViewModel

class TodoDifUtillCallback : BaseDiffUtillCallback<BaseTodoItemViewModel>(mutableListOf(), mutableListOf()) {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]

        return when {
            newItem !is EmptyItemViewModel && oldItem !is EmptyItemViewModel -> {
                newItem.id == oldItem.id
            }
            else -> {
                newItem is EmptyItemViewModel && oldItem is EmptyItemViewModel && newItem.id == oldItem.id
            }
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]

        return when {
            oldItem is TodoItemViewModel && newItem is TodoItemViewModel -> {
                return oldItem.item.isDone != newItem.item.isDone && oldItem.item.title != newItem.item.title
            }
            oldItem is SectionTodoItemViewModel && newItem is SectionTodoItemViewModel -> {
                return oldItem.isExpanded != newItem.isExpanded && oldItem.section.title != newItem.section.title
            }
            else -> false
        }
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int) = Bundle().apply {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]

        when {
            oldItem is TodoItemViewModel && newItem is TodoItemViewModel -> {
                if (oldItem.item.isDone != newItem.item.isDone) {
                    putBoolean(TodoAdapterContract.EXTRA_IS_DONE, newItem.item.isDone)
                }

                if (oldItem.item.title != newItem.item.title) {
                    putString(TodoAdapterContract.EXTRA_TITLE, newItem.item.title)
                }
            }
            oldItem is SectionTodoItemViewModel && newItem is SectionTodoItemViewModel -> {
                if (oldItem.isExpanded != newItem.isExpanded) {
                    putBoolean(TodoAdapterContract.EXTRA_EXPANDED, newItem.isExpanded)
                }

                if (oldItem.section.title != newItem.section.title) {
                    putString(TodoAdapterContract.EXTRA_TITLE, newItem.section.title)
                }
            }
        }
    }
}