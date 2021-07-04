package jihun.bang.searchwiki.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import jihun.bang.searchwiki.R
import jihun.bang.searchwiki.databinding.ActivityWebBinding

class WebActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityWebBinding.inflate(layoutInflater).apply { setContentView(root) }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        val title = intent.getStringExtra("query")

        supportActionBar?.run {
            this.title = title
        }
        with(binding.viewWeb) {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            loadUrl("https://en.wikipedia.org/api/rest_v1/page/html/$title")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}