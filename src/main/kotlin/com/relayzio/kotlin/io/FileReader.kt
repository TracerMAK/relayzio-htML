package com.relayzio.kotlin.io

import java.io.BufferedReader
import java.io.File

import com.relayzio.kotlin.common.Result

open class FileReader (protected val reader: BufferedReader) :
                           AbstractReader(reader), AutoCloseable {
                      
    override fun close() {
        reader.close()
    }    
    companion object {
        operator fun invoke(path: String): Result<InputReader> = try {
            Result(FileReader(File(path).bufferedReader()))
        } catch (e: Exception) {
            Result.failure(e)
        }

    }
}
                       