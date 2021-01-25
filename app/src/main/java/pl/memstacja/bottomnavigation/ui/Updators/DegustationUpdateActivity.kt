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
import pl.memstacja.bottomnavigation.data.model.dashboard.DegustationItem
import pl.memstacja.bottomnavigation.ui.dashboard.DashboardViewModel

class DegustationUpdateActivity: AppCompatActivity() {

    private lateinit var name : TextView
    private lateinit var description : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_update)

        name = findViewById(R.id.name_text)
        description = findViewById(R.id.description_text)

        if(intent.hasExtra("name"))
            name.text = intent.getStringExtra("name")
        if(intent.hasExtra("description"))
            description.text = intent.getStringExtra("description")

        val buttonAdd = findViewById<Button>(R.id.update_degustation)
        val buttonRemove = findViewById<Button>(R.id.remove_degustation)

        buttonAdd.setOnClickListener {
            comebackToParent()
        }

        buttonRemove.setOnClickListener {
            sendRemoveRequest()
        }
    }

    fun sendRemoveRequest() {
        val url = "https://rate.kamilcraft.com/api/degustations/${intent.getIntExtra("id", 0)}"

        val stringRequest = object: StringRequest(
            Method.DELETE, url,
            {
                Log.d("CONNECT", "OK")

                DashboardViewModel.getAdapter().removeWithList(
                    intent.getIntExtra("id", 0)
                )

                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("id", intent.getIntExtra("id", 0))
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

    fun resultSuccessfull(jsonObject: JSONObject) {
        Log.d("CONNECT", "OK")

        DashboardViewModel.getAdapter().updateInList(
            DegustationItem(
                intent.getIntExtra("id", 0),
                name.text.toString(),
                description.text.toString()
            )
        )

        val intent = Intent()
        intent.putExtra("id", intent.getIntExtra("id", 0))
        intent.putExtra("name", name.text.toString())
        intent.putExtra("description", description.text.toString())
        intent.putExtra("type", "update")
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    fun sendRequest() {
        val url = "https://rate.kamilcraft.com/api/degustations/${intent.getIntExtra("id", 0)}"

        val params: MutableMap<String, String> = HashMap()
        params["name"] = String(name.text.toString().toByteArray(), Charsets.UTF_8)
        params["description"] = String(description.text.toString().toByteArray(), Charsets.UTF_8)
        val jsonObject = JSONObject(params as Map<*, *>)

        val stringRequest = object: JsonObjectRequest(
            Method.PUT, url, jsonObject,
            {
                resultSuccessfull(it)
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