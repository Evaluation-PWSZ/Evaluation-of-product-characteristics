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
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.pwszproducts.myapplication.data.model.StaticUserData
import pl.memstacja.bottomnavigation.R
import pl.memstacja.bottomnavigation.data.model.dashboard.DegustationItem
import pl.memstacja.bottomnavigation.data.model.dashboard.FeatureItem
import pl.memstacja.bottomnavigation.ui.Updators.FeaturesUpdateActivity

class FeatureList: ArrayList<FeatureItem>()

class FeaturesOpen : AppCompatActivity() {
    var name: String? = null
    var degustation_id: Int? = null
    var tmpId: Int = 0

    var list = ArrayList<FeatureItem>()
    lateinit var featuresList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        title = "Ocena cech produktu"
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_features)

        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        name = intent.getStringExtra("productName")
        degustation_id = intent.getIntExtra("degustation_id", 0)

        findViewById<TextView>(R.id.productName).text = name

        Log.d("ACTIVE_APP", "$name")

        featuresList = findViewById<RecyclerView>(R.id.viewGroup)

        downloadList()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun downloadList() {
        val url = "https://rate.kamilcraft.com/api/degustations/$degustation_id/features"

        val stringRequest = object: JsonArrayRequest(
                Request.Method.GET, url, null,
                {
                    val gson = Gson()
                    val featureList: FeatureList = gson.fromJson(it.toString(), FeatureList::class.java)
                    Log.d("LIST", "Status TRUE");
                    list = featureList
                    Log.d("CONNECT", "OK")
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
                headers["Authorization"] = "Bearer ${StaticUserData.token.token}"
                headers["Accept"] = "application/json"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        Volley.newRequestQueue(this).add(stringRequest)
    }

    private fun setToList() {
        featuresList.adapter = FeaturesAdapter(list.reversed())
        featuresList.layoutManager = LinearLayoutManager(this)
        featuresList.setHasFixedSize(true)
    }
}