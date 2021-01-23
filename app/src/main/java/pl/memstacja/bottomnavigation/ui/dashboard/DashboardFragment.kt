package pl.memstacja.bottomnavigation.ui.dashboard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import pl.memstacja.bottomnavigation.Connections.Dashboard
import pl.memstacja.bottomnavigation.R
import pl.memstacja.bottomnavigation.data.model.dashboard.DegustationItem
import pl.memstacja.bottomnavigation.ui.Creators.DegustationCreateActivity

class DegustationList: ArrayList<DegustationItem>()

class DashboardFragment : Fragment() {

    private var CREATE = 1
    private var firstLoad: Boolean = true
    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        DashboardViewModel.resetAdapter()
        dashboardViewModel =
                ViewModelProvider(this).get(DashboardViewModel::class.java)

        dashboardViewModel.getAdapter().activity = activity as Activity
        dashboardViewModel.getAdapter().context = context as Context

        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.recycle_view)

        val addButton = root.findViewById<FloatingActionButton>(R.id.add_degustation)

        addButton.setOnClickListener {
            val intent = Intent(context, DegustationCreateActivity::class.java)
            startActivityForResult(intent, CREATE)
        }

        setToList(recyclerView)

        return root
    }

    private fun setToList(recyclerView: RecyclerView) {
        if(firstLoad) {
            recyclerView.adapter = dashboardViewModel.getAdapter()
            val dashboardConnection = Dashboard()
            context?.let {
                dashboardConnection.downloadList(it, dashboardViewModel)
            }
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.setHasFixedSize(true)
        }
        firstLoad = false
    }

    fun createList(data: Intent?) {
        val id: Int = data!!.getIntExtra("id", 0)
        val name: String = data.getStringExtra("name").toString()
        val description: String = data.getStringExtra("description").toString()

        val dashboardConnection = Dashboard()
        context?.let {
            dashboardConnection.createList(it, dashboardViewModel, DegustationItem(id, name, description = description))
        }
    }

    fun updateList(data: Intent?) {
        val id: Int = data!!.getIntExtra("id", 0)
        val name: String = data.getStringExtra("name").toString()
        val description: String = data.getStringExtra("description").toString()

        val dashboardConnection = Dashboard()
        context?.let {
            dashboardConnection.createList(it, dashboardViewModel, DegustationItem(id, name, description = description))
        }
    }

    fun removeWithList(data: Intent?) {
        val id: Int = data!!.getIntExtra("id", 0)

        val dashboardConnection = Dashboard()
        context?.let {
            dashboardConnection.createList(it, dashboardViewModel, DegustationItem(id))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CREATE && resultCode == Activity.RESULT_OK) {
            createList(data)
            Log.d("EDIT_STATUS", "CREATED")
        } else if(resultCode == Activity.RESULT_OK && data != null && data.hasExtra("type")) {
            if(data.getStringExtra("type") == "update") {
                updateList(data)
                Log.d("EDIT_STATUS", "UPDATED")
            } else if(data.getStringExtra("type") == "delete") {
                removeWithList(data)
                Log.d("EDIT_STATUS", "DELETED")
            }
        } else {
            Log.d("EDIT_STATUS", "Empty WORLD");
        }
    }

}