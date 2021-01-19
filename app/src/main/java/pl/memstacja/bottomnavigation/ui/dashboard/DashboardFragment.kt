package pl.memstacja.bottomnavigation.ui.dashboard

import android.app.Activity
import android.content.Intent
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.pwszproducts.myapplication.data.model.StaticUserData
import pl.memstacja.bottomnavigation.R
import pl.memstacja.bottomnavigation.data.model.dashboard.DegustationItem
import pl.memstacja.bottomnavigation.ui.Creators.DegustationCreateActivity

class DegustationList: ArrayList<DegustationItem>()

class DashboardFragment : Fragment() {

    private var CREATE = 1;
    private var UPDATE = 2;
    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.recycle_view)

        val addButton = root.findViewById<FloatingActionButton>(R.id.add_degustation)
        addButton.setOnClickListener {
            val intent = Intent(activity, DegustationCreateActivity::class.java)
            startActivityForResult(intent, CREATE)
        }

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
                Log.d("LIST", "Status TRUE");
                for(elementList in degustationList) {
                    Log.d("LIST", "Loaded: ${elementList.id}");
                    dashboardViewModel.addToAdapter(elementList, "DESC")
                }
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

    fun createList(data: Intent?) {
        val id: Int = data!!.getIntExtra("id", 0)
        val name: String = data.getStringExtra("name").toString()
        val description: String = data.getStringExtra("description").toString()

        dashboardViewModel.addToAdapter(DegustationItem(id = id, name = name, description = description))
    }

    fun updateList(data: Intent?) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CREATE && resultCode == Activity.RESULT_OK) {
            createList(data)
        } else if(requestCode == UPDATE) {
            updateList(data)
        }
    }

}