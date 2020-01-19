package com.relayzio.kotlin.io

import java.io.BufferedReader

import com.relayzio.kotlin.common.Result

class HTMLReader private constructor(reader: BufferedReader) : Validator,
                                      FileReader(reader) {
    
    companion object {

    }
}
                       