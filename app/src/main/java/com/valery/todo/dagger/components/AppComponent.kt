package com.valery.todo.dagger.components

import android.content.Context
import com.valery.todo.dagger.modules.DbModule
import com.valery.todo.dagger.modules.ManagerModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface AppComponent {

    fun addViewModelComponent(dbModule: DbModule, managerModule: ManagerModule): ViewModelComponent

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }

}