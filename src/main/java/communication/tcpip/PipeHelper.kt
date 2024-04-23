package communication.tcpip

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket
import java.util.*

class PipeHelper(outputStream: OutputStream, inputStream: InputStream) {
    init {
        Companion.outputStream = outputStream
        Companion.inputStream = inputStream
    }

    fun sendPipeReplyMessage(message: String): String {
        return try {
            outputStream.write((message + "\r\n").toByteArray())
            outputStream.flush()
            val sc = Scanner(inputStream, "ISO-8859-2")
            sc.nextLine()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    companion object {
        private lateinit var outputStream: OutputStream
        private lateinit var inputStream: InputStream
        fun sendPipeRequestWithoutAuth(host: String, port: Int, message: String): String {
            val interFaceSocket: PipeHelper = connect(host, port)
            return interFaceSocket.sendPipeReplyMessage(message)
        }

        fun sendPipeRequestBBAuth(
            Host: String,
            HostPort: String,
            bbOrg: String,
            bbUser: String,
            requestString: String
        ): String {
            return try {
                val pu = PipeUtil.connectBBAuth(Host, HostPort, bbOrg, bbUser)
                println("Request.: $requestString")
                val response = pu.sendMessage(requestString)
                println("Response.: $response")
                pu.finalizer()
                response
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }

        fun connect(host: String, port: Int): PipeHelper {
            return try {
                val socket = Socket(host, port)
                val outputStream = socket.getOutputStream()
                val `is` = socket.getInputStream()
                PipeHelper(outputStream, `is`)
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
    }
}
