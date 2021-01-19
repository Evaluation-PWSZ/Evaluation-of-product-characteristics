package pl.memstacja.bottomnavigation.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.pwszproducts.myapplication.data.model.ResultLogin
import com.pwszproducts.myapplication.data.model.ResultUser
import com.pwszproducts.myapplication.data.model.StaticUserData
import org.json.JSONObject
import pl.memstacja.bottomnavigation.R

data class Errors (
        var login: List<String>? = null,
        var password: List<String>? = null
    )

data class resultErrorMessage(
        var message: String,
        var errors: Errors? = null
    )

class LoginActivity: AppCompatActivity() {

    private val REGISTERED = 1

    private lateinit var loginText: TextView
    private lateinit var loginError: TextView
    private lateinit var passwordText: TextView
    private lateinit var passwordError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginText = findViewById<Button>(R.id.login)
        loginError = findViewById<Button>(R.id.login_error)
        passwordText = findViewById<Button>(R.id.password)
        passwordError = findViewById<Button>(R.id.password_error)

        val buttonLogin = findViewById<Button>(R.id.sign_in)
        val buttonRegister = findViewById<Button>(R.id.register_click)

        buttonLogin.isEnabled = true

        buttonRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivityForResult(intent, REGISTERED)
        }

        buttonLogin.setOnClickListener {
            loginToSystem()
        }
    }

    fun loginToSystem() {
        val url = "https://rate.kamilcraft.com/api/login"

        val params: MutableMap<String, String> = HashMap()
        params["login"] = loginText.text.toString()
        params["password"] = passwordText.text.toString()

        val jsonObject = JSONObject(params as Map<*, *>)

        val stringRequest = object: JsonObjectRequest(Method.POST, url, jsonObject,
                { response ->
                    loginError.text = ""
                    passwordError.text = ""

                    val gson = Gson()
                    val myToken = gson.fromJson(response.toString(), ResultLogin::class.java)

                    StaticUserData.token = myToken

                    getUser(myToken.token)
                },
                {
                    val statusCode = it.networkResponse.statusCode
                    val response = String(it.networkResponse.data, Charsets.UTF_8)

                    val result = Gson().fromJson(response, resultErrorMessage::class.java)

                    val errors = result.errors
                    if(errors != null) {
                        val login = errors.login
                        val password = errors.password

                        if(login != null)
                            loginError.text = login.first().toString()
                        else
                            loginError.text = ""

                        if(password != null)
                            passwordError.text = password.first().toString()
                        else
                            passwordError.text = ""
                    }

                    Toast.makeText(this,
                            result.message,
                            Toast.LENGTH_SHORT).show()
                }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        Volley.newRequestQueue(this).add(stringRequest)
    }

    fun getUser(token: String) {
        val url = "https://rate.kamilcraft.com/api/user"

        val stringRequest = object: JsonObjectRequest(Method.GET, url, null,
                { response ->
                    val gson = Gson()
                    val user = gson.fromJson(response.toString(), ResultUser::class.java)

                    StaticUserData.user = user

                    Toast.makeText(this,
                            "Witaj ${StaticUserData.user.login}!",
                            Toast.LENGTH_SHORT).show()
                    openListActivity()
                },
                {
                    Toast.makeText(this,
                            "Wystąpił błąd podczas logowania! Spróbuj ponownie później",
                            Toast.LENGTH_SHORT).show()
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

    fun openListActivity() {
        /*val intent = Intent(this, ListActivity::class.java)
        startActivity(intent)*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REGISTERED && resultCode == RESULT_OK) {
            Toast.makeText(this, "Witaj ${StaticUserData.user.login}!",
                    Toast.LENGTH_SHORT).show()
            openListActivity()
        }
    }
}