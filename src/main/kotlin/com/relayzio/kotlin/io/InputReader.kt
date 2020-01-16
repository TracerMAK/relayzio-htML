package com.relayzio.kotlin.io

import java.io.Closeable
import com.relayzio.kotlin.common.Result

/**
 * Provides methods for the reading data from various input sources such as
 * files, consoles, or scripts.
 */
interface InputReader : Closeable {
    fun readString() : Result<Pair<String, InputReader>>
    fun readInt() : Result<Pair<Int, InputReader>>
    
    fun readString(message: String) : Result<Pair<String, InputReader>> = readString()
    fun readInt(message: String) : Result<Pair<Int, InputReader>> = readInt()
}