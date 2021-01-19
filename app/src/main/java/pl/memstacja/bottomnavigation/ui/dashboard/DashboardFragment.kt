package pl.memstacja.bottomnavigation.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.pwszproducts.myapplication.data.model.StaticUserData
import pl.memstacja.bottomnavigation.R
import pl.memstacja.bottomnavigation.data.model.dashboard.DegustationItem

class DegustationList: ArrayList<DegustationItem>()

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val recyclerView: RecyclerView = root.findViewById<RecyclerView>(R.id.recycle_view)

        setToList(recyclerView);

        return root
    }

    private fun setToList(recyclerView: RecyclerView) {
        recyclerView.adapter = dashboardViewModel.getAdapter()
        downloadList()
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
    }

    fun downloadList() {
        val url = "https://rate.kamilcraft.com/api/degustations"

        val stringRequest = object: JsonArrayRequest(
            Request.Method.GET, url, null,
            {
                val gson = Gson()
                val degustationList: DegustationList = gson.fromJson(it.toString(), DegustationList::class.java)
                /*val success = user.success
                val lists = user.lists*/

                Log.d("LIST", "Status TRUE");
                for(elementList in degustationList) {
                    Log.d("LIST", "Loaded: ${elementList.id}");
                    dashboardViewModel.addToAdapter(elementList)
                }
                /*if(viewModel.getAdapter().itemCount > 0) {
                    textMessage.text = ""
                }*/
                Log.d("CONNECT", "OK")
            },
            {
                Log.d("CONNECT", "NOT OK, ${it.toString()}")
                Toast.makeText(activity,
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

        Volley.newRequestQueue(activity).add(stringRequest)
    }

}