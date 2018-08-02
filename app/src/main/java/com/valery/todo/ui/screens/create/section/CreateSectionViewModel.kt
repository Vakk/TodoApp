package com.valery.todo.ui.screens.create.section

import com.valery.todo.model.db.item.Section
import com.valery.todo.model.manager.section.ISectionManager
import com.valery.todo.ui.DataStatus
import com.valery.todo.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CreateSectionViewModel : BaseViewModel() {

    @Inject
    lateinit var sectionManager: ISectionManager

    init {
        daggerManager.viewModelComponent?.inject(this)
    }

    fun createSection(name: String) {
        sectionManager.save(Section(0, name))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    dataStatusLiveData.postValue(DataStatus(DataStatus.TypeEnum.SUCCESS))
                }, {
                    dataStatusLiveData.postValue(DataStatus(DataStatus.TypeEnum.ERROR))
                })
                .addTo(disposableBag)
    }
}