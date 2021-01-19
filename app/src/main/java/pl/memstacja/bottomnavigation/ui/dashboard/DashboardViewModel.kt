package pl.memstacja.bottomnavigation.ui.dashboard

import androidx.lifecycle.ViewModel
import pl.memstacja.bottomnavigation.data.model.dashboard.DegustationItem
import java.util.ArrayList

class DashboardViewModel : ViewModel() {

    private var adapter: DashboardAdapter = DashboardAdapter(generateEmptyList())

    fun addToAdapter(listItem: DegustationItem) {
        adapter.addToList(listItem)
    }

    fun getAdapter(): DashboardAdapter {
        return adapter
    }

    fun generateEmptyList(): MutableList<DegustationItem> {
        return ArrayList<DegustationItem>()
    }

    /*private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text*/
}