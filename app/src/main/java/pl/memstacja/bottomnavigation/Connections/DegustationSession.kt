package pl.memstacja.bottomnavigation.Connections

import android.content.Context
import android.widget.Toast
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.pwszproducts.myapplication.data.model.StaticUserData
import kotlinx.coroutines.delay
import pl.memstacja.bottomnavigation.data.model.dashboard.DegustationItem

data class DegustationSessionStatus(
    var statusError: Boolean? = false,
    var messageError: String? = "",
    var resultData: Any? = null
)

class DegustationSession(private var context: Context) {

    private var token: String = StaticUserData.token.token
    private var connect = Volley.newRequestQueue(context)
    private lateinit var resultData: DegustationSessionStatus

    fun downloadDegustation(degustation_id: Int) {
        val url = "https://rate.kamilcraft.com/api/degustations/$degustation_id"

        val stringRequest = object: JsonObjectRequest(
                Method.GET, url, null,
                {
                    val gson = Gson()
                    val degustation: DegustationItem  = gson.fromJson(
                            it.toString(), DegustationItem::class.java)

                    resultData = DegustationSessionStatus(resultData = degustation)
                },
                {
                    val errorMessage: String = it.networkResponse.data.toString()
                    resultData = DegustationSessionStatus(
                            true,
                            errorMessage
                    )
                }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token"
                headers["Accept"] = "application/json"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        connect.add(stringRequest)
    }

    suspend fun getResultData(): DegustationSessionStatus {
        delay(1000L)
        return resultData
    }
}