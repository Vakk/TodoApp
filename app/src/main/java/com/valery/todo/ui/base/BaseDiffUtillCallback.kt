package com.valery.todo.ui.base

import android.support.v7.util.DiffUtil

abstract class BaseDiffUtillCallback<T>(var oldItems: List<T>, var newItems: List<T>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldItems[oldItemPosition] == newItems[newItemPosition]

    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size

}