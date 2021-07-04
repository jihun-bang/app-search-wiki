package jihun.bang.searchwiki.data

import android.graphics.Bitmap

data class WikiModel(
    val title: String,
    val content: String,
    val thumbnail: Bitmap?
)