package com.aroundpharmacy.app.view.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aroundpharmacy.app.BuildConfig
import com.aroundpharmacy.app.adapter.DrugAdapter
import com.aroundpharmacy.app.databinding.FragmentDrugSearchBinding
import com.aroundpharmacy.app.service.DrugPrdtPrmsnInfoService
import com.aroundpharmacy.app.view.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URLEncoder

class DrugSearchFragment : BaseFragment(){
    override  var isBackAvailable: Boolean = false
    private lateinit var binding : FragmentDrugSearchBinding
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var pageSize = 50
    private val visibleThreshold = 5
    private var currentKeyword: String? = null
    private lateinit var adapter: DrugAdapter
    private val logging = HttpLoggingInterceptor().apply {
        // 요청 URL + 헤더 + 바디까지 전부 찍고 싶다면 BODY
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://apis.data.go.kr/")
            .addConverterFactory(GsonConverterFactory.create()).client(client)
            .build()
    }
    private val service: DrugPrdtPrmsnInfoService by lazy {
        retrofit.create(DrugPrdtPrmsnInfoService::class.java)
    }


    private lateinit var progressBar : ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDrugSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar =binding.progressBar
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = DrugAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if(dy<=0) return
                val lm = recyclerView.layoutManager as LinearLayoutManager
                val total = lm.itemCount
                val lastVisible = lm.findLastVisibleItemPosition()
                if(!isLoading && !isLastPage && lastVisible >= total - visibleThreshold){
                    loadPage()
                }
            }
        })

        performSearch(keyword = null)  // 전체 리스트
    }
    private fun performSearch(keyword: String?) {
        currentKeyword = keyword
        currentPage = 1
        isLastPage = false
        adapter.clearItems()
        loadPage()
    }
    private fun loadPage() {
        isLoading = true
        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val resp = service.getMedicineList(
                    serviceKey = BuildConfig.DATA_API_KEY,
                    pageNo     = currentPage,
                    numOfRows  = pageSize,
                    itemName   = currentKeyword
                )


//                if (resp.isSuccessful) {
//                    val rawJson = resp.body()?.string() ?: "빈 바디"
//                    Log.d("RawJSON", "$rawJson")             // 원시 JSON 전체 출력
//                } else {
//                    Log.e("RawJSON", "HTTP ${resp.code()}")
//                }
//                val items = resp.body.items.itemList
//
//                withContext(Dispatchers.Main) {
//                    adapter.addItems(items)
//                    isLoading = false
//                    progressBar.visibility = View.GONE
//
//                    if (items.size < pageSize) {
//                        isLastPage = true
//                    } else {
//                        currentPage++
//                    }
//                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    isLoading = false
                    progressBar.visibility = View.GONE
                    Log.d("TAG",e.message.toString())
                    Toast.makeText(requireContext(), "로드 실패: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
  
}