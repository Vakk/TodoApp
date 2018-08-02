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
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TodosViewModel : BaseViewModel() {
    val itemsLiveData: MutableLiveData<MutableList<BaseTodoItemViewModel>> = MutableLiveData()

    var sectionsViewModels: MutableList<SectionTodoItemViewModel> = mutableListOf()
    var todoViewModels: MutableList<TodoItemViewModel> = mutableListOf()

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

    fun addSection(name: String) {
        sectionManager.save(Section(title = name, id = 0))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    sectionsViewModels.add(SectionTodoItemViewModel(it, false))
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
        todoViewModels.firstOrNull { it.id == todoItemViewModel.id }?.item?.apply {
            isDone = !todoItemViewModel.item.isDone
        }
        updateLiveData()
    }

    fun load() {
        if (isFirstLoading) {
            dataStatusLiveData.postValue(DataStatus(DataStatus.TypeEnum.LOADING))
            Observable.zip(todoManager.getAll(), sectionManager.getAll(), BiFunction { todos: List<Todo>, sections: List<Section> ->
                todoViewModels = todos.map { TodoItemViewModel(it) }.toMutableList()
                this.sectionsViewModels = sections.map { SectionTodoItemViewModel(it, false, 0, 0) }.toMutableList()
                todos
            })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        dataStatusLiveData.postValue(DataStatus(DataStatus.TypeEnum.SUCCESS))
                        updateLiveData()
                    }, {
                        dataStatusLiveData.postValue(DataStatus(DataStatus.TypeEnum.ERROR))
                    })
                    .addTo(disposableBag)
        }
        isFirstLoading = false
    }

    fun expandSection(section: SectionTodoItemViewModel) {
        sectionsViewModels.firstOrNull { it.id == section.id }?.isExpanded = !section.isExpanded
        updateLiveData()
    }

    private fun updateLiveData() {
        dataPreparingDisposable?.dispose()
        val aggregator: MutableList<BaseTodoItemViewModel> = mutableListOf()
        dataPreparingDisposable = Observable.just(this)
                .flatMapIterable { sectionsViewModels }
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
        val todosForSection = todoViewModels
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

    fun addTodo(section: SectionTodoItemViewModel) {
        val todo = Todo(0, "Todo", false, section.id)
        todoManager.save(todo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    todoViewModels.add(TodoItemViewModel(it))
                    updateLiveData()
                }, {
                    it.printStackTrace()
                })
                .addTo(disposableBag)
    }

    fun save() {
        todoManager.save(todoViewModels.map { it.item })
                .flatMap { sectionManager.save(sectionsViewModels.map { it.section }) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({}, { it.printStackTrace() })
                .addTo(disposableBag)
    }

    fun removeTodo(todo: TodoItemViewModel) {
        todoManager.remove(todo.id)
                .doOnNext { todoViewModels.removeAll { it.item.id == todo.id } }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    updateLiveData()
                }, {
                    it.printStackTrace()
                })
    }

    fun removeSection(section: SectionTodoItemViewModel) {
        todoManager.getAll()
                .flatMapIterable { it }
                .filter { it.sectionType != section.id }
                .flatMap { todoManager.remove(it.id) }
                .doOnNext { todoViewModels.removeAll{ it.item.sectionType == section.id} }
                .toList()
                .toObservable()
                .flatMap { sectionManager.remove(section.id) }
                .doOnNext { sectionsViewModels.removeAll { it.id == section.id } }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    updateLiveData()
                }, {it.printStackTrace()})
                .addTo(disposableBag)
    }
}