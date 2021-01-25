package pl.memstacja.bottomnavigation.ui.invitations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import pl.memstacja.bottomnavigation.R

class InvitationsFragment : Fragment() {

    private lateinit var invitationsViewModel: InvitationsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        invitationsViewModel =
                ViewModelProvider(this).get(InvitationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_invitations, container, false)
        val textView: TextView = root.findViewById(R.id.text_account)
        invitationsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}