package jihun.bang.searchwiki.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import javax.net.ssl.HttpsURLConnection


object NetworkModule {
    fun setConnection(
        protocol: Protocol = Protocol.HTTPS,
        connectTimeout: Int = 5000,
        readTimeout: Int = 5000,
        uri: String = "en.wikipedia.org/api/rest_v1/page",
        requestProperties: List<Pair<String, String>> = listOf("Accept" to "application/json"),
        method: String = "GET",
        api: String,
        postData: String = ""
    ): JSONObject? {
        val url = URL("${protocol.name.lowercase()}://$uri/$api")
        val connection = when (protocol) {
            Protocol.HTTP -> url.openConnection() as HttpURLConnection
            Protocol.HTTPS -> url.openConnection() as HttpsURLConnection
        }
        requestProperties.forEach {
            connection.setRequestProperty(it.first, it.second)
        }
        connection.requestMethod = method
        connection.connectTimeout = connectTimeout
        connection.readTimeout = readTimeout

        return getResponse(connection, method, postData)?.let { JSONObject(it) }
    }

    private fun getResponse(connection: URLConnection, method: String, postData: String): String? {
        return try {
            if (method != "GET") {
                connection.doOutput = true
                connection.doInput = true
                val outputStream = connection.getOutputStream()
                outputStream.write(postData.toByteArray(Charsets.UTF_8))
                outputStream.flush()
                outputStream.close()
            }

            val (responseCode, responseMessage) = when (connection) {
                is HttpURLConnection -> connection.responseCode to connection.responseMessage
                is HttpsURLConnection -> connection.responseCode to connection.responseMessage
                else -> return null
            }
            val content = if (responseCode == HttpURLConnection.HTTP_OK) {
                val streamReader = InputStreamReader(connection.inputStream)
                val buffered = BufferedReader(streamReader)
                val content = StringBuilder()
                while (true) {
                    val line = buffered.readLine() ?: break
                    content.append(line)
                }
                buffered.close()
                content.toString()
            } else {
                responseMessage
            }

            Log.e(
                "BJH",
                "[NetworkModule][getResponse] responseCode=$responseCode\ncontent=$content"
            )
            when (connection) {
                is HttpURLConnection -> connection.disconnect()
                is HttpsURLConnection -> connection.disconnect()
            }
            content
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getImage(uri: String, connectTimeout: Int = 1000): Bitmap? {
        return try {
            val url = URL(uri)
            val connection = url.openConnection() as HttpsURLConnection
            connection.connectTimeout = connectTimeout
            val bis = BufferedInputStream(connection.getInputStream())
            val bm = BitmapFactory.decodeStream(bis);
            bis.close()
            bm
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun readStream(inputStream: BufferedInputStream): String {
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        bufferedReader.forEachLine { stringBuilder.append(it) }
        return stringBuilder.toString()
    }
}

enum class Protocol {
    HTTP, HTTPS
}