package com.valery.todo.dagger.modules

import android.arch.persistence.room.Room
import android.content.Context
import com.valery.todo.AppDatabase
import com.valery.todo.dagger.SessionScope
import com.valery.todo.model.db.section.ISectionRepository
import com.valery.todo.model.db.section.SectionRepository
import com.valery.todo.model.db.todo.ITodoRepository
import com.valery.todo.model.db.todo.TodoRepository
import dagger.Module
import dagger.Provides

@Module
class DbModule {

    @Provides
    @SessionScope
    fun provideAppDatabase(context: Context) = Room
            .databaseBuilder(context.applicationContext, AppDatabase::class.java, context.packageName)
            .build()

    @Provides
    @SessionScope
    fun provideTodoRepository(appDatabase: AppDatabase): ITodoRepository = TodoRepository(appDatabase.todoDao())

    @Provides
    @SessionScope
    fun provideSectionRepository(appDatabase: AppDatabase): ISectionRepository = SectionRepository(appDatabase.sectionDao())
}