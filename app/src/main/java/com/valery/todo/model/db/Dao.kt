package com.valery.todo.model.db

import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Update

interface Dao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert (vararg item: T)

    @Delete
    fun delete (vararg item: T)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update (vararg item: T)
}