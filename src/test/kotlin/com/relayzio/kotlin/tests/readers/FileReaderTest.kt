package com.relayzio.kotlin.tests.readers

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.Test

import com.relayzio.kotlin.common.Result
import com.relayzio.kotlin.io.FileReader

class FileReaderTest {
    val path = "target\\classes\\test1.txt"
    val path2 = "target\\classes\\notfound.txt"
    
    @Test fun fileExists() : Unit {
         val result = FileReader(path)
         var msg = ""
         result.forEach( onSuccess = {msg = "Success"}, onFailure = {msg = "Failure"})
         println(result)
         assertEquals("Success", msg)
    }
    
    @Test fun fileNotExists() : Unit {
        val result = FileReader(path2)
         var msg = ""
         result.forEach( onSuccess = {msg = "Success"}, onFailure = {msg = "Failure"})
         println(result)
         assertEquals("Failure", msg)
     }        
}
                       