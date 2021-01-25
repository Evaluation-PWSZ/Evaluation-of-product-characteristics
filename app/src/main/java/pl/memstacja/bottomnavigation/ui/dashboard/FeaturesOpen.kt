package pl.memstacja.bottomnavigation.ui.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.pwszproducts.myapplication.data.model.StaticUserData
import pl.memstacja.bottomnavigation.Connections.DegustationSession
import pl.memstacja.bottomnavigation.R
import pl.memstacja.bottomnavigation.data.model.dashboard.DegustationItem
import pl.memstacja.bottomnavigation.data.model.dashboard.FeatureItem
import pl.memstacja.bottomnavigation.data.model.dashboard.ProductItem
import pl.memstacja.bottomnavigation.ui.Updators.FeaturesUpdateActivity
import kotlin.properties.Delegates

class FeatureList: ArrayList<FeatureItem>()

class FeaturesOpen : AppCompatActivity() {

    private lateinit var degustationSession: DegustationSession

    private var degustation_id: Int = 0
    private var product_id: Int = 0
    private var token: String = ""

    private var degustation = DegustationItem(0)
    private var product = ProductItem(0, "")
    private var featureList = ArrayList<FeatureItem>()

    private lateinit var degustationName: TextView
    private lateinit var productName: TextView
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        title = "Oceń produkt!"
        token = "${StaticUserData.token.token}"
        Log.d("TOKEN", "$token")

        degustationSession = DegustationSession(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_features)

        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if(intent.hasExtra("degustation_id")) {
            degustation_id = intent.getIntExtra("degustation_id", 0)
        }
        if(intent.hasExtra("product_id")) {
            product_id = intent.getIntExtra("product_id", 0)
        }

        degustationName = findViewById(R.id.degustationName)
        productName = findViewById(R.id.productName)
        recyclerView = findViewById(R.id.viewGroup)

        if(intent.hasExtra("productName")) {
            productName.text = intent.getStringExtra("productName")
        }

        downloadDegustation()
        downloadProduct()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun downloadDegustation() {
        val url = "https://rate.kamilcraft.com/api/degustations/$degustation_id"

        val stringRequest = object: JsonObjectRequest(
                Method.GET, url, null,
                {
                    val gson = Gson()
                    degustation  = gson.fromJson(it.toString(), DegustationItem::class.java)
                    degustationName.text = degustation.name

                    downloadFeatures()
                },
                {
                    var errorMessage: String = it.networkResponse.data.toString()
                    Toast.makeText(this,
                            "Wystąpił błąd podczas pobierania danych! ${errorMessage}",
                            Toast.LENGTH_LONG).show()
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

        Volley.newRequestQueue(this).add(stringRequest)
    }

    fun downloadProduct() {
        val url = "https://rate.kamilcraft.com/api/degustations/$degustation_id/products/$product_id"

        val stringRequest = object: JsonObjectRequest(
                Method.GET, url, null,
                {
                    val gson = Gson()
                    product  = gson.fromJson(it.toString(), ProductItem::class.java)
                    productName.text = product.name
                },
                {
                    var errorMessage: String = it.networkResponse.data.toString()
                    Toast.makeText(this,
                            "Wystąpił błąd podczas pobierania danych! ${errorMessage}",
                            Toast.LENGTH_LONG).show()
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

        Volley.newRequestQueue(this).add(stringRequest)
    }

    fun downloadFeatures() {
        val url = "https://rate.kamilcraft.com/api/degustations/$degustation_id/features"

        val stringRequest = object: JsonArrayRequest(
                Method.GET, url, null,
                {
                    val gson = Gson()
                    featureList = gson.fromJson(it.toString(), FeatureList::class.java)
                    setToList()
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
                headers["Authorization"] = "Bearer $token"
                headers["Accept"] = "application/json"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        Volley.newRequestQueue(this).add(stringRequest)
    }

    private fun setToList() {
        recyclerView.adapter = FeaturesAdapter(featureList.reversed())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
    }
}