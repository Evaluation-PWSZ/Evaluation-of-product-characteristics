package pl.memstacja.bottomnavigation.ui.invitations

import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.pwszproducts.myapplication.data.model.StaticUserData
import kotlinx.coroutines.delay
import pl.memstacja.bottomnavigation.R
import pl.memstacja.bottomnavigation.ui.session.SessionDegustation

data class responseErrorMessage(
    var message: String? = null
)

data class member(
    var id: Int? = null,
    var degustation_id: Int? = null,
    var user_id: Int? = null,
    var created_at: String? = null,
    var updated_at: String? = null
)

data class resultOK(
    var message: String? = null,
    var member: member? = null
)

class InvitationsFragment : Fragment() {

    private lateinit var invitationsViewModel: InvitationsViewModel

    private lateinit var invitationKey: TextView
    private lateinit var errorMessage: TextView
    private lateinit var okMessage: TextView
    private lateinit var startSession: Button

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        invitationsViewModel =
                ViewModelProvider(this).get(InvitationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_invitations, container, false)

        invitationKey = root.findViewById(R.id.invitation)
        errorMessage = root.findViewById(R.id.errorMessage)
        okMessage = root.findViewById(R.id.okMessage)
        startSession = root.findViewById(R.id.start)

        startSession.setOnClickListener {
            checkInvitationKey()
        }

        invitationsViewModel.key.observe(viewLifecycleOwner, Observer {
            invitationKey.text = it
        })
        return root
    }

    fun checkInvitationKey() {

        val invitationKey = invitationKey.text.toString()
        val url = "https://rate.kamilcraft.com/api/invitations/${invitationKey}"

        val stringRequest = object: StringRequest(Method.GET, url,
                { response ->
                    errorMessage.visibility = View.INVISIBLE
                    val result: resultOK = Gson().fromJson(response.toString(), resultOK::class.java)

                    Log.d("RESULT_DATA", "$response; ${result.message.toString()}")

                    okMessage.visibility = View.VISIBLE
                    okMessage.text = result.message.toString()

                    result.member?.degustation_id?.let {
                        startDegustation(it)
                    }
                },
                {
                    if(it.networkResponse.statusCode == 400 || it.networkResponse.statusCode == 404) {
                        val resultErrorMessage = String(it.networkResponse.data, Charsets.UTF_8)
                        val result: responseErrorMessage = Gson().fromJson(resultErrorMessage, responseErrorMessage::class.java)
                        errorMessage.visibility = View.INVISIBLE
                        errorMessage.visibility = View.VISIBLE

                        errorMessage.text = result.message.toString()
                    }
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

        Volley.newRequestQueue(context).add(stringRequest)
    }

    fun startDegustation(degustation_id: Int) {
        val intent = Intent(context, SessionDegustation::class.java)
        intent.putExtra("degustation_id", degustation_id)
        startActivity(intent)
    }
}