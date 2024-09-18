package com.example.front.data

import android.content.Context
import android.icu.util.TimeUnit
import com.example.front.BuildConfig
import com.example.front.ChatWebSocketListener
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayInputStream

class ApiClient {
    //val inputStream = context.applicationContext.resources
    private val IP = BuildConfig.ip
    private val PORT = BuildConfig.port
    private val BASE_URL = "https://$IP:$PORT"
    private val BASE_WS_URL = "ws://$IP:$PORT"

    private lateinit var apiService: ApiService

    val cert: String = "-----BEGIN CERTIFICATE-----\n" +
            "MIIFazCCA1OgAwIBAgIUQO4kpbgcZ8sELedUZ6k5K6ZVC9IwDQYJKoZIhvcNAQEL\n" +
            "BQAwRTELMAkGA1UEBhMCQVUxEzARBgNVBAgMClNvbWUtU3RhdGUxITAfBgNVBAoM\n" +
            "GEludGVybmV0IFdpZGdpdHMgUHR5IEx0ZDAeFw0yNDA4MjgxMjU1MzBaFw0yNTA4\n" +
            "MjgxMjU1MzBaMEUxCzAJBgNVBAYTAkFVMRMwEQYDVQQIDApTb21lLVN0YXRlMSEw\n" +
            "HwYDVQQKDBhJbnRlcm5ldCBXaWRnaXRzIFB0eSBMdGQwggIiMA0GCSqGSIb3DQEB\n" +
            "AQUAA4ICDwAwggIKAoICAQCT8a2+QPgHjR3A739UgpJpizFp8HMZEk8zQ9u3Nsc0\n" +
            "rhY6Y+h/cH4Ktzo6eSC7L2JA0KTnKnvh89Ce9ngvQsAzDopJXs1c+nN+KPgxykNw\n" +
            "hOvTKaGALFFxDvKdXnt1klLxBDeygh+SGPA5gCTVlRFRxFmllLgmCmyT6bFdXNUx\n" +
            "nrNQxMeKlMG2jhKsq1OTyqtzBN+x5dIWjBCqRYEc8IUt1X87MP0HjjVi6J2Bu2Ql\n" +
            "Q0mZVHJLdG9jbmtAU8buX6OoTeoP+yUIJex3qrvHYpBdNcNl1c0f1wbgSQJ5kiHE\n" +
            "Yt0vQxz2SyDwwXPwlGdUdqZ5cgW6epb9Hpe70ttFf1BQWCjc4RVu1oc+3xJwprsZ\n" +
            "4b/tweXEAzasSk6tGdWNXj2k3MXe9dUZbFjlT3lI5Z0c60NdR19TM+YKOnccEUue\n" +
            "50RCryBA53JgyXnaXBkzRjMNHcZbLtfk8yow04vRuSh0GABIoxcDlOO7FHwD7h0V\n" +
            "WUGL4Csuun860wR8NRLnssFWSC0ML+IXkMd7wvY/CiSTAgJ+deVeZIZ77RHQbBSK\n" +
            "05Z6VpJONoEz7mZ3xsx9OtxdIRcvcKRXJwtWccR0Lu/H/oLVHoizKfFvm4dERROI\n" +
            "hKnalf0SiXoA/0PbpjOtrFL1E8lWSc9Qtn8w9rJwS9hVLhTzXXtXQZVqfoF0cpRJ\n" +
            "+QIDAQABo1MwUTAdBgNVHQ4EFgQUOm+wEudUgA8j5ck4c0Sl1nSF9kQwHwYDVR0j\n" +
            "BBgwFoAUOm+wEudUgA8j5ck4c0Sl1nSF9kQwDwYDVR0TAQH/BAUwAwEB/zANBgkq\n" +
            "hkiG9w0BAQsFAAOCAgEAYz5E1qxvQBbhADBS1hU/dhzUC6OiP4u9dzzLairjYjk4\n" +
            "nzFtbe/8yiTf0LHGut4nxtIdzB/oQAmlRDVzwbZmS4FKLWmJQkllpiBrgvoaunR3\n" +
            "6ktRrInF8I0MGy4c5v8wSk53L+7cEoSV+iEDSKfKFb24KSQt7RezmWSmVLcUJASL\n" +
            "iUn2PdLIh2Z5Qf4/uVBqnBHmVN0COQCT5w3NlYVW9QHpiznTz2n8EFv/a50gmp8n\n" +
            "goTBzZOxVCNcHURdTF7vWopi3JROYXglSCENwF5jr1sOP6a9k30tq1ZY6Hepqzxh\n" +
            "oMwUhmWkKk3pxOsEW+hh2IQAEg8bZX9oR3XpUV854iPSPshQcM2EeMpI7sOwU45A\n" +
            "E/qbLro9L0oMfJC8Vm0snL0j+lfb8YkHfzPB9S6p1WVQ/aLMomKhPRe4TmwSHoiP\n" +
            "IUhtVRwPsd4HNFLKvkdT25uADGeWbXc8o9+pprrFFLAe7Dc3deioEoV5q1pVn3HY\n" +
            "ZWaSuk0sfwitERnNtXLX0MqQpqztkKA8tPidpHf/5KHZY/PI66akUgeL6v/XhRG+\n" +
            "G3eZmo74qlTSO2lHWy8FZshaB32YdWn1na+rnhTFh470NFqj7vEfMi6VWoD5oBLi\n" +
            "m5NS8qsj1fq7E3qetgIPND7HA7A6t7pgaWnTDE0q4bjVp0S4oHGc/N3r2ytRHqU=\n" +
            "-----END CERTIFICATE-----\n"


    val inputStream = ByteArrayInputStream(cert.toByteArray())
//    val inp : InputStream? = this.javaClass.getResourceAsStream("/raw/certificate.pem")


    fun getApiService(context: Context): ApiService {
        val client: OkHttpClient.Builder = HttpClient.getRetrofitInstance(inputStream, IP)
        val clientBuilt: OkHttpClient = client
            .addInterceptor(AuthInterceptor(context))
            .connectTimeout(150L, java.util.concurrent.TimeUnit.SECONDS)
            .callTimeout(150L, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(150L, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(150L, java.util.concurrent.TimeUnit.SECONDS)
            .build()

        // Initialize ApiService if not initialized yet
        if (!::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientBuilt)
                .build()

            apiService = retrofit.create(ApiService::class.java)
        }

        return apiService
    }


    fun createWebSocket(listener: ChatWebSocketListener, context: Context, user_id: Int): WebSocket {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .pingInterval(10, java.util.concurrent.TimeUnit.SECONDS)
            .build()

        val request = Request.Builder()
            .url("$BASE_WS_URL/ws/chat/$user_id") // Replace with your FastAPI server URL
            .build()

        return client.newWebSocket(request, listener)
    }
}




