package com.valery.todo

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.valery.todo.model.db.item.Section
import com.valery.todo.model.db.item.Todo
import com.valery.todo.model.db.section.SectionDao
import com.valery.todo.model.db.todo.TodoDao

@Database(entities = [
    (Todo::class),
    (Section::class) // TODO: Find more flexible way for do this.
], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao

    abstract fun sectionDao(): SectionDao

    companion object {

        private var INSTANCE: AppDatabase? = null

        fun getAppDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, context.packageName)
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return INSTANCE as AppDatabase
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}