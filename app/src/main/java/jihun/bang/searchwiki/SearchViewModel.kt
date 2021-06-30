package jihun.bang.searchwiki

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SearchViewModel : ViewModel() {
    private val baseUrl = "en.wikipedia.org/api/rest_v1/page/html"
    fun getDetail(word: String, isHttp: Boolean) {
        viewModelScope.launch {
            try {
                Log.e("BJH", "[getDetail] Start")
                val url = URL(
                    when (isHttp) {
                        true -> "http://$baseUrl/$word"
                        false -> "https://$baseUrl/$word"
                    }
                )
                Log.e("BJH", "[getDetail] url=$url")
                val connection = url.openConnection() as HttpURLConnection
                connection.setRequestProperty("GET", "application/xml")
                connection.connectTimeout = 10000

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val streamReader = InputStreamReader(connection.inputStream)
                    val buffered = BufferedReader(streamReader)

                    val content = StringBuilder()
                    while (true) {
                        val line = buffered.readLine() ?: break
                        content.append(line)
                    }

                    Log.e("BJH", "[getDetail] content=$content")
                    buffered.close()
                    connection.disconnect()
                } else {
                    Log.e("BJH", "[getDetail] connection.responseCode=${connection.responseCode}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}