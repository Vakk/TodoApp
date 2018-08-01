package com.valery.todo.model.manager

import com.valery.todo.model.db.item.Todo
import com.valery.todo.model.db.todo.ITodoRepository
import io.reactivex.Maybe
import io.reactivex.Observable

class TodoManager(val repository: ITodoRepository): Manager<Todo, Long> {

    override fun save(item: Todo): Observable<Todo> {
        return repository.save(item)
    }

    override fun save(items: List<Todo>): Observable<List<Todo>> {
        return repository.save(items)
    }

    override fun remove(id: Long): Observable<Boolean> {
        return repository.remove(id)
    }

    override fun remove(ids: List<Long>): Observable<Boolean> {
        return repository.remove(ids)
    }

    override fun update(item: Todo): Observable<Todo> {
        return repository.update(item)
    }

    override fun update(items: List<Todo>): Observable<List<Todo>> {
        return repository.update(items)
    }

    override fun getAll(): Observable<List<Todo>> {
        return repository.getAll()
    }

    override fun get(id: Long): Maybe<Todo> {
        return repository.get(id)
    }
}