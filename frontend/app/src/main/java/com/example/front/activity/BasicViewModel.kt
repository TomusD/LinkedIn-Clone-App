import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.front.data.ApiClient
import com.example.front.data.SessionManager
import com.example.front.data.base.User
import com.example.front.data.response.APIResponse
import com.example.front.data.response.EducationList
import com.example.front.data.response.EducationResponse
import com.example.front.data.response.SkillsList
import com.example.front.data.response.UsersList
import com.example.front.data.response.WorkList
import com.example.front.data.response.WorkResponse
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
        val apiClient = ApiClient()
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

    private val _skillsList = MutableStateFlow<List<String>>(mutableListOf())
    val skillsList: StateFlow<List<String>> get() = _skillsList

    fun fetchSkills(context: Context) {
        val uid = SessionManager(context).getUserInfo(SessionManager.USER_ID)

        val apiClient = ApiClient()
        val call = apiClient.getApiService(context).getSkills(uid!!.toInt())

        call.enqueue(object : Callback<SkillsList> {
            override fun onResponse(call: Call<SkillsList>, response: Response<SkillsList>) {
                if (response.isSuccessful) {
                    Log.d("MYTEST", "SKILLS-SUCCESS")
                    _skillsList.value = response.body()?.skills ?: emptyList()
                }
            }

            override fun onFailure(call: Call<SkillsList>, t: Throwable) {
                Log.e("MYTEST", "SKILLS-FAILURE: "+ t.message.toString())
            }
        })
    }

    private val _publicityMap = MutableStateFlow<Map<String, Boolean>>(mutableMapOf())
    val publicityMap: StateFlow<Map<String, Boolean>> get() = _publicityMap

    fun fetchPublicity(context: Context) {
        val uid = SessionManager(context).getUserInfo(SessionManager.USER_ID)
        Log.d("MYTEST", "UIDDDDDD       $uid")

        val apiClient = ApiClient()
        val call = apiClient.getApiService(context).getPublicity(uid!!.toInt())

        call.enqueue(object : Callback<Map<String, Boolean>> {
            override fun onResponse(call: Call<Map<String, Boolean>>, response: Response<Map<String, Boolean>>) {
                if (response.isSuccessful) {
                    _publicityMap.value = response.body() ?: mutableMapOf(
                                                                Pair("work", true),
                                                                Pair("education", true),
                                                                Pair("skills", true))
                    Log.d("MYTEST", "EDU-SUCCESS" + _publicityMap.value)
                }
            }

            override fun onFailure(call: Call<Map<String, Boolean>>, t: Throwable) {
                Log.e("MYTEST", "EDU-FAILURE: "+ t.message.toString())
            }
        })
    }


    fun updatePublicity(context: Context, info: String) {
        val uid = SessionManager(context).getUserInfo(SessionManager.USER_ID)
        Log.d("MYTEST", "UIDDDDDD       $uid")

        val apiClient = ApiClient()
        val call = apiClient.getApiService(context).updatePublicity(info)

        call.enqueue(object : Callback<APIResponse> {
            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                if (response.isSuccessful) {
                    val res = response.body()
                    Log.d("MYTEST", "TOGGLE-SUCCESS - $info: $res")
                }
            }

            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                Log.e("MYTEST", "TOGGLE-FAILURE - $info: "+ t.message.toString())
            }
        })
    }
}
