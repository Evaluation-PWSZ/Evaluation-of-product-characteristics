package pl.memstacja.bottomnavigation.data.model.dashboard

data class ProductItem(
        var id: Int,
        var name: String,
        var degustation_id: Int? = null,
        var created_at: String? = null,
)