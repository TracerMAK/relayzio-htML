package com.relayzio.kotlin.io

interface Validator<in T> {
    fun validate(resource: T): Boolean
    fun exists(resource: T): Boolean
}
                       