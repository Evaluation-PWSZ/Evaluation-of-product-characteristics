package pl.memstacja.bottomnavigation.data.model.dashboard

data class DegustationItem(
    val id: Int,
    var name: String? = null,
    val created_at: String? = null,
    var description: String? = null,
    val invitation_key: String? = null,
    val owner: Owner? = null,
    val owner_id: Int? = null,
    val status: String? = null,
)