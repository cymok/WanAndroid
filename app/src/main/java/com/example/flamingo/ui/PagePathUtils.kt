package com.example.flamingo.ui

fun List<String>.getCurrent(): String? {
    if (this.isEmpty()) {
        return null
    }
    return this[this.size - 1]
}

fun List<String>.getParent(): String? {
    if (this.size < 2) {
        return null
    }
    return this[this.size - 2]
}
