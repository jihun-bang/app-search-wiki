package jihun.bang.searchwiki.views

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import jihun.bang.searchwiki.R
import jihun.bang.searchwiki.adapters.SearchAdaptor
import jihun.bang.searchwiki.databinding.ActivitySearchBinding
import jihun.bang.searchwiki.databinding.ItemHeaderBinding
import jihun.bang.searchwiki.viewModels.SearchViewModel


class SearchActivity : AppCompatActivity() {
    private val viewModel by viewModels<SearchViewModel>()
    private val binding by lazy {
        ActivitySearchBinding.inflate(layoutInflater).apply { setContentView(root) }
    }
    private val headerBinding by lazy {
        ItemHeaderBinding.inflate(
            LayoutInflater.from(baseContext), binding.root, false
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val adapter = SearchAdaptor()
        val title = intent.getStringExtra("query")?.apply {
            viewModel.search(this)
        } ?: ""

        supportActionBar?.run {
            this.title = title
        }
        with(binding) {
            listview.addHeaderView(headerBinding.root)
            listview.adapter = adapter
            listview.setOnItemClickListener { _, _, position, id ->
                startSearch(SearchActivity::class.java, adapter.items[id.toInt()].title)
            }
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.search(title)
                swipeRefreshLayout.isRefreshing = false
            }
        }
        with(headerBinding) {
            root.setOnClickListener {
                startWeb(WebActivity::class.java, title)
            }
        }
        with(viewModel) {
            summary.observe(this@SearchActivity) {
                with(headerBinding) {
                    item = it
                    textSubTitle.text = Html.fromHtml(it.content)
                    it.thumbnail?.let {
                        imageTitle.setImageBitmap(it)
                    } ?: kotlin.run {
                        imageTitle.setImageDrawable(null)
                    }
                }
            }
            related.observe(this@SearchActivity) {
                adapter.items = it
                adapter.notifyDataSetChanged()
            }
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

    private fun startWeb(activity: Class<WebActivity>, query: String) {
        val intent = Intent(baseContext, activity)
        intent.putExtra("query", query)
        startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK))
    }

    private fun startSearch(activity: Class<SearchActivity>, query: String) {
        val intent = Intent(baseContext, activity)
        intent.putExtra("query", query)
        startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK))
    }
}