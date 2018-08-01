package com.valery.todo.model.db.todo

import com.valery.todo.model.db.Repository
import com.valery.todo.model.db.item.Todo

interface ITodoRepository : Repository<Todo, Long> {

}