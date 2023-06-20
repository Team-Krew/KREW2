package com.example.krew.controller

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.ViewAnimator
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.krew.R
import com.example.krew.adapter.PlacePredictionAdapter
import com.example.krew.apimodel.GeocodingResult
import com.example.krew.apimodel.LatLngAdapter
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.*
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.gson.GsonBuilder
import org.json.JSONArray
import org.json.JSONException


class ProgrammaticAutocompleteGeocodingActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private val adapter = PlacePredictionAdapter()
    private val gson = GsonBuilder().registerTypeAdapter(LatLng::class.java, LatLngAdapter()).create()

    private lateinit var queue: RequestQueue
    private lateinit var placesClient: PlacesClient
    private var sessionToken: AutocompleteSessionToken? = null

    private lateinit var viewAnimator: ViewAnimator
    private lateinit var progressBar: ProgressBar

    private var formattedAddress:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_programmatic_autocomplete)
        progressBar = findViewById(R.id.progress_bar)
        viewAnimator = findViewById(R.id.view_animator)
        placesClient = Places.createClient(this)
        queue = Volley.newRequestQueue(this)

        val searchView = findViewById<SearchView>(R.id.search)
        initSearchView(searchView)

        initRecyclerView()
    }

    //SearchView initialize
    private fun initSearchView(searchView: SearchView) {
        searchView.queryHint = getString(R.string.search_a_place)
        searchView.isIconifiedByDefault = false
        searchView.isFocusable = true
        searchView.isIconified = false
        searchView.requestFocusFromTouch()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                progressBar.isIndeterminate = true
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({ getPlacePredictions(newText)}, 300)
                return true
            }
        })
    }

    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
        adapter.onPlaceClickListener = { geocodePlaceAndDisplay(it)}

    }

    private fun getPlacePredictions(query: String) {
        val bias: LocationBias = RectangularBounds.newInstance(
            //대한민국 위도 경도 범위 설정
            LatLng(33.00000, 124.00000),  // SW lat, lng
            LatLng(43.00000, 132.00000) // NE lat, lng
        )

        val newRequest = FindAutocompletePredictionsRequest.builder()
            .setLocationBias(bias)
            .setCountries("KR")
            .setTypesFilter(listOf(PlaceTypes.ESTABLISHMENT))
            .setSessionToken(sessionToken)
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(newRequest)
            .addOnSuccessListener { response ->
                val predictions = response.autocompletePredictions
                adapter.setPredictions(predictions)
                progressBar.isIndeterminate = false
                viewAnimator.displayedChild = if (predictions.isEmpty()) 0 else 1
            }.addOnFailureListener { exception: Exception? ->
                progressBar.isIndeterminate = false
                if (exception is ApiException) {
                }
            }
    }
    private fun geocodePlaceAndDisplay(placePrediction: AutocompletePrediction){
        // Construct the request URL
        val apiKey = getString(R.string.apiKey)
        Log.i("checkapikey",apiKey)
        val requestURL =
            "https://maps.googleapis.com/maps/api/geocode/json?place_id=${placePrediction.placeId}&key=$apiKey&language=ko"
        // Use the HTTP request URL for Geocoding API to get geographic coordinates for the place
        val request = JsonObjectRequest(Request.Method.GET, requestURL, null, { response ->
            Log.i("checkResponse",response.toString())
            try {
                val status: String = response.getString("status")
                Log.i("status",status)
                if (status != "OK") {
                    Log.e("StatusERROR", "$status " + response.getString("error_message"))
                }

                // Inspect the value of "results" and make sure it's not empty
                val results: JSONArray = response.getJSONArray("results")
                if (results.length() == 0) {
                    Log.w("NORESULT", "No results from geocoding request.")
                    return@JsonObjectRequest
                }
                // Use Gson to convert the response JSON object to a POJO
                val result: GeocodingResult = gson.fromJson(results.getString(0), GeocodingResult::class.java)
                Log.i("result",result.toString())
                ActivityIntent(placePrediction,result)
            } catch (e: JSONException) {
                e.printStackTrace()
                Log.e("checkJsonexception",e.printStackTrace().toString())
            }
        }, { error ->
        })
        // Add the request to the Request queue.
        queue.add(request)
    }
    @SuppressWarnings("deprecation")
    fun ActivityIntent(placePrediction: AutocompletePrediction,result:GeocodingResult){
        Log.i("ACTIVITYINTENTINPABEFORE","ACTIVITYINTENTINPABEFORE")
        val intent_ret = Intent(this@ProgrammaticAutocompleteGeocodingActivity, AddSchedule::class.java)
        intent_ret.putExtra("formattedAddress",result.formatted_address)
        intent_ret.putExtra("place",placePrediction.getPrimaryText(null).toString())
        intent_ret.putExtra("selected_date", intent.getStringExtra("selected_date").toString())
        startActivity(intent_ret)
        finish()
    }

    override fun onBackPressed() {
        val intent = Intent(this@ProgrammaticAutocompleteGeocodingActivity,AddSchedule::class.java)
        startActivity(intent)
        finish()
    }
}

