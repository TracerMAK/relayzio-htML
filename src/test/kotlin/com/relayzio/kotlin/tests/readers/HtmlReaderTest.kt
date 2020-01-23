package com.relayzio.kotlin.tests.readers

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.Test

import com.relayzio.kotlin.common.Result
import com.relayzio.kotlin.io.HtmlReader
import com.relayzio.kotlin.io.InputReader

class HtmlReaderTest {
    val badPortUrl = "http://example.com:-80/"
    
    fun processResult(result: Result<InputReader>, msgExpected: String): Unit {
        var msg = ""
        result.forEach( onSuccess = {msg = "Success"}, onFailure = {msg = "Failure"})
        println(result)
        assertEquals(msgExpected, msg)
    }
    
    @Test fun malformedURL() : Unit {
         val result = HtmlReader(badPortUrl)
         processResult(result, "Failure")
    }   
}
                       