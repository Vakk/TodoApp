package com.valery.todo.dagger.modules

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val application: Application) {
    @Provides
    @Singleton
    fun provideContext (application: Application): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideApplication () = application
}