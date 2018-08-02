package com.valery.todo.ui.screens.todos

import android.arch.lifecycle.MutableLiveData
import com.valery.todo.TodoApp
import com.valery.todo.model.db.item.Section
import com.valery.todo.model.db.item.Todo
import com.valery.todo.model.manager.todo.ITodoManager
import com.valery.todo.ui.base.BaseViewModel
import com.valery.todo.ui.screens.todos.item.BaseTodoItemViewModel
import com.valery.todo.ui.screens.todos.item.EmptyItemViewModel
import com.valery.todo.ui.screens.todos.item.SectionTodoItemViewModel
import com.valery.todo.ui.screens.todos.item.TodoItemViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class TodosViewModel : BaseViewModel() {
    val itemsLiveData: MutableLiveData<MutableList<BaseTodoItemViewModel>> = MutableLiveData()

    var sections: MutableList<SectionTodoItemViewModel> = mutableListOf()
    var itemViewModels: MutableList<TodoItemViewModel> = mutableListOf()

    @Inject
    lateinit var todoManager: ITodoManager

    private var lastValue: Int = 0

    private var dataPreparingDisposable: Disposable? = null

    private var isFirstLoading = true

    init {
        TodoApp.instance.daggerManger.viewModelComponent?.inject(this)
        sections.add(SectionTodoItemViewModel(Section(lastValue++.toLong(), "Shopping", 0), true))
        sections.add(SectionTodoItemViewModel(Section(lastValue++.toLong(), "Work", 1), true))
        updateLiveData()
    }

    fun addValue(done: Boolean, title: String) {
        itemViewModels.add(TodoItemViewModel(Todo(lastValue++.toLong(), title, done, Random().nextInt(2))))
        updateLiveData()
    }

    fun removeValue(todoItemViewModel: TodoItemViewModel) {
        itemViewModels.remove(todoItemViewModel)
        updateLiveData()
    }

    fun changeStatus(todoItemViewModel: TodoItemViewModel) {
        itemViewModels.firstOrNull { it.id == todoItemViewModel.id }?.item?.apply {
            isDone = !todoItemViewModel.item.isDone
        }
        updateLiveData()
    }

    fun addValue() {
        addValue(false, "Check box #$lastValue")
    }

    fun save() {
        todoManager.save(itemViewModels.map { it.item })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe ({

                }, {
                    it.printStackTrace()
                })
                .addTo(disposableBag)
    }

    fun load() {
        if (isFirstLoading) {
            todoManager.getAll()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        itemViewModels = it.map { TodoItemViewModel(it) }.toMutableList()
                        updateLiveData()
                    }, {
                        it.printStackTrace()
                    })
                    .addTo(disposableBag)
        }
        isFirstLoading = false
    }

    fun expandSection(section: SectionTodoItemViewModel) {
        sections.firstOrNull { it.id == section.id }?.isExpanded = !section.isExpanded
        updateLiveData()
    }

    private fun updateLiveData() {
        dataPreparingDisposable?.dispose()
        val aggregator: MutableList<BaseTodoItemViewModel> = mutableListOf()
        dataPreparingDisposable = Observable.just(this)
                .flatMapIterable { sections }
                .map { section -> section.copy(section = section.section.copy()) }
                .doOnNext { aggregator.add(it) }
                .doOnNext { section -> processSectionTodos(aggregator, section) }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe { dataToView -> itemsLiveData.postValue(aggregator) }
                .addTo(disposableBag)
    }

    private fun processSectionTodos(aggregator: MutableList<BaseTodoItemViewModel>, section: SectionTodoItemViewModel) {
        val todosForSection = itemViewModels
                .filter { it.item.sectionType == section.section.type }
                .map { it.copy(item = it.item.copy()) }
                .sortedBy { it.id }
                .sortedBy { it.item.isDone }
        section.allCount = todosForSection.size
        section.doneCount = todosForSection.count { it.item.isDone }
        if (section.isExpanded) {
            if (todosForSection.isNotEmpty()) {
                aggregator.addAll(todosForSection)
            } else {
                aggregator.add(EmptyItemViewModel(section.id, section.id))
            }
        }
    }
}