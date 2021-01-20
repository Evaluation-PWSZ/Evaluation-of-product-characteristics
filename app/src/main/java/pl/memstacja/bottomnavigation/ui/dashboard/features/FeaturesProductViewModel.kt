package pl.memstacja.bottomnavigation.ui.dashboard.features

import androidx.lifecycle.ViewModel
import pl.memstacja.bottomnavigation.data.model.dashboard.FeatureItem
import pl.memstacja.bottomnavigation.data.model.dashboard.ProductItem
import java.util.ArrayList

class FeaturesProductViewModel: ViewModel() {

    private var productAdapter: FeaturesProductAdapter = FeaturesProductAdapter(ArrayList())

    fun addToAdapter(listItem: FeatureItem, orderBy: String = "ASC") {
        if(orderBy == "ASC")
            productAdapter.addToList(listItem)
        if(orderBy == "DESC")
            productAdapter.addToListDesc(listItem)
    }

    fun getAdapter(): FeaturesProductAdapter {
        return productAdapter
    }

}