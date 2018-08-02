package com.valery.todo.ui.screens.todos

import android.animation.Animator
import android.animation.AnimatorInflater
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import com.valery.todo.R
import com.valery.todo.ui.base.BaseAdapter
import com.valery.todo.ui.base.BaseViewHolder
import com.valery.todo.ui.screens.todos.item.BaseTodoItemViewModel
import com.valery.todo.ui.screens.todos.item.SectionTodoItemViewModel
import com.valery.todo.ui.screens.todos.item.TodoItemViewModel
import java.lang.ref.WeakReference

class TodosAdapter(todoCallback: TodoCallback?) : BaseAdapter<BaseViewHolder<out BaseTodoItemViewModel>, BaseTodoItemViewModel>() {

    protected var todoCallback = WeakReference(todoCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<out BaseTodoItemViewModel> {
        when (viewType) {
            TodoItemType.ITEM.value -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
                return TodoViewHolder(view)
            }
            TodoItemType.SECTION.value -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo_section, parent, false)
                return SectionViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo_section_empty, parent, false)
                return EmptySectionViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<out BaseTodoItemViewModel>, position: Int) {
        when (holder) {
            is TodoViewHolder -> {
                onBindViewHolder(holder, position)
            }
            is SectionViewHolder -> {
                onBindViewHolder(holder, position)
            }
            is EmptySectionViewHolder -> {

            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<out BaseTodoItemViewModel>, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            (payloads[0] as? Bundle)?.apply {
                when (holder) {
                    is TodoViewHolder -> {
                        onBindViewHolder(holder, position, payloads)
                    }
                    is SectionViewHolder -> {
                        onBindViewHolder(holder, position, payloads)
                    }
                    else -> {

                    }
                }
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is TodoItemViewModel -> TodoItemType.ITEM
            is SectionTodoItemViewModel -> TodoItemType.SECTION
            else -> TodoItemType.EMPTY_ITEM
        }.value
    }

    private fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val item = getItem(position) as TodoItemViewModel
        holder.bind(item)
    }

    private fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val item = getItem(position) as SectionTodoItemViewModel
        holder.bind(item)
    }

    private fun onBindViewHolder(holder: TodoViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            (payloads[0] as? Bundle)?.apply {
                val item = getItem(position) as TodoItemViewModel
                if (containsKey(TodoAdapterContract.EXTRA_TITLE)) {
                    holder.updateTitle(item.item.title)
                }
                if (containsKey(TodoAdapterContract.EXTRA_IS_DONE)) {
                    holder.updateDoneStatus(item.item.isDone)
                }
                return
            }
        }
    }

    private fun onBindViewHolder(holder: SectionViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            (payloads[0] as? Bundle)?.apply {
                val item = getItem(position) as SectionTodoItemViewModel
                if (containsKey(TodoAdapterContract.EXTRA_TITLE)) {
                    holder.updateTitle(item.section.title)
                }
                if (containsKey(TodoAdapterContract.EXTRA_EXPANDED)) {
                    holder.updateExpandState(item.isExpanded)
                }
                if (containsKey(TodoAdapterContract.EXTRA_COUNTER)) {
                    holder.updateCounter(item.allCount, item.doneCount)
                }
                return
            }
        }
    }

    inner class TodoViewHolder(itemView: View) : BaseViewHolder<TodoItemViewModel>(itemView) {
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        val vItemDone = itemView.findViewById<View>(R.id.vItemDone)
        val vCrossedOut = itemView.findViewById<View>(R.id.vCrossedOut)
        val ivDelete = itemView.findViewById<ImageView>(R.id.ivDelete)

        val rossedOutAnimator = AnimatorInflater.loadAnimator(itemView.context, R.animator.animator_crossed_out)
        val rossedInAnimator = AnimatorInflater.loadAnimator(itemView.context, R.animator.animator_crossed_in)

        init {
            ivDelete.setOnClickListener {
                (getItem(adapterPosition) as? TodoItemViewModel)?.let {
                    todoCallback.get()?.removeTodo(it)
                }
            }
        }

        fun bind(itemViewModel: TodoItemViewModel) {
            prepareCheckListener()

            tvTitle.text = itemViewModel.item.title

            if (itemViewModel.item.isDone) {
                vItemDone.visibility = View.VISIBLE
                vCrossedOut.visibility = View.VISIBLE
            } else {
                vItemDone.visibility = View.GONE
                vCrossedOut.visibility = View.GONE
            }
        }

        fun updateTitle(title: String) {
            tvTitle.text = title
        }

        fun updateDoneStatus(isDone: Boolean) {
            if (isDone) {
                vItemDone.visibility = View.VISIBLE
                vCrossedOut.visibility = View.VISIBLE
                rossedOutAnimator.setTarget(vCrossedOut)
                rossedOutAnimator.interpolator = AccelerateInterpolator()
                rossedOutAnimator.start()
            } else {
                vItemDone.visibility = View.GONE
                rossedInAnimator.setTarget(vCrossedOut)
                rossedInAnimator.interpolator = AccelerateInterpolator()
                rossedInAnimator.start()
            }
        }

        private fun prepareCheckListener() {
            itemView.setOnClickListener { v ->
                (getItem(adapterPosition) as? TodoItemViewModel)?.let { item ->
                    todoCallback.get()?.setDone(item)
                }
            }
        }
    }

    inner class SectionViewHolder(itemView: View) : BaseViewHolder<SectionTodoItemViewModel>(itemView) {
        val ivExpandState = itemView.findViewById<ImageView>(R.id.ivExpandState)
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        val tvCounter = itemView.findViewById<TextView>(R.id.tvCounter)
        val ivAddItem = itemView.findViewById<ImageView>(R.id.ivAddItem)
        val ivDelete = itemView.findViewById<ImageView>(R.id.ivDelete)

        val openAnimator: Animator by lazy {
            AnimatorInflater.loadAnimator(itemView.context, R.animator.animator_toogle_todo_open)
        }

        val closeAnimator: Animator by lazy {
            AnimatorInflater.loadAnimator(itemView.context, R.animator.animator_toogle_todo_close)
        }

        init {
            itemView.setOnClickListener {
                (getItem(adapterPosition) as? SectionTodoItemViewModel)?.let {
                    todoCallback.get()?.switchExpandState(it)
                }
            }
            ivAddItem.setOnClickListener {
                (getItem(adapterPosition) as? SectionTodoItemViewModel)?.let {
                    todoCallback.get()?.addTodo(it)
                }
            }
            ivDelete.setOnClickListener {
                (getItem(adapterPosition) as? SectionTodoItemViewModel)?.let {
                    todoCallback.get()?.removeSection(it)
                }
            }
        }

        fun bind(section: SectionTodoItemViewModel) {
            tvTitle.text = section.section.title
            ivExpandState.rotation = if (section.isExpanded) 225f else 0f
            tvCounter.text = "(${section.doneCount}/${section.allCount})"
        }

        fun updateTitle(title: String) {
            tvTitle.text = title
        }

        fun updateExpandState(expanded: Boolean) {
            if (expanded) {
                openAnimator.interpolator = OvershootInterpolator()
                openAnimator.setTarget(ivExpandState)
                openAnimator.start()
            } else {
                closeAnimator.interpolator = OvershootInterpolator()
                closeAnimator.setTarget(ivExpandState)
                closeAnimator.start()
            }
        }

        fun updateCounter(itemsCount: Int, doneCount: Int) {
            tvCounter.text = "($doneCount/$itemsCount)"
        }

    }

    inner class EmptySectionViewHolder(itemView: View) : BaseViewHolder<SectionTodoItemViewModel>(itemView)
}

private enum class TodoItemType(val value: Int) {
    SECTION(0), ITEM(1), EMPTY_ITEM(2);
}
