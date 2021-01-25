package pl.memstacja.bottomnavigation.ui.invitations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InvitationsViewModel : ViewModel() {

    private val _key = MutableLiveData<String>().apply {
        value = ""
    }
    val key: LiveData<String> = _key
}