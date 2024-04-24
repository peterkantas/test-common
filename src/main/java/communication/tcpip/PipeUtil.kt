package communication.tcpip

import org.springframework.util.DigestUtils
import java.io.IOException
import java.io.OutputStream
import java.net.Socket
import java.nio.charset.Charset
import java.util.*

class PipeUtil {
    private lateinit var socket: Socket
    private lateinit var outputStream: OutputStream
    private lateinit var scanner: Scanner

    constructor(socket: Socket, outputStream: OutputStream, scanner: Scanner) {
        this.scanner = scanner
        this.socket = socket
        this.outputStream = outputStream
    }

    private constructor()

    @Throws(IOException::class)
    fun sendMessage(message: String): String {
        return try {
            outputStream.write((message + "\r\n").toByteArray(charset("ISO-8859-2")))
            outputStream.flush()
            scanner.nextLine()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    fun finalizer() {
        try {
            socket.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        fun connectBBAuth(Host: String, interfacePort: String, bborganization: String, bbuser: String): PipeUtil {
            return try {
                val socket = Socket(Host, interfacePort.toInt())
                val outputStream = socket.getOutputStream()
                val `is` = socket.getInputStream()
                val scanner = Scanner(`is`, "ISO-8859-2")
                val welcomeMessage = scanner.nextLine()
                val password =
                    DigestUtils.md5DigestAsHex((welcomeMessage + "jelszo").toByteArray()).uppercase(Locale.getDefault())
                outputStream.write("BB|$bborganization|$bbuser|$password|\r\n".toByteArray(Charset.forName("ISO-8859-2")))
                outputStream.flush()
                scanner.nextLine()
                PipeUtil(socket, outputStream, scanner)
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
    }
}
