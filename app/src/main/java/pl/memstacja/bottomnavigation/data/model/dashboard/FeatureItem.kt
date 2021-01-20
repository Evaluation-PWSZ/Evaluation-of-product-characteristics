package pl.memstacja.bottomnavigation.data.model.dashboard

data class FeatureItem(
        var id: Int,
        var degustation_id: Int? = null,
        var name: String,
        var created_at: String? = null,
        var updated_at: String? = null,
        var deleted_at: String? = null
)