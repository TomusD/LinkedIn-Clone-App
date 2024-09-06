import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.front.data.ApiClient
import com.example.front.data.base.User
import com.example.front.data.response.EducationList
import com.example.front.data.response.EducationResponse
import com.example.front.data.response.UsersList
import com.example.front.data.response.WorkList
import com.example.front.data.response.WorkResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BasicViewModel : ViewModel() {
    private val apiClient = ApiClient()

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> get() = _users

    fun fetchUsers(context: Context) {
        val apiClient = ApiClient()
        val call = apiClient.getApiService(context).getUsers()
        call.enqueue(object : Callback<UsersList> {
            override fun onResponse(call: Call<UsersList>, response: Response<UsersList>) {
                if (response.isSuccessful) {
                    Log.d("MYTEST", "USERS-SUCCESS")
                    _users.value = response.body()?.users ?: emptyList()
                }
            }

            override fun onFailure(call: Call<UsersList>, t: Throwable) {
                Log.e("MYTEST", "USERS-FAILURE: "+ t.message.toString())
            }
        })
    }

    private val _workList = MutableStateFlow<List<WorkResponse>>(emptyList())
    val workList: StateFlow<List<WorkResponse>> get() = _workList

    fun fetchWork(context: Context) {
        val apiClient = ApiClient()
        val call = apiClient.getApiService(context).getWorkExperience()

        call.enqueue(object : Callback<WorkList> {
            override fun onResponse(call: Call<WorkList>, response: Response<WorkList>) {
                if (response.isSuccessful) {
                    Log.d("MYTEST", "WORK-SUCCESS")
                    _workList.value = response.body()?.workList ?: emptyList()
                }
            }

            override fun onFailure(call: Call<WorkList>, t: Throwable) {
                Log.e("MYTEST", "WORK-FAILURE: "+ t.message.toString())
            }
        })
    }

    private val _educationList = MutableStateFlow<List<EducationResponse>>(emptyList())
    val educationList: StateFlow<List<EducationResponse>> get() = _educationList

    fun fetchEducation(context: Context) {
        val call = apiClient.getApiService(context).getEducation()

        call.enqueue(object : Callback<EducationList> {
            override fun onResponse(call: Call<EducationList>, response: Response<EducationList>) {
                if (response.isSuccessful) {
                    Log.d("MYTEST", "EDU-SUCCESS")
                    _educationList.value = response.body()?.eduList ?: emptyList()
                }
            }

            override fun onFailure(call: Call<EducationList>, t: Throwable) {
                Log.e("MYTEST", "EDU-FAILURE: "+ t.message.toString())
            }
        })
    }
}
