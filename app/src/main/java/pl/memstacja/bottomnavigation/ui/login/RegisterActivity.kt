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

data class RegisterErrors (
        var login: List<String>? = null,
        var email: List<String>? = null,
        var password: List<String>? = null
)

data class resultErrorRegisterMessage(
        var message: String,
        var errors: RegisterErrors? = null
)

class RegisterActivity: AppCompatActivity() {

    private lateinit var submitButton: Button

    private lateinit var loginText: TextView
    private lateinit var loginError: TextView

    private lateinit var emailText: TextView
    private lateinit var emailError: TextView

    private lateinit var passwordText: TextView
    private lateinit var passwordError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        loginText = findViewById<Button>(R.id.login)
        loginError = findViewById<Button>(R.id.login_error)
        emailText = findViewById<Button>(R.id.email)
        emailError = findViewById<Button>(R.id.email_error)
        passwordText = findViewById<Button>(R.id.password)
        passwordError = findViewById<Button>(R.id.password_error)

        submitButton = findViewById(R.id.submit)

        submitButton.setOnClickListener {
            registerInSystem()
        }
    }

    fun registerOK() {
        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
    }

    fun registerInSystem() {
        val url = "https://rate.kamilcraft.com/api/register"

        val params: MutableMap<String, String> = HashMap()
        params["login"] = loginText.text.toString()
        params["email"] = emailText.text.toString()
        params["password"] = passwordText.text.toString()

        val jsonObject = JSONObject(params as Map<*, *>)

        val stringRequest = object: JsonObjectRequest(Method.POST, url, jsonObject,
                { response ->
                    loginError.text = ""
                    emailError.text = ""
                    passwordError.text = ""

                    val gson = Gson()
                    val myToken = gson.fromJson(response.toString(), ResultLogin::class.java)

                    StaticUserData.token = myToken

                    getUser(myToken.token)
                },
                {
                    val statusCode = it.networkResponse.statusCode
                    val response = String(it.networkResponse.data, Charsets.UTF_8)

                    val result = Gson().fromJson(response, resultErrorRegisterMessage::class.java)

                    val errors = result.errors
                    if(errors != null) {
                        val login = errors.login
                        val email = errors.email
                        val password = errors.password

                        if(login != null)
                            loginError.text = login.first().toString()
                        else
                            loginError.text = ""

                        if(email != null)
                            emailError.text = email.first().toString()
                        else
                            emailError.text = ""

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
                    registerOK()
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

}