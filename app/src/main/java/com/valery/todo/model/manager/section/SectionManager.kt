package com.valery.todo.model.manager.section

import com.valery.todo.model.db.item.Section
import com.valery.todo.model.db.section.ISectionRepository
import io.reactivex.Maybe
import io.reactivex.Observable

class SectionManager(val repository: ISectionRepository) : ISectionManager {

    override fun save(item: Section): Observable<Section> {
        return repository.save(item)
    }

    override fun save(items: List<Section>): Observable<List<Section>> {
        return repository.save(items)
    }

    override fun remove(id: Long): Observable<Boolean> {
        return repository.remove(id)
    }

    override fun remove(ids: List<Long>): Observable<Boolean> {
        return repository.remove(ids)
    }

    override fun update(item: Section): Observable<Section> {
        return repository.update(item)
    }

    override fun update(items: List<Section>): Observable<List<Section>> {
        return repository.update(items)
    }

    override fun getAll(): Observable<List<Section>> {
        return repository.getAll()
    }

    override fun get(id: Long): Maybe<Section> {
        return repository.get(id)
    }
}