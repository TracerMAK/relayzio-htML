package com.relayzio.kotlin.io

interface Validator {
    fun validate(resource: Any): Boolean

}
                       