package com.ansh.searchengine

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ResultAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchInput = findViewById<EditText>(R.id.searchInput)
        val searchButton = findViewById<Button>(R.id.searchButton)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        adapter = ResultAdapter { url ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        searchButton.setOnClickListener {
            val query = searchInput.text.toString()
            search(query)
        }
    }

    private fun search(query: String) {

        val retrofit = Retrofit.Builder()
            .baseUrl("YOUR_PROJECT_URL/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ApiService::class.java)

        val filter = "(title.ilike.*$query*,content.ilike.*$query*)"

        service.searchPages(filter).enqueue(object : Callback<List<SearchResult>> {
            override fun onResponse(
                call: Call<List<SearchResult>>,
                response: Response<List<SearchResult>>
            ) {
                if (response.isSuccessful) {
                    adapter.submitList(response.body() ?: emptyList())
                }
            }

            override fun onFailure(call: Call<List<SearchResult>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}

data class SearchResult(
    val id: Int,
    val title: String,
    val url: String,
    val content: String
)

interface ApiService {

    @Headers(
        "apikey: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImpoeXF5c2tlbXN2b2l6bW11cGthIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzE4NDQ5ODUsImV4cCI6MjA4NzQyMDk4NX0.IvjAWJZ4DeOCNG0SzKgV5P-LXW2aYvX_RA-NDw5S-ec",
        "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImpoeXF5c2tlbXN2b2l6bW11cGthIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzE4NDQ5ODUsImV4cCI6MjA4NzQyMDk4NX0.IvjAWJZ4DeOCNG0SzKgV5P-LXW2aYvX_RA-NDw5S-ec"
    )
    @GET("rest/v1/pages")
    fun searchPages(
        @Query("or") filter: String,
        @Query("limit") limit: Int = 20
    ): Call<List<SearchResult>>
}
