package com.valery.todo.model.db.item

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Section(
        @PrimaryKey(autoGenerate = true) var id: Long,
        var title: String,
        var type: Int
)