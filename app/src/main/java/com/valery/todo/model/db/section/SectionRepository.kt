package com.valery.todo.model.db.section

import com.valery.todo.model.db.Repository
import com.valery.todo.model.db.item.Section
import io.reactivex.Maybe
import io.reactivex.Observable

class SectionRepository(val dao: SectionDao) : ISectionRepository {

    override fun save(item: Section): Observable<Section> {
        return Observable.fromCallable {
            dao.insert(item)
            item
        }
    }

    override fun save(items: List<Section>): Observable<List<Section>> {
        return Observable.fromCallable {
            dao.insert(*items.toTypedArray())
            items
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

    override fun update(item: Section): Observable<Section> {
        return Observable.fromCallable {
            dao.update(item)
            item
        }
    }

    override fun update(items: List<Section>): Observable<List<Section>> {
        return Observable.fromCallable {
            dao.update(*items.toTypedArray())
            items
        }
    }

    override fun getAll(): Observable<List<Section>> {
        return Observable.fromCallable {
            dao.getAll()
        }
    }

    override fun get(id: Long): Maybe<Section> {
        return Maybe.fromCallable {
            dao.get(id)
        }
    }

}