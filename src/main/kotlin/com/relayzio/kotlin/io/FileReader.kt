package com.relayzio.kotlin.io

import java.io.BufferedReader
import java.io.File
import java.nio.file.Files
import java.nio.file.FileSystems

import com.relayzio.kotlin.common.Result

open class FileReader (protected val reader: BufferedReader) :
                           AbstractReader(reader), AutoCloseable, Validator {
                      
    override fun close() {
        reader.close()
    }
    
    override fun validate(resource: Any): Boolean =
        Files.exists(FileSystems.getDefault().getPath(resource as String))
    
    companion object {
        operator fun invoke(path: String): Result<InputReader> = try {
            Result(FileReader(File(path).bufferedReader()))
        } catch (e: Exception) {
            Result.failure(e)
        }
        
        
    }
}
                       