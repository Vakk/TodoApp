package com.valery.todo.ui.screens.todos.item

import com.valery.todo.model.db.Todo

data class TodoItemViewModel(var item: Todo): BaseTodoItemViewModel(item.id)