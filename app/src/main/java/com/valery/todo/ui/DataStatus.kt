package com.valery.todo.ui

class DataStatus (val status: TypeEnum, val data: Any? = null) {

    enum class TypeEnum {
        LOADING,
        ERROR,
        SUCCESS
    }
}
