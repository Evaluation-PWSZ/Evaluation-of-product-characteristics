package pl.memstacja.bottomnavigation.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pwszproducts.myapplication.data.model.StaticUserData

class AccountViewModel : ViewModel() {
    private val _login = MutableLiveData<String>().apply {
        value = StaticUserData.user.login
    }
    private val _email = MutableLiveData<String>().apply {
        value = StaticUserData.user.email
    }

    val login: LiveData<String> = _login
    val email: LiveData<String> = _email
}