package com.example.front.data

import androidx.compose.ui.res.stringResource
import com.example.front.BuildConfig
import com.example.front.HttpClient
import com.example.front.R
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayInputStream
import java.io.InputStream

import android.content.Context

//val inputStream = context.applicationContext.resources
object RetrofitClient {
    private const val IP = BuildConfig.ip
    private const val PORT = BuildConfig.port

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

//    private const val BASE_URL = "domaintesturlwow.com"
    private const val BASE_URL = "https://$IP:$PORT"

    val inputStream = ByteArrayInputStream(cert.toByteArray())
//    val inp : InputStream? = this.javaClass.getResourceAsStream("/raw/certificate.pem")

    val client : OkHttpClient = HttpClient.getRetrofitInstance(inputStream, IP)


    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}

object ApiClient {
    val apiService: ApiService by lazy {
        RetrofitClient.retrofit.create(ApiService::class.java)
    }
}
