package com.example.popisos

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import com.example.popisosnovnihsredstava.entities.Artikal
import com.example.popisosnovnihsredstava.entities.Lokacija
import com.example.popisosnovnihsredstava.entities.Popis
import com.example.popisosnovnihsredstava.entities.PopisStavka
import com.example.popisosnovnihsredstava.entities.Racunopolagac
import com.example.popisosnovnihsredstava.entities.User
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import sqlite.SQLitePopisHelper
import sqlite.SQLiteSifarnikHelper
import java.net.HttpURLConnection
import java.net.URL

class WebAPIClient(private val context: Context) {

    fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun isWebServiceAvailable(url: String): Boolean {
        return try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 1000
            connection.readTimeout = 5000

            connection.responseCode == HttpURLConnection.HTTP_OK
        } catch (e: Exception) {
            false
        }
    }
}

interface ApiService {

    @POST("sendPopisStavke")
    fun sendPopisStavke(@Body popisStavke: List<PopisStavka>): Call<Void>

    @GET("getSifarnikData")
    fun getSifarnikData(): Call<SifarnikResponse>

    @GET("getPopis")
    fun getPopisData(): Call<List<Popis>>
}

const val BASE_URL = "https://localhost:5305/api/"

data class SifarnikResponse(
    val lokacija: List<Lokacija>,
    val racunopolagac: List<Racunopolagac>,
    val user: List<User>,
    val artikal: List<Artikal>
)

object RetrofitClient {

    private val retrofit: Retrofit by lazy {
        val client = OkHttpClient.Builder()
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

fun sendPopisStavkeToServer(context: Context, onResult: (Boolean) -> Unit) {
    val popisHelper = SQLitePopisHelper(context)
    val stavke = popisHelper.getAllPopisStavke()

    if (!WebAPIClient(context).isNetworkAvailable()) {
        Toast.makeText(context, "Internet konekcija nije dostupna!", Toast.LENGTH_SHORT).show()
        onResult(false)
        return
    }

    val webServiceUrl = "$BASE_URL/ping" // Make sure the URL is correct
    if (!WebAPIClient(context).isWebServiceAvailable(webServiceUrl)) {
        Toast.makeText(context, "Servis nije dostupan!", Toast.LENGTH_SHORT).show()
        onResult(false)
        return
    }

    RetrofitClient.apiService.sendPopisStavke(stavke)
        .enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    onResult(true)
                } else {
                    Toast.makeText(context, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    onResult(false)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, "Request failed: ${t.message}", Toast.LENGTH_SHORT).show()
                onResult(false)
            }
        })
}

fun getPopisData(context: Context, onResult: (Boolean) -> Unit) {

    if (!WebAPIClient(context).isNetworkAvailable()) {
        Toast.makeText(context, "Internet konekcija nije dostupna!", Toast.LENGTH_SHORT).show()
        onResult(false)
        return
    }

    val webServiceUrl = "$BASE_URL/ping"
    if (!WebAPIClient(context).isWebServiceAvailable(webServiceUrl)) {
        Toast.makeText(context, "Servis nije dostupan!", Toast.LENGTH_SHORT).show()
        onResult(false)
        return
    }

    RetrofitClient.apiService.getPopisData().enqueue(object : Callback<List<Popis>> {
        override fun onResponse(call: Call<List<Popis>>, response: Response<List<Popis>>) {
            if (response.isSuccessful) {
                val popisList = response.body()
                if (popisList != null) {
                    onResult(true)
                } else {
                    onResult(false)
                }
            } else {
                Toast.makeText(context, "Greška pri preuzimanju podataka!", Toast.LENGTH_SHORT).show()
                onResult(false)
            }
        }

        override fun onFailure(call: Call<List<Popis>>, t: Throwable) {
            Toast.makeText(context, "Greška u komunikaciji: ${t.message}", Toast.LENGTH_SHORT).show()
            onResult(false)
        }
    })
}

fun getSifarnikData(context: Context, onResult: (Boolean) -> Unit) {

    if (!WebAPIClient(context).isNetworkAvailable()) {
        Toast.makeText(context, "Internet konekcija nije dostupna!", Toast.LENGTH_SHORT).show()
        onResult(false)
        return
    }

    val webServiceUrl = "$BASE_URL/ping"
    if (!WebAPIClient(context).isWebServiceAvailable(webServiceUrl)) {
        Toast.makeText(context, "Servis nije dostupan!", Toast.LENGTH_SHORT).show()
        onResult(false)
        return
    }


    RetrofitClient.apiService.getSifarnikData().enqueue(object : Callback<SifarnikResponse> {
        override fun onResponse(call: Call<SifarnikResponse>, response: Response<SifarnikResponse>) {
            if (response.isSuccessful) {
                val sifarnikResponse = response.body()

                if (sifarnikResponse != null) {
                    val sifarnikHelper = SQLiteSifarnikHelper(context)
                    val lokacijaList = sifarnikResponse.lokacija
                    for (lokacija in lokacijaList) {
                        sifarnikHelper.insertLokacija(lokacija)
                    }

                    val racunopolagacList = sifarnikResponse.racunopolagac
                    for (racunopolagac in racunopolagacList) {
                        sifarnikHelper.insertRacunopolagac(racunopolagac)
                    }

                    val userList = sifarnikResponse.user
                    for (user in userList) {
                        sifarnikHelper.insertUser(user)
                    }

                    val artikalList = sifarnikResponse.artikal
                    for (artikal in artikalList) {
                        sifarnikHelper.insertArtikal(artikal)
                    }

                    Toast.makeText(context, "Sifarnik podaci uspešno preuzeti i sačuvani!", Toast.LENGTH_SHORT).show()
                    onResult(true)
                }
            } else {
                Toast.makeText(context, "Greška pri preuzimanju podataka!", Toast.LENGTH_SHORT).show()
                onResult(false)
            }
        }

        override fun onFailure(call: Call<SifarnikResponse>, t: Throwable) {
            Toast.makeText(context, "Greška u komunikaciji: ${t.message}", Toast.LENGTH_SHORT).show()
            onResult(false)
        }
    })
}

