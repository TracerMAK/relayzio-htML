package com.relayzio.kotlin.io

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

import com.relayzio.kotlin.common.Result

class HtmlReader private constructor(reader: BufferedReader) : 
                    FileReader(reader) {

    companion object {
        operator fun invoke(url: String): Result<InputReader> = try {
            val stream = URL(url).openStream()
            Result()
            //validate(stream) ? HtmlReader(BufferedReader(InputStreamReader(stream, "UTF-8"))) : Result() 
        } catch (e: Exception) {
            Result.failure(e)
        }

    }
}
                       