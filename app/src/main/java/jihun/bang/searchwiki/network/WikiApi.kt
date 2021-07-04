package jihun.bang.searchwiki.network

import android.graphics.Bitmap
import jihun.bang.searchwiki.data.WikiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject

object WikiApi {
    fun getSummary(title: String): WikiModel? {
        NetworkModule.setConnection(api = "summary/$title")?.let { json ->
            val thumbnail = getImage(json)

            return WikiModel(
                title = json.getString("displaytitle"),
                content = json.getString("extract_html"),
                thumbnail = thumbnail
            )
        }
        return null
    }

    fun getRelated(title: String): Flow<List<WikiModel>> = flow {
        NetworkModule.setConnection(api = "related/$title")?.let { json ->
            val pages = json.getJSONArray("pages")
            val models = mutableListOf<WikiModel>()

            for (i in 0 until pages.length()) {
                val page = pages.getJSONObject(i)
                val thumbnail = getImage(page)
                models.add(
                    WikiModel(
                        title = page.getString("title"),
                        content = page.getString("extract"),
                        thumbnail = thumbnail
                    )
                )
                emit(models)
            }
        }
    }

    private fun getImage(json: JSONObject): Bitmap? {
        return when (runCatching { json.getJSONObject("thumbnail") }.isSuccess) {
            true -> NetworkModule.getImage(json.getJSONObject("thumbnail").getString("source"))
            false -> null
        }
    }
}