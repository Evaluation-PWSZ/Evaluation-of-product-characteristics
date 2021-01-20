package pl.memstacja.bottomnavigation.ui.dashboard.parts

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.pwszproducts.myapplication.data.model.StaticUserData
import pl.memstacja.bottomnavigation.R
import pl.memstacja.bottomnavigation.data.model.dashboard.DegustationItem
import pl.memstacja.bottomnavigation.data.model.dashboard.ProductItem
import pl.memstacja.bottomnavigation.ui.Creators.PartsCreateActivity
import pl.memstacja.bottomnavigation.ui.dashboard.DegustationList
import pl.memstacja.bottomnavigation.ui.dashboard.features.FeaturesProductOpen

class PartsList: ArrayList<ProductItem>()

class PartsOpen : AppCompatActivity() {

    private var CREATE = 1
    private var UPDATE_DELETE = 2

    lateinit var partsViewModel: PartsViewModel
    //var tmpId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        title = "Produkty"
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parts)

        partsViewModel = ViewModelProvider(this).get(PartsViewModel::class.java)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id: Int = intent.getIntExtra("id", 0)
        val name: String? = intent.getStringExtra("listName")
        val description: String? = intent.getStringExtra("listDescription")

        val buttonCreate = findViewById<FloatingActionButton>(R.id.add_product)
        val buttonGoFeatures = findViewById<Button>(R.id.go_features)

        buttonCreate.setOnClickListener {
            val intent = Intent(this, PartsCreateActivity::class.java)
            intent.putExtra("id", id)
            startActivityForResult(intent, CREATE)
        }

        buttonGoFeatures.setOnClickListener {
            val intent = Intent(this, FeaturesProductOpen::class.java)
            intent.putExtra("id", id)
            intent.putExtra("degustation_name", name)
            startActivity(intent)
        }

        findViewById<TextView>(R.id.listName).text = name
        findViewById<TextView>(R.id.listDescription).text = description

        Log.d("ACTIVE_APP", "$name $description")

        val featuresList = findViewById<RecyclerView>(R.id.viewGroup)

        setToList(featuresList)
    }

    fun downloadList() {
        val url = "https://rate.kamilcraft.com/api/degustations/${intent.getIntExtra("id", 0)}/products"

        val stringRequest = object: JsonArrayRequest(
                Request.Method.GET, url, null,
                {
                    val gson = Gson()
                    val degustationList: PartsList = gson.fromJson(it.toString(), PartsList::class.java)
                    Log.d("LIST", "Status TRUE");
                    for(elementList in degustationList) {
                        elementList.degustation_id = intent.getIntExtra("id", 0)
                        Log.d("LIST", "Loaded: ${elementList.id}, ${elementList.degustation_id}")
                        partsViewModel.addToAdapter(elementList)
                    }
                    Log.d("CONNECT", "OK")
                },
                {
                    Log.d("CONNECT", "NOT OK, ${it.toString()}")
                    Toast.makeText(this@PartsOpen,
                            "Wystąpił błąd podczas pobierania danych!",
                            Toast.LENGTH_LONG).show()
                    var errorMessage: String = ""
                    if(it.networkResponse.data != null)
                        errorMessage = it.networkResponse.data.toString()
                    Log.d("LIST", "Error: $errorMessage")
                    Toast.makeText(this@PartsOpen,
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

        Volley.newRequestQueue(this@PartsOpen).add(stringRequest)
    }

    private fun setToList(recyclerView: RecyclerView) {
        downloadList()
        recyclerView.adapter = partsViewModel.getAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this@PartsOpen)
        recyclerView.setHasFixedSize(true)
    }

    fun createList(data: Intent?) {
        val id: Int = data!!.getIntExtra("id", 0)
        val name: String = data.getStringExtra("name").toString()

        partsViewModel.addToAdapter(
                ProductItem(id = id, name = name)
        )

        Toast.makeText(this,
                "Utworzono!",
                Toast.LENGTH_LONG).show()
    }

    fun updateList(data: Intent?) {
        val id: Int = data!!.getIntExtra("id", 0)
        val name: String = data.getStringExtra("name").toString()

        partsViewModel.getAdapter().updateInList(id, name)

        Toast.makeText(this,
                "Dokonano aktualizacji produktu!",
                Toast.LENGTH_LONG).show()
    }

    fun removeWithList(data: Intent?) {
        val id: Int = data!!.getIntExtra("id", 0)

        partsViewModel.getAdapter().removeWithList(id)

        Toast.makeText(this,
                "Usunięto produkt!",
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