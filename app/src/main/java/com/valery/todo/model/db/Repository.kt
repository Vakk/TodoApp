package com.valery.todo.model.db

import io.reactivex.Maybe
import io.reactivex.Observable

interface Repository<Item, Id> {
    fun save(item: Item): Observable<Item>

    fun save(items: List<Item>): Observable<List<Item>>

    fun remove(id: Id): Observable<Boolean>

    fun remove(ids: List<Id>): Observable<Boolean>

    fun update(item: Item): Observable<Item>

    fun update(items: List<Item>): Observable<List<Item>>

    fun getAll(): Observable<List<Item>>

    fun get(id: Id): Maybe<Item>
}