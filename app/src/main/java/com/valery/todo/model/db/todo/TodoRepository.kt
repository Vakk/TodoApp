package com.valery.todo.model.db.todo

import com.valery.todo.model.db.item.Todo
import io.reactivex.Maybe
import io.reactivex.Observable

class TodoRepository(val dao: TodoDao) : ITodoRepository {

    override fun save(item: Todo): Observable<Todo> {
        return Observable.fromCallable {
            val id = dao.insert(item)[0]
            get(id).blockingGet()
        }
    }

    override fun save(items: List<Todo>): Observable<List<Todo>> {
        return Observable.fromCallable {
            val result = dao.insert(*items.toTypedArray())
            val aggregator = mutableListOf<Todo>()
            for (id in result) {
                aggregator.add(get(id).blockingGet())
            }
            return@fromCallable aggregator
        }
    }

    override fun remove(id: Long): Observable<Boolean> {
        return Observable.fromCallable {
            dao.delete(id)
            true
        }
    }

    override fun remove(ids: List<Long>): Observable<Boolean> {
        return Observable.fromCallable {
            dao.deleteAllWith(ids)
            true
        }
    }

    override fun update(item: Todo): Observable<Todo> {
        return Observable.fromCallable {
            dao.update(item)
            item
        }
    }

    override fun update(items: List<Todo>): Observable<List<Todo>> {
        return Observable.fromCallable {
            dao.update(*items.toTypedArray())
            items
        }
    }

    override fun getAll(): Observable<List<Todo>> {
        return Observable.fromCallable {
            dao.getAll()
        }
    }

    override fun get(id: Long): Maybe<Todo> {
        return Maybe.fromCallable {
            dao.get(id)
        }
    }

}