package jihun.bang.searchwiki.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import jihun.bang.searchwiki.R
import jihun.bang.searchwiki.databinding.ActivityMainBinding
import jihun.bang.searchwiki.network.NetworkModule
import jihun.bang.searchwiki.network.Protocol
import jihun.bang.searchwiki.viewModels.SearchViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<SearchViewModel>()
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater).apply { setContentView(root) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        with(binding) {
            viewSearch.apply {
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextChange(newText: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return when {
                            query.isNullOrBlank() -> false
                            else -> {
                                startSearch(context, query)
                                clearFocus()
                                true
                            }
                        }
                    }
                })
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.viewSearch.clearFocus()
    }

    private fun startSearch(context: Context, query: String) {
        val intent = Intent(context, SearchActivity::class.java)
        intent.putExtra("query", query)
        ContextCompat.startActivity(context, intent, null)
        viewModel.search(query)
    }
}