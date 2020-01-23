package com.relayzio.kotlin.io

import java.io.BufferedReader
import java.net.URL

import com.relayzio.kotlin.common.Result

class HtmlReader private constructor(reader: BufferedReader) : FileReader(reader) {
    
    companion object {
        operator fun invoke(url: String): Result<InputReader> = try {
            // open a URL
            val urladdr = URL(url)
            // create a buffered stream
            
            // validate
            
            Result.failure("Not implemented")
        } catch (e: Exception) {
            Result.failure(e)
        }

    }
}
                       