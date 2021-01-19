package pl.memstacja.bottomnavigation.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import pl.memstacja.bottomnavigation.R

class AccountFragment : Fragment() {

    private lateinit var accountViewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        accountViewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_account, container, false)

        val loginView: TextView = root.findViewById(R.id.login_text)
        val emailView: TextView = root.findViewById(R.id.email_text)

        accountViewModel.login.observe(viewLifecycleOwner, Observer {
            loginView.text = it
        })

        accountViewModel.email.observe(viewLifecycleOwner, Observer {
            emailView.text = it
        })

        return root
    }
}