package com.relayzio.kotlin.io

import java.nio.file.Files
import java.nio.file.FileSystems

class FileValidator : Validator<String> {
    
	override fun exists(resource: String): Boolean =
	    Files.exists(FileSystems.getDefault().getPath(resource))
	
    override fun validate(resource: String): Boolean = exists(resource)
	    
		
}