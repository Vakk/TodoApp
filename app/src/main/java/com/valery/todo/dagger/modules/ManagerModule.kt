package com.valery.todo.dagger.modules

import com.valery.todo.dagger.SessionScope
import com.valery.todo.model.db.section.ISectionRepository
import com.valery.todo.model.db.section.SectionRepository
import com.valery.todo.model.db.todo.ITodoRepository
import com.valery.todo.model.manager.section.ISectionManager
import com.valery.todo.model.manager.section.SectionManager
import com.valery.todo.model.manager.todo.ITodoManager
import com.valery.todo.model.manager.todo.TodoManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ManagerModule {

    @Provides
    @SessionScope
    fun provideTodoManager(todoRepository: ITodoRepository): ITodoManager = TodoManager(todoRepository)

    @Provides
    @SessionScope
    fun provideSectionManager(sectionRepository: ISectionRepository): ISectionManager = SectionManager(sectionRepository)

}