package jihun.bang.searchwiki.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jihun.bang.searchwiki.data.WikiModel
import jihun.bang.searchwiki.network.WikiApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class SearchViewModel : ViewModel() {
    private val _summary = MutableLiveData<WikiModel>()
    val summary: LiveData<WikiModel>
        get() = _summary

    private val _related = MutableLiveData<List<WikiModel>>()
    val related: LiveData<List<WikiModel>>
        get() = _related

    fun search(word: String) {
        viewModelScope.launch(Dispatchers.IO) {
            WikiApi.getSummary(word).let {
                _summary.postValue(it)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            WikiApi.getRelated(word).let {
                it.collect {
                    _related.postValue(it)
                }
            }
        }
    }
}