package com.relayzio.kotlin.tests.validators

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.URL

import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.Test

import com.relayzio.kotlin.common.Result
import com.relayzio.kotlin.io.HtmlValidator

class ValidatorTest {
    val noDocType = "target//classes//nodoctype.html"
	val nonExistingURL = "http://blahblahblah.com"
	val anExistingURL = "https://www.kotlinlang.org"
	val htmlValidator = HtmlValidator()
	
    @Test
	fun checkNonExistingURL() {
		assertFalse(htmlValidator.exists(URL(nonExistingURL)))
	}
	
	@Test
	fun checkExistingURL() {
	    assertTrue(htmlValidator.exists(URL(anExistingURL)))
    }
	
	@Test
	fun missingDocType() {
	    val s = File(noDocType).bufferedReader().readLine()
	    assertFalse(htmlValidator.docTypeFound(s))
    }
	
	@Test
	fun foundDocType() {
	    val url = URL(anExistingURL)
		val bufferedReader = BufferedReader(InputStreamReader(url.openStream(), "UTF-8"))
	    assertTrue(htmlValidator.docTypeFound(bufferedReader.readLine()))
	}
}