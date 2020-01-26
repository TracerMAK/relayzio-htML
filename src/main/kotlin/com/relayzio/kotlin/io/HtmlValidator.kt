package com.relayzio.kotlin.io

import java.net.HttpURLConnection
import java.net.URL

import com.relayzio.kotlin.common.Result

class HtmlValidator : Validator<URL> {
    
	/**
	 * Send a HEAD request to check that a remote URL exists.
	 *
	 * @param resource  The remote URL to connect to.
	 * @return          True if a connection can be made, false otherwise.
	 */
	override fun exists(resource: URL): Boolean {
	    val conn = resource.openConnection() as HttpURLConnection		
		conn.setRequestMethod("HEAD")
		val code = Result.of { (conn.getResponseCode()) }.getOrElse(0) 
		return if (code == 200) true else false
	}

    override fun validate(resource: URL): Boolean = TODO("")
	       	
    /**
	 * Checks for <!DOCTYPE html> occurrence in first line of html file.
	 *
	 * @param s  The first line of the html file which contains the document type.
	 * @return   True if a DOCTYPE found, false otherwise.
	 */
    fun docTypeFound(s: String): Boolean = s.contains("<!DOCTYPE html>")
}