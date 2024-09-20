package com.example.front.activity

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.front.data.ApiClient
import com.example.front.data.SessionManager
import com.example.front.data.base.User
import com.example.front.data.request.Education
import com.example.front.data.request.Work
import com.example.front.data.response.APIResponse
import com.example.front.data.response.AllJobs
import com.example.front.data.response.EducationList
import com.example.front.data.response.EducationResponse
import com.example.front.data.response.JobApplied
import com.example.front.data.response.JobUploaded
import com.example.front.data.response.JobsList
import com.example.front.data.response.SkillsList
import com.example.front.data.response.UsersList
import com.example.front.data.response.WorkList
import com.example.front.data.response.WorkResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
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

    private val _workList = MutableStateFlow<MutableList<WorkResponse>>(mutableListOf())
    val workList: StateFlow<MutableList<WorkResponse>> get() = _workList

    fun updateWork(work: Work) {
        val wr = WorkResponse(-1, work.organization, work.role, work.date_started, work.date_ended)
        _workList.update {
            it += wr
            it
        }
    }

    fun fetchWork(context: Context) {
        val apiClient = ApiClient()
        val call = apiClient.getApiService(context).getWorkExperience()

        call.enqueue(object : Callback<WorkList> {
            override fun onResponse(call: Call<WorkList>, response: Response<WorkList>) {
                if (response.isSuccessful) {
                    Log.d("MYTEST", "WORK-SUCCESS")
                    _workList.value = response.body()?.workList ?: mutableListOf()
                }
            }

            override fun onFailure(call: Call<WorkList>, t: Throwable) {
                Log.e("MYTEST", "WORK-FAILURE: "+ t.message.toString())
            }
        })
    }

    private val _educationList = MutableStateFlow<MutableList<EducationResponse>>(mutableListOf())
    val educationList: StateFlow<MutableList<EducationResponse>> get() = _educationList

    fun updateEducation(edu: Education) {
        val er = EducationResponse(-1, edu.organization, edu.science_field, edu.degree, edu.date_started, edu.date_ended)
        _educationList.update {
            it += er
            it
        }
    }

    fun fetchEducation(context: Context) {
        val apiClient = ApiClient()
        val call = apiClient.getApiService(context).getEducation()

        call.enqueue(object : Callback<EducationList> {
            override fun onResponse(call: Call<EducationList>, response: Response<EducationList>) {
                if (response.isSuccessful) {
                    Log.d("MYTEST", "EDU-SUCCESS")
                    _educationList.value = response.body()?.eduList ?: mutableListOf()
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


    private val _availableSkillsList = MutableStateFlow<List<String>>(mutableListOf())
    val availableSkillsList: StateFlow<List<String>> get() = _availableSkillsList

    fun fetchAvailableSkills(context: Context) {

        val apiClient = ApiClient()
        val call = apiClient.getApiService(context).getAvailableSkills()

        call.enqueue(object : Callback<SkillsList> {
            override fun onResponse(call: Call<SkillsList>, response: Response<SkillsList>) {
                if (response.isSuccessful) {
                    Log.d("MYTEST", "AVAILABLE SKILLS-SUCCESS")
                    _availableSkillsList.value = response.body()?.skills ?: emptyList()
                }
            }

            override fun onFailure(call: Call<SkillsList>, t: Throwable) {
                Log.e("MYTEST", "AVAILABLE SKILLS-FAILURE: "+ t.message.toString())
            }
        })
    }


    private val _publicityMap = MutableStateFlow<MutableMap<String, Boolean>>(mutableMapOf())
    val publicityMap: StateFlow<MutableMap<String, Boolean>> get() = _publicityMap

    fun updatePublicityToggle(field: String, public: Boolean) {
        _publicityMap.update {
            it[field] = public
            it
        }
    }

    fun fetchPublicity(context: Context) {
        val uid = SessionManager(context).getUserInfo(SessionManager.USER_ID)

        val apiClient = ApiClient()
        val call = apiClient.getApiService(context).getPublicity(uid!!.toInt())

        call.enqueue(object : Callback<MutableMap<String, Boolean>> {
            override fun onResponse(call: Call<MutableMap<String, Boolean>>, response: Response<MutableMap<String, Boolean>>) {
                if (response.isSuccessful) {
                    _publicityMap.value = response.body() ?: mutableMapOf(
                                                                Pair("work", true),
                                                                Pair("education", true),
                                                                Pair("skills", true))
                    Log.d("MYTEST", "EDU-SUCCESS" + _publicityMap.value)
                }
            }

            override fun onFailure(call: Call<MutableMap<String, Boolean>>, t: Throwable) {
                Log.e("MYTEST", "EDU-FAILURE: "+ t.message.toString())
            }
        })
    }


    fun updatePublicity(context: Context, info: String) {
        val uid = SessionManager(context).getUserInfo(SessionManager.USER_ID)

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


    private val _recommendedJobs = MutableStateFlow<List<JobApplied>>(mutableListOf())
    val recommendedJobs: StateFlow<List<JobApplied>> get() = _recommendedJobs
    fun fetchRecommendedJobs(context: Context) {
        val apiClient = ApiClient()
        val call = apiClient.getApiService(context).getRecommendedJobs()
        Log.d("MYTEST", "JOBS RECOMMENDED - HERE WE GO")

        call.enqueue(object : Callback<JobsList> {
            override fun onResponse(call: Call<JobsList>, response: Response<JobsList>) {
                if (response.isSuccessful) {
                    _recommendedJobs.value = response.body()?.recommendations ?: emptyList()
                    Log.d("MYTEST", "JOBS RECOMMENDED-SUCCESS")
                }
            }

            override fun onFailure(call: Call<JobsList>, t: Throwable) {
                Log.e("MYTEST", "JOBS RECOMMENDED-FAILURE: "+ t.message.toString())
            }
        })
    }

    fun applyToRecommendedJob(job: JobApplied) {
        _recommendedJobs.update {
            val notAppliedJobs = it.toMutableList()
            notAppliedJobs.remove(job)
            notAppliedJobs
        }

        _jobPair.update {
            val appliedJobs = it.first?.toMutableList()
            appliedJobs!!.add(job)
            Pair(appliedJobs, it.second)
        }
    }

    fun revokeAppliance(job: JobApplied) {
        _jobPair.update {
            val appliedJobs = it.first?.toMutableList()
            appliedJobs!!.remove(job)
            Pair(appliedJobs, it.second)
        }

        _recommendedJobs.update {
            val notAppliedJobs = it.toMutableList()
            notAppliedJobs.add(job)
            notAppliedJobs
        }

    }



    private val _jobPair = MutableStateFlow<Pair<List<JobApplied>?, List<JobUploaded>?>>(Pair(listOf(), listOf()))
    val jobPair: StateFlow<Pair<List<JobApplied>?, List<JobUploaded>?>> get() = _jobPair

    fun fetchJobs(context: Context) {
        val apiClient = ApiClient()
        val call = apiClient.getApiService(context).getAllJobs()
        Log.d("MYTEST", "HERE WE GO")

        call.enqueue(object : Callback<AllJobs> {
            override fun onResponse(call: Call<AllJobs>, response: Response<AllJobs>) {
                if (response.isSuccessful) {
                    _jobPair.value = Pair(response.body()?.jobs_applied,
                        response.body()?.jobs_uploaded
                    )
                    Log.d("MYTEST", response.body().toString())
                    Log.d("MYTEST", "JOBS ALL-SUCCESS")
                }
            }

            override fun onFailure(call: Call<AllJobs>, t: Throwable) {
                Log.e("MYTEST", "JOBS ALL-FAILURE: "+ t.message.toString())
            }
        })
    }
}
