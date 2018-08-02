package com.valery.todo.utils.extensions

import android.view.View

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide(isGone: Boolean = false) {
    visibility = if (isGone) View.GONE else View.INVISIBLE
}