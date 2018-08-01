package com.valery.todo.dagger.modules

import android.arch.persistence.room.Room
import android.content.Context
import com.valery.todo.AppDatabase
import com.valery.todo.model.db.section.ISectionRepository
import com.valery.todo.model.db.section.SectionRepository
import com.valery.todo.model.db.todo.ITodoRepository
import com.valery.todo.model.db.todo.TodoRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule {

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context) = Room
            .databaseBuilder(context.applicationContext, AppDatabase::class.java, context.packageName)
            .build()

    @Provides
    @Singleton
    fun provideTodoRepository (appDatabase: AppDatabase): ITodoRepository = TodoRepository(appDatabase.todoDao())

    @Provides
    @Singleton
    fun provideSectionRepository (appDatabase: AppDatabase): ISectionRepository = SectionRepository(appDatabase.sectionDao())
}