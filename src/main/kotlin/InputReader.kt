package org.example

import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class InputReader {
    fun readFile(filePath: String): String {
        val bufferedReader: BufferedReader = File("src/main/kotlin/$filePath").bufferedReader()
        return bufferedReader.use { it.readText() }
    }

    fun getBufferedReader(filePath: String): BufferedReader = BufferedReader(FileReader("src/main/kotlin/$filePath"))
}
