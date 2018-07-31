package com.valery.todo.model.db

data class Todo(var id: Long, var title: String, var isDone: Boolean, var sectionType: Int)