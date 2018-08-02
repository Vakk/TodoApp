package com.valery.todo.ui.screens.todos

import android.arch.lifecycle.MutableLiveData
import com.valery.todo.model.db.item.Section
import com.valery.todo.model.db.item.Todo
import com.valery.todo.model.manager.section.ISectionManager
import com.valery.todo.model.manager.todo.ITodoManager
import com.valery.todo.ui.DataStatus
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
import javax.inject.Inject

class TodosViewModel : BaseViewModel() {
    val itemsLiveData: MutableLiveData<MutableList<BaseTodoItemViewModel>> = MutableLiveData()

    var sections: MutableList<SectionTodoItemViewModel> = mutableListOf()
    var itemViewModels: MutableList<TodoItemViewModel> = mutableListOf()

    @Inject
    lateinit var todoManager: ITodoManager

    @Inject
    lateinit var sectionManager: ISectionManager

    private var dataPreparingDisposable: Disposable? = null

    private var isFirstLoading = true

    init {
        daggerManager.viewModelComponent?.inject(this)
        updateLiveData()
    }

    fun addValue(done: Boolean, title: String) {
        todoManager.save(Todo(0, title, false, 0))
        updateLiveData()
    }

    fun removeValue(todoItemViewModel: TodoItemViewModel) {
        itemViewModels.remove(todoItemViewModel)
        updateLiveData()
    }

    fun addSection(name: String) {
        sectionManager.save(Section(title = name, id = 0))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    sections.add(SectionTodoItemViewModel(it, false))
                    updateLiveData()
                }, {
                    it.printStackTrace()
                })
                .addTo(disposableBag)
    }

    fun addSection() {
        addSection("Section")
    }

    fun changeStatus(todoItemViewModel: TodoItemViewModel) {
        itemViewModels.firstOrNull { it.id == todoItemViewModel.id }?.item?.apply {
            isDone = !todoItemViewModel.item.isDone
        }
        updateLiveData()
    }

    fun load() {
        if (isFirstLoading) {
            dataStatusLiveData.postValue(DataStatus(DataStatus.TypeEnum.LOADING))
            todoManager.getAll()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        dataStatusLiveData.postValue(DataStatus(DataStatus.TypeEnum.SUCCESS))
                        itemViewModels = it.map { TodoItemViewModel(it) }.toMutableList()
                        updateLiveData()
                    }, {
                        dataStatusLiveData.postValue(DataStatus(DataStatus.TypeEnum.ERROR))
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
                .filter { it.item.sectionType == section.section.id }
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