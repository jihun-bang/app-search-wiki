package jihun.bang.searchwiki

import android.app.SearchManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import jihun.bang.searchwiki.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val viewModel = SearchViewModel()
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater).apply { setContentView(this.root) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.menu_search).actionView as SearchView).apply {
            setOnQueryTextListener(object : OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    return when {
                        query.isNullOrBlank() -> false
                        else -> {
                            Log.e("BJH", "[onQueryTextSubmit] query=$query")
                            onActionViewCollapsed()
                            viewModel.getDetail(query, false)
                            true
                        }
                    }
                }
            })
        }
        return super.onCreateOptionsMenu(menu)
    }
}