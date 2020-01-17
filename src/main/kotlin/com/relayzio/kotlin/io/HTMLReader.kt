package com.relayzio.kotlin.io

import java.io.BufferedReader
import java.io.File

import com.relayzio.kotlin.common.Result

class HTMLReader private constructor(private val reader: BufferedReader) :
                                      AbstractReader(reader), AutoCloseable {
    override fun close() {
        reader.close()
    }
    
    companion object {
        operator fun invoke(path: String): Result<InputReader> = try {
            Result(HTMLReader(File(path).bufferedReader()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
                       