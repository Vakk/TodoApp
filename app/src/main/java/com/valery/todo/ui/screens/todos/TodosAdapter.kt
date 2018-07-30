package com.valery.todo.ui.screens.todos

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.valery.todo.R
import com.valery.todo.ui.base.BaseAdapter

class TodosAdapter : BaseAdapter<TodosAdapter.TodoViewHolder, TodoItemViewModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            (payloads[0] as? Bundle)?.apply {
                if (containsKey(TodoAdapterContract.title)) {
                    holder.bindTitle(getString(TodoAdapterContract.title))
                }
                if (containsKey(TodoAdapterContract.checked)) {
                    holder.bindCheckBox(getBoolean(TodoAdapterContract.checked))
                }
                return
            }
        }
        super.onBindViewHolder(holder, position, payloads)
    }

    class TodoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cbChecked = itemView.findViewById<CheckBox>(R.id.cbChecked)
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)

        fun bind (todoItemViewModel: TodoItemViewModel) {
            cbChecked.isChecked = todoItemViewModel.isDone
            tvTitle.text = todoItemViewModel.title
        }

        fun bindTitle (title: String) {
            tvTitle.text = title
        }

        fun bindCheckBox (checked: Boolean) {
            cbChecked.isChecked = checked
        }

    }

}