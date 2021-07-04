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
            val responseCode = when (connection) {
                is HttpURLConnection -> connection.responseCode
                is HttpsURLConnection -> connection.responseCode
                else -> null
            }
            if (method != "GET") {
                connection.doOutput = true
                connection.doInput = true
                val outputStream = connection.getOutputStream()
                outputStream.write(postData.toByteArray(Charsets.UTF_8))
                outputStream.flush()
                outputStream.close()
            }

            val streamReader = InputStreamReader(connection.inputStream)
            val buffered = BufferedReader(streamReader)
            val content = StringBuilder()
            while (true) {
                val line = buffered.readLine() ?: break
                content.append(line)
            }

            buffered.close()

            when (connection) {
                is HttpURLConnection -> connection.disconnect()
                is HttpsURLConnection -> connection.disconnect()
            }
            content.toString().apply {
                Log.e("BJH", "[NetworkModule][getResponse] responseCode=$responseCode\ncontent=$this")
            }
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
}

enum class Protocol {
    HTTP, HTTPS
}