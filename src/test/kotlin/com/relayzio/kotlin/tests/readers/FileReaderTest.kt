package com.relayzio.kotlin.tests.readers

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.Test

import com.relayzio.kotlin.common.Result
import com.relayzio.kotlin.io.FileReader
import com.relayzio.kotlin.io.InputReader

class FileReaderTest {
    val path = "target\\classes\\test1.txt"
    val path2 = "target\\classes\\notfound.txt"
    
    fun processResult(result: Result<InputReader>, msgExpected: String): Unit {
        var msg = ""
        result.forEach( onSuccess = {msg = "Success"}, onFailure = {msg = "Failure"})
        println(result)
        assertEquals(msgExpected, msg)
    }
    
    @Test fun fileExists() : Unit {
         val result = FileReader(path)
         processResult(result, "Success")
    }
    
    @Test fun fileNotExists() : Unit {
        val result = FileReader(path2)
        processResult(result, "Failure")
     }
     
    @Test fun readOneLine(): Unit {
        val firstLine = FileReader(path).flatMap {
            reader -> reader.use { reader.readString() }
        }.map { pair -> pair.first }.getOrElse({""})
        
        println("First line of test1.txt => ${firstLine}")
        assertEquals("Hello", firstLine)
    }
}
                       