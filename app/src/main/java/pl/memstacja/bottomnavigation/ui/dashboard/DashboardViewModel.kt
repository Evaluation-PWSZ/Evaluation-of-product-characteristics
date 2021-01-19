package pl.memstacja.bottomnavigation.ui.dashboard

import androidx.lifecycle.ViewModel
import pl.memstacja.bottomnavigation.data.model.dashboard.DegustationItem
import java.util.ArrayList

class DashboardViewModel : ViewModel() {

    companion object {
        private var adapter: DashboardAdapter = DashboardAdapter(ArrayList())

        fun resetAdapter() {
            adapter.resetAdapter()
        }

        fun getAdapter(): DashboardAdapter {
            return adapter
        }
    }

    fun addToAdapter(listItem: DegustationItem, orderBy: String = "ASC") {
        if(orderBy == "ASC")
            adapter.addToList(listItem)
        if(orderBy == "DESC")
            adapter.addToListDesc(listItem)
    }

    fun getAdapter(): DashboardAdapter {
        return adapter
    }

    /*private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text*/
}