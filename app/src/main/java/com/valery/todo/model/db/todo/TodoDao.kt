package com.valery.todo.model.db.todo

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import com.valery.todo.model.db.item.Todo

@Dao
abstract class TodoDao : com.valery.todo.model.db.Dao<Todo> {
    @Query("SELECT * from Todo")
    abstract fun getAll(): List<Todo>

    @Query("SELECT * from Todo WHERE id = :id")
    abstract fun get(id: Long): Todo

    @Query("DELETE from Todo WHERE id = :id")
    abstract fun delete(id: Long)

    @Transaction
    open fun deleteAllWith(ids: List<Long>) {
        for (id in ids) {
            delete(id)
        }
    }
}