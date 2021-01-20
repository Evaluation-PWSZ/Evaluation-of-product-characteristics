package pl.memstacja.bottomnavigation.ui.dashboard.parts

import androidx.lifecycle.ViewModel
import pl.memstacja.bottomnavigation.data.model.dashboard.ProductItem
import java.util.ArrayList

class PartsViewModel: ViewModel() {
    companion object {
        private var adapter: PartsAdapter = PartsAdapter(ArrayList())

        fun resetAdapter() {
            adapter.resetAdapter()
        }

        fun getAdapter(): PartsAdapter {
            return adapter
        }
    }

    fun addToAdapter(listItem: ProductItem, orderBy: String = "ASC") {
        if(orderBy == "ASC")
            adapter.addToList(listItem)
        if(orderBy == "DESC")
            adapter.addToListDesc(listItem)
    }

    fun getAdapter(): PartsAdapter {
        return adapter
    }
}