package pl.memstacja.bottomnavigation.Connections

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.pwszproducts.myapplication.data.model.StaticUserData
import pl.memstacja.bottomnavigation.data.model.dashboard.DegustationItem
import pl.memstacja.bottomnavigation.ui.dashboard.DashboardViewModel
import pl.memstacja.bottomnavigation.ui.dashboard.DegustationList

class Dashboard {

    fun downloadList(context: Context, dashboardViewModel: DashboardViewModel) {
        val url = "https://rate.kamilcraft.com/api/degustations"

        val stringRequest = object: JsonArrayRequest(
                Method.GET, url, null,
                {
                    val gson = Gson()
                    val degustationList: DegustationList = gson.fromJson(it.toString(), DegustationList::class.java)
                    Log.d("LIST", "Status TRUE");
                    for(elementList in degustationList) {
                        Log.d("LIST", "Loaded: ${elementList.id}");
                        dashboardViewModel.addToAdapter(elementList, "DESC")
                    }
                    Log.d("CONNECT", "OK")
                },
                {
                    Log.d("CONNECT", "NOT OK, ${it.toString()}")
                    Toast.makeText(context,
                            "Wystąpił błąd podczas pobierania danych!",
                            Toast.LENGTH_LONG).show()
                    var errorMessage: String = ""
                    if(it.networkResponse.data != null)
                        errorMessage = it.networkResponse.data.toString()
                    Log.d("LIST", "Error: $errorMessage")
                    Toast.makeText(context,
                            "Wystąpił błąd podczas pobierania danych! ${errorMessage}",
                            Toast.LENGTH_LONG).show()
                }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer ${StaticUserData.token.token}"
                headers["Accept"] = "application/json"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        Volley.newRequestQueue(context).add(stringRequest)
    }

    fun createList(context: Context, dashboardViewModel: DashboardViewModel, degustationItem: DegustationItem) {
        dashboardViewModel.addToAdapter(degustationItem)
        Toast.makeText(context,
                "Utworzono ${degustationItem.name}!",
                Toast.LENGTH_LONG).show()
    }

    fun updateList(context: Context, dashboardViewModel: DashboardViewModel, degustationItem: DegustationItem) {
        dashboardViewModel.getAdapter().updateInList(degustationItem)

        Toast.makeText(context,
                "Dokonano aktualizacji!",
                Toast.LENGTH_LONG).show()
    }

    fun removeWithList(context: Context, dashboardViewModel: DashboardViewModel, degustationItem: DegustationItem) {
        dashboardViewModel.getAdapter().removeWithList(degustationItem.id)

        Toast.makeText(context,
                "Usunięto!",
                Toast.LENGTH_LONG).show()
    }

}