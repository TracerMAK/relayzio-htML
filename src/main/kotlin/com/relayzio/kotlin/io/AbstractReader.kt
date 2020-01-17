package com.relayzio.kotlin.io

import java.io.BufferedReader
import com.relayzio.kotlin.common.Result

abstract class AbstractReader(private val reader: BufferedReader) : InputReader {
    override fun readString(): Result<Pair<String, InputReader>> = try {
        reader.readLine().let {
            when {
                it.isEmpty() -> Result()
                else -> Result(Pair(it, this))
            }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    override fun readInt(): Result<Pair<Int, InputReader>> = try {
        reader.readLine().let {
            when {
                it.isEmpty() -> Result()
                else -> Result(Pair(it.toInt(), this))
            }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun close(): Unit = reader.close()
}
