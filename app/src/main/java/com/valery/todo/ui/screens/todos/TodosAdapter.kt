package com.valery.todo.ui.screens.todos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.valery.todo.R
import com.valery.todo.ui.base.BaseAdapter
import com.valery.todo.ui.base.BaseViewHolder
import com.valery.todo.ui.screens.todos.item.BaseTodoItem
import com.valery.todo.ui.screens.todos.item.SectionTodoItem
import com.valery.todo.ui.screens.todos.item.TodoItem
import java.lang.ref.WeakReference

class TodosAdapter(todoCallback: TodoCallback?) : BaseAdapter<BaseViewHolder<out BaseTodoItem>, BaseTodoItem>() {

    protected var todoCallback = WeakReference(todoCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<out BaseTodoItem> {
        when (viewType) {
            TodoItemType.ITEM.value -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
                return TodoViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo_section, parent, false)
                return SectionViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<out BaseTodoItem>, position: Int) {
        when (holder) {
            is TodoViewHolder -> {
                onBindViewHolder(holder, position)
            }
            is SectionViewHolder -> {
                onBindViewHolder(holder, position)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<out BaseTodoItem>, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            (payloads[0] as? Bundle)?.apply {
                when (holder) {
                    is TodoViewHolder -> {
                        onBindViewHolder(holder, position, payloads)
                    }
                    is SectionViewHolder -> {
                        onBindViewHolder(holder, position, payloads)
                    }
                }
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is TodoItem -> TodoItemType.ITEM
            is SectionTodoItem -> TodoItemType.SECTION
            else -> TodoItemType.SECTION
        }.value
    }

    private fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val item = getItem(position) as TodoItem
        holder.bind(item)
    }

    private fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val item = getItem(position) as SectionTodoItem
        holder.bind(item)
    }

    private fun onBindViewHolder(holder: TodoViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            (payloads[0] as? Bundle)?.apply {
                val item = getItem(position) as TodoItem
                if (containsKey(TodoAdapterContract.EXTRA_TITLE)) {
                    holder.updateTitle(item.title)
                }
                if (containsKey(TodoAdapterContract.EXTRA_IS_DONE)) {
                    holder.updateCheckBox(item.isDone)
                }
                return
            }
        }
    }

    private fun onBindViewHolder(holder: SectionViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            (payloads[0] as? Bundle)?.apply {
                val item = getItem(position) as SectionTodoItem
                if (containsKey(TodoAdapterContract.EXTRA_TITLE)) {
                    holder.updateTitle(item.title)
                }
                if (containsKey(TodoAdapterContract.EXTRA_EXPANDED)) {
                    holder.updateExpandState(item.isExpanded)
                }
                return
            }
        }
    }

    inner class TodoViewHolder(itemView: View) : BaseViewHolder<TodoItem>(itemView) {
        val cbChecked = itemView.findViewById<CheckBox>(R.id.cbChecked)
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)

        fun bind(item: TodoItem) {
            cbChecked.setOnCheckedChangeListener(null)
            cbChecked.isChecked = item.isDone
            prepareCheckListener()

            tvTitle.text = item.title
        }

        fun updateTitle(title: String) {
            tvTitle.text = title
        }

        fun updateCheckBox(checked: Boolean) {
            cbChecked.setOnCheckedChangeListener(null)
            cbChecked.isChecked = checked
            prepareCheckListener()
        }

        private fun prepareCheckListener() {
            val item = getItem(adapterPosition) as? TodoItem
            item?.let { item ->
                cbChecked.setOnCheckedChangeListener { btn, isCheked ->
                    todoCallback.get()?.setDone(item)
                }
            }
        }
    }

    inner class SectionViewHolder(itemView: View) : BaseViewHolder<SectionTodoItem>(itemView) {
        val ivExpandState = itemView.findViewById<ImageView>(R.id.ivExpandState)
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)

        init {
            itemView.setOnClickListener {
                val item = getItem(adapterPosition) as? SectionTodoItem
                item?.let {
                    todoCallback.get()?.switchExpandState(it)
                }
            }
        }

        fun bind(item: SectionTodoItem) {
            tvTitle.text = item.title
            ivExpandState.rotation = if (item.isExpanded) 225f else 0f
        }

        fun updateTitle(title: String) {
            tvTitle.text = title
        }

        fun updateExpandState(expanded: Boolean) {
            ivExpandState.animate().setInterpolator(OvershootInterpolator()).rotation(if (expanded) 225f else 0f).duration = 300
        }
    }
}

private enum class TodoItemType(val value: Int) {
    SECTION(0), ITEM(1);
}
