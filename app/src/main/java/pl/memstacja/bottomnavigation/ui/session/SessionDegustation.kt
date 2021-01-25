package pl.memstacja.bottomnavigation.ui.session

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.pwszproducts.myapplication.data.model.StaticUserData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.memstacja.bottomnavigation.R
import pl.memstacja.bottomnavigation.data.model.dashboard.DegustationItem
import pl.memstacja.bottomnavigation.data.model.dashboard.FeatureItem
import pl.memstacja.bottomnavigation.data.model.dashboard.ProductItem

data class isStarted( var started: Boolean? = null )

data class feature(
    var id: Int,
    var degustation_id: Int,
    var name: String,
    var rate: Int? = null,
    var created_at: String,
    var updated_at: String,
    var deleted_at: String
)

data class currentProduct(
    var id: Int,
    var degustation_id: Int,
    var name: String,
    var created_at: String,
    var features: MutableList<feature>
)

class SessionDegustation: AppCompatActivity() {

    private var blockComeback = false
    private var firstStart = true

    private var isStartedStatus = false
    private var degustation_id: Int = 0
    private var product_id: Int = 0
    private var token: String = ""

    private var currentProductData: currentProduct? = null

    private var degustation = DegustationItem(0)
    private var product = ProductItem(0, "")
    private lateinit var featureList: MutableList<feature>

    private lateinit var degustationName: TextView
    private lateinit var productName: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var sendButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_degustation)

        token = StaticUserData.token.token

        if(intent.hasExtra("degustation_id")) {
            degustation_id = intent.getIntExtra("degustation_id", 0)
            blockComeback = true
        } else {
            blockComeback = false
        }

        degustationName = findViewById(R.id.degustationName)
        productName = findViewById(R.id.productName)
        recyclerView = findViewById(R.id.viewGroup)
        sendButton = findViewById(R.id.send)

        productName.text = "Czekamy na właściciela."
        recyclerView.visibility = View.INVISIBLE
        sendButton.visibility = View.INVISIBLE

        sendButton.setOnClickListener {
            sendMyRate()
        }

        downloadDegustation()

        if(firstStart) {
            sendReadyToStart()
        }

        runCheckIsStarted()

    }

    fun loadList(features: MutableList<feature>) {
        featureList = features
        recyclerView.adapter = SessionDegustationAdapter(features)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
    }

    override fun onBackPressed() {
        if (blockComeback)
            moveTaskToBack(true)
        else
            super.onBackPressed()
    }

    fun sendMyRate() {
        for (feature in featureList) {
            feature.rate?.let {
                requestRate(feature.id, it)
                Log.d("RATE_SEND", "SENDED: ${feature.id}")
            }
        }

        recyclerView.isEnabled = false
        sendButton.visibility = View.INVISIBLE
    }

    fun requestRate(feature_id: Int, rate_value: Int) {
        val url = "https://rate.kamilcraft.com/api/session/currentProduct/${feature_id}/rate/${rate_value}"

        val stringRequest = object: JsonObjectRequest(
                Method.GET, url, null,
                {
                    Log.d("RATE_SEND", "OK $url")
                },
                {
                    Log.d("RATE_SEND", "ERROR $url")
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

    fun loadCurrentProduct() {
        val url = "https://rate.kamilcraft.com/api/session/currentProduct"

        val stringRequest = object: JsonObjectRequest(
                Method.GET, url, null,
                {
                    val currentProductDataResult: currentProduct = Gson().fromJson(it.toString(), currentProduct::class.java)
                    if(currentProductData == null || currentProductData!!.id != currentProductDataResult.id) {
                        currentProductData = currentProductDataResult
                        productName.text = currentProductData!!.name
                        recyclerView.visibility = View.VISIBLE
                        sendButton.visibility = View.VISIBLE

                        recyclerView.isEnabled = true

                        loadList(currentProductData!!.features)

                        Log.d("WRITE_DATA", "Loaded: ${currentProductData!!.name}")
                    }
                },
                {}
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

    fun runCheckIsStarted() {
        GlobalScope.launch {
            while (true) {
                delay(2000L)
                isStarted()
            }
        }
    }

    fun isStarted() {
        val url = "https://rate.kamilcraft.com/api/session/isStarted"

        val stringRequest = object: JsonObjectRequest(
            Method.GET, url, null,
            {
                val result: isStarted = Gson().fromJson(it.toString(), isStarted::class.java)
                if(!isStartedStatus && result.started == true) {
                    loadCurrentProduct()
                } else if(isStartedStatus && result.started == false) {
                    productName.text = "ZAKOŃCZONO!"
                    blockComeback = false
                } else if(isStartedStatus) {
                    loadCurrentProduct()
                }

                isStartedStatus = result.started == true

                Log.d("START_STATUS", "${isStartedStatus}")
            },
            {

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

    fun sendReadyToStart() {
        val url = "https://rate.kamilcraft.com/api/degustations/$degustation_id/redyToStart"

        val stringRequest = object: JsonObjectRequest(
                Method.GET, url, null,
                {

                },
                {

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

    fun downloadDegustation() {
        val url = "https://rate.kamilcraft.com/api/degustations/$degustation_id"

        val stringRequest = object: JsonObjectRequest(
                Method.GET, url, null,
                {
                    val gson = Gson()
                    degustation  = gson.fromJson(it.toString(), DegustationItem::class.java)
                    degustationName.text = degustation.name
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

}