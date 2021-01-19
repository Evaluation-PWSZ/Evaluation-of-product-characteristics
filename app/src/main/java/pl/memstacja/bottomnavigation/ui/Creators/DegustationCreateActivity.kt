package pl.memstacja.bottomnavigation.ui.Creators

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.pwszproducts.myapplication.data.model.StaticUserData
import org.json.JSONObject
import pl.memstacja.bottomnavigation.R
import pl.memstacja.bottomnavigation.data.model.dashboard.DegustationItem

class DegustationCreateActivity: AppCompatActivity() {

    private lateinit var name : EditText
    private lateinit var description : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_create)

        name = findViewById(R.id.name_text)
        description = findViewById(R.id.description_text)

        val buttonAdd = findViewById<Button>(R.id.add_degustation)

        buttonAdd.setOnClickListener {
            comebackToParent()
        }
    }

    fun sendRequest() {
        val url = "https://rate.kamilcraft.com/api/degustations"

        val params: MutableMap<String, String> = HashMap()
        params["name"] = String(name.text.toString().toByteArray(), Charsets.UTF_8)
        params["description"] = String(description.text.toString().toByteArray(), Charsets.UTF_8)
        val jsonObject = JSONObject(params as Map<*, *>)

        val stringRequest = object: JsonObjectRequest(
            Method.POST, url, jsonObject,
            {
                val gson = Gson()
                val degustation: DegustationItem = gson.fromJson(it.toString(), DegustationItem::class.java)

                val intent = Intent()
                intent.putExtra("id", degustation.id)
                intent.putExtra("name", name.text.toString())
                intent.putExtra("description", description.text.toString())

                setResult(Activity.RESULT_OK, intent)
                finish()
                Log.d("CONNECT", "OK")
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