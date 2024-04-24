package reading.file

import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStream
import java.io.InputStreamReader

class FileReader {
    fun readFromInputStream(path: String): String {
        val inputStream =
            FileInputStream(path) // Példa InputStream létrehozása
        val requestString = readFromInputStream(inputStream)
        inputStream.close()
        return requestString
    }

    fun readFromInputStream(inputStream: InputStream): String {
        val reader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        var line: String?

        try {
            while (reader.readLine().also { line = it } != null) {
                stringBuilder.append(line).append("\n")
            }
        } finally {
            reader.close()
        }

        return stringBuilder.toString()
    }
}