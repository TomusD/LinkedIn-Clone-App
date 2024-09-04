import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.front.data.ApiClient
import com.example.front.data.base.User
import com.example.front.data.response.UsersList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BasicViewModel : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> get() = _users

    fun fetchUsers(context: Context) {
        val apiClient = ApiClient()
        val call = apiClient.getApiService(context).getUsers()

        call.enqueue(object : Callback<UsersList> {
            override fun onResponse(call: Call<UsersList>, response: Response<UsersList>) {
                if (response.isSuccessful) {
                    Log.d("MYTEST", "SUCCESS")
                    _users.value = response.body()?.users ?: emptyList()
                }
            }

            override fun onFailure(call: Call<UsersList>, t: Throwable) {
                Log.e("MYTEST", "FAILURE BVM: "+ t.message.toString())
            }
        })
    }
}
