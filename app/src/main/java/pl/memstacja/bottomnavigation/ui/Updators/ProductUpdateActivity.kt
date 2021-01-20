package pl.memstacja.bottomnavigation.ui.Updators

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.pwszproducts.myapplication.data.model.StaticUserData
import org.json.JSONObject
import pl.memstacja.bottomnavigation.MainActivity
import pl.memstacja.bottomnavigation.R
import pl.memstacja.bottomnavigation.ui.dashboard.DashboardViewModel
import pl.memstacja.bottomnavigation.ui.dashboard.parts.PartsViewModel

class ProductUpdateActivity: AppCompatActivity() {

    private lateinit var name : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parts_update)

        name = findViewById(R.id.name_text)

        if(intent.hasExtra("name"))
            name.text = intent.getStringExtra("name")

        val buttonAdd = findViewById<Button>(R.id.update_product)
        val buttonRemove = findViewById<Button>(R.id.delete_product)

        buttonAdd.setOnClickListener {
            comebackToParent()
        }

        buttonRemove.setOnClickListener {
            sendRemoveRequest()
        }
    }

    fun sendRemoveRequest() {

        val degustation_id: Int = intent.getIntExtra("degustation_id", 0)
        val id: Int = intent.getIntExtra("id", 0)

        val url = "https://rate.kamilcraft.com/api/degustations/$degustation_id/products/$id"

        val stringRequest = object: StringRequest(
            Method.DELETE, url,
            {
                Log.d("CONNECT", "OK")

                DashboardViewModel.getAdapter().removeWithList(
                    intent.getIntExtra("id", 0)
                )

                val intent = Intent()
                intent.putExtra("id", id)
                intent.putExtra("name", name.text.toString())
                intent.putExtra("type", "delete")
                setResult(Activity.RESULT_OK, intent)
                finish()
            },
            {
                Log.d("CONNECT", "NOT OK, ${it.toString()}")
                Toast.makeText(this,
                    "Wystąpił błąd podczas pobierania danych!",
                    Toast.LENGTH_LONG).show()
                var errorMessage = ""
                if(it.networkResponse.data != null)
                    errorMessage = String(it.networkResponse.data, Charsets.UTF_8)
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

    fun sendRequest() {

        val degustation_id: Int = intent.getIntExtra("degustation_id", 0)
        val id: Int = intent.getIntExtra("id", 0)

        val url = "https://rate.kamilcraft.com/api/degustations/$degustation_id/products/$id"

        val params: MutableMap<String, String> = HashMap()
        params["name"] = String(name.text.toString().toByteArray(), Charsets.UTF_8)
        val jsonObject = JSONObject(params as Map<*, *>)

        val stringRequest = object: JsonObjectRequest(
            Method.PUT, url, jsonObject,
            {
                Log.d("CONNECT", "OK")

                val intent = Intent()
                intent.putExtra("id", id)
                intent.putExtra("name", name.text.toString())
                intent.putExtra("type", "update")

                setResult(Activity.RESULT_OK, intent)
                finish()
            },
            {
                Log.d("CONNECT", "NOT OK, ${it.toString()}")
                Toast.makeText(this,
                    "Wystąpił błąd podczas pobierania danych!",
                    Toast.LENGTH_LONG).show()
                var errorMessage = ""
                if(it.networkResponse.data != null)
                    errorMessage = String(it.networkResponse.data, Charsets.UTF_8)
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

    fun comebackToParent() {
        if(name.text.length in 3..30) {
            sendRequest()
        } else {
            Toast.makeText(this,
                "Tekst musi posiadać przynajmniej 3 znaki i maksymalnie 30"
                , Toast.LENGTH_SHORT).show()
        }
    }

}