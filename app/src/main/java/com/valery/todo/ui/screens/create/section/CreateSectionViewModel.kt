package com.valery.todo.ui.screens.create.section

import android.arch.lifecycle.MutableLiveData
import com.valery.todo.ui.base.BaseViewModel

class CreateSectionViewModel: BaseViewModel() {
    val completeLiveData = MutableLiveData<Boolean>()

    fun createSection (name: String) {

    }
}