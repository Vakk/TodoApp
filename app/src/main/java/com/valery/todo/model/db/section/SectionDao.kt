package com.valery.todo.model.db.section

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import com.valery.todo.model.db.item.Section

@Dao
abstract class SectionDao : com.valery.todo.model.db.Dao<Section> {
    @Query("SELECT * from Section")
    abstract fun getAll(): List<Section>

    @Query("SELECT * from Section WHERE id = :id")
    abstract fun get(id: Long): Section

    @Query("DELETE from Section WHERE id = :id")
    abstract fun delete(id: Long)

    @Transaction
    open fun deleteAllWith(ids: List<Long>) {
        for (id in ids) {
            delete(id)
        }
    }
}