package com.example.flashfeed.mvvm

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.flashfeed.dp.News
import com.example.flashfeed.dp.SavedArticle
import com.example.flashfeed.wrapper.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
class NewsViewModel(private val newsRepo: NewsRepo, application: Application) :
    AndroidViewModel(application) {

    val breakingNews: MutableLiveData<Resource<News>> = MutableLiveData()
    val categoryNews: MutableLiveData<Resource<News>> = MutableLiveData()
    val getSavedNews = newsRepo.getAllSavedArticles()

    private val pageNumber = 1

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(code: String) = viewModelScope.launch(Dispatchers.IO) {
        checkInternetAndFetchBreakingNews(code)
    }

    private suspend fun checkInternetAndFetchBreakingNews(code: String) {
        breakingNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepo.getBreakingNews(code, pageNumber)
                breakingNews.postValue(handleNewsResponse(response))
            } else {
                breakingNews.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            breakingNews.postValue(
                when (t) {
                    is IOException -> Resource.Error("Network Failure")
                    else -> Resource.Error("Conversion Error")
                }
            )
        }
    }

    private fun handleNewsResponse(response: Response<News>): Resource<News> {
        return if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Resource.Success(resultResponse)
            } ?: Resource.Error("Empty Response")
        } else {
            Resource.Error(response.message())
        }
    }

    fun getCategory(cat: String) = viewModelScope.launch {
        categoryNews.postValue(Resource.Loading())
        try {
            val response = newsRepo.getCategoryNews(cat)
            categoryNews.postValue(handleNewsResponse(response))
        } catch (t: Throwable) {
            categoryNews.postValue(
                when (t) {
                    is IOException -> Resource.Error("Network Failure")
                    else -> Resource.Error("Conversion Error")
                }
            )
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<NewsApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } else {
            connectivityManager.activeNetworkInfo?.run {
                type == ConnectivityManager.TYPE_WIFI ||
                        type == ConnectivityManager.TYPE_MOBILE ||
                        type == ConnectivityManager.TYPE_ETHERNET
            } ?: false
        }
    }

    fun insertArticle(savedArt: SavedArticle) = viewModelScope.launch(Dispatchers.IO) {
        newsRepo.insertNews(savedArt)
    }

    fun deleteArticle() = viewModelScope.launch(Dispatchers.IO) {
        newsRepo.deleteAllArticles()
    }
}