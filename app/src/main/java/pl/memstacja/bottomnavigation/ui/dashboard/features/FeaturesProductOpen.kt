package pl.memstacja.bottomnavigation.ui.dashboard.features

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.pwszproducts.myapplication.data.model.StaticUserData
import pl.memstacja.bottomnavigation.R
import pl.memstacja.bottomnavigation.data.model.dashboard.FeatureItem
import pl.memstacja.bottomnavigation.ui.Creators.FeaturesCreateActivity
import pl.memstacja.bottomnavigation.ui.Creators.PartsCreateActivity

class FeaturesList: ArrayList<FeatureItem>()

class FeaturesProductOpen : AppCompatActivity() {

    private var CREATE = 1
    private var UPDATE_DELETE = 2

    lateinit var featuresProductViewModel: FeaturesProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        title = "Cechy"
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_features2)

        featuresProductViewModel = ViewModelProvider(this).get(FeaturesProductViewModel::class.java)

        val id: Int = intent.getIntExtra("id", 0)
        val name: String? = intent.getStringExtra("name")
        val degustationName: String? = intent.getStringExtra("degustation_name")

        val buttonCreate = findViewById<FloatingActionButton>(R.id.add_features)

        buttonCreate.setOnClickListener {
            val intent = Intent(this, FeaturesCreateActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("name", name)
            startActivityForResult(intent, CREATE)
        }

        findViewById<TextView>(R.id.degustation_name).text =  degustationName

        Log.d("ACTIVE_APP", "$name")

        val featuresList = findViewById<RecyclerView>(R.id.viewGroup)

        setToList(featuresList)
    }

    fun downloadList() {

        val id: Int = intent.getIntExtra("id", 0)
        Log.d("DEGUSTATION_ID", "Id: $id")
        val url = "https://rate.kamilcraft.com/api/degustations/$id/features"

        val stringRequest = object: JsonArrayRequest(
            Method.GET, url, null,
            {
                val gson = Gson()
                val featureList: FeaturesList = gson.fromJson(it.toString(), FeaturesList::class.java)
                Log.d("LIST", "Status TRUE");
                for(elementList in featureList) {
                    Log.d("LIST", "Loaded: ${elementList.id}, ${elementList.degustation_id}")
                    featuresProductViewModel.addToAdapter(elementList, "DESC")
                }
                Log.d("CONNECT", "OK")
            },
            {
                Log.d("CONNECT", "NOT OK, ${it.toString()}")
                Toast.makeText(this,
                    "Wystąpił błąd podczas pobierania danych!",
                    Toast.LENGTH_LONG).show()
                var errorMessage: String = ""
                if(it.networkResponse.data != null)
                    errorMessage = it.networkResponse.data.toString()
                Log.d("LIST", "Error: $errorMessage")
                Toast.makeText(this,
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

        Volley.newRequestQueue(this).add(stringRequest)
    }

    private fun setToList(recyclerView: RecyclerView) {
        downloadList()
        recyclerView.adapter = featuresProductViewModel.getAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
    }

    fun createList(data: Intent?) {
        val id: Int = data!!.getIntExtra("id", 0)
        val degustation_id: Int = data.getIntExtra("degustation_id", 0)
        val name: String = data.getStringExtra("name").toString()

        featuresProductViewModel.addToAdapter(
            FeatureItem(id = id, name = name, degustation_id = degustation_id)
        )

        Toast.makeText(this,
            "Dodano cechę dla produtków!",
            Toast.LENGTH_LONG).show()
    }

    fun updateList(data: Intent?) {
        val id: Int = data!!.getIntExtra("id", 0)
        val name: String = data.getStringExtra("name").toString()

        featuresProductViewModel.getAdapter().updateInList(id, name)

        Toast.makeText(this,
            "Dokonano aktualizacji cechy produktów!",
            Toast.LENGTH_LONG).show()
    }

    fun removeWithList(data: Intent?) {
        val id: Int = data!!.getIntExtra("id", 0)

        featuresProductViewModel.getAdapter().removeWithList(id)

        Toast.makeText(this,
            "Usunięto cechę produktów!",
            Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("EDIT_STATUS", "Loaded: $requestCode, $resultCode")

        if(requestCode == CREATE && resultCode == Activity.RESULT_OK) {
            createList(data)
            Log.d("EDIT_STATUS", "CREATED")
        } else if(requestCode == UPDATE_DELETE &&
            resultCode == Activity.RESULT_OK &&
            data != null && data.hasExtra("type")) {
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