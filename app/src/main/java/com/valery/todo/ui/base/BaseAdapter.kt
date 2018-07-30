package com.valery.todo.ui.base

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView

abstract class BaseAdapter<VH: RecyclerView.ViewHolder, Item>: RecyclerView.Adapter<VH>() {
    private val items = mutableListOf<Item>()

    override fun getItemCount() = items.size

    fun updateListItems(newItems: List<Item>, callback: BaseDiffUtillCallback<Item>) {
        callback.oldItems = items
        callback.newItems = newItems
        DiffUtil.calculateDiff(callback).dispatchUpdatesTo(this)
        items.clear()
        items.addAll(newItems)
    }

    fun getItem (position: Int) = items[position]
}