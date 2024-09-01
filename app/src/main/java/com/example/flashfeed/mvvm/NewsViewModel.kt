package com.example.flashfeed.mvvm

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

class NewsViewModel(val newsRepo: NewsRepo, application: NewsApplication) :
    AndroidViewModel(application) {
    //get breaking news from api
    val breakingNews: MutableLiveData<Resource<News>> = MutableLiveData()
    val pageNumber = 1

    //get news by category from api
    val categoryNews: MutableLiveData<Resource<News>> = MutableLiveData()

    //get news from db
    val getSavedNews = newsRepo.getAllSavedArticles()

init {
    getBreakingNews("us")
}
    fun getBreakingNews(code: String) = viewModelScope.launch(Dispatchers.IO) {
        chechingInternetAndBreakingNews(code)
    }

    private suspend fun chechingInternetAndBreakingNews(code: String) {
        breakingNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepo.getBreakingNews(code, pageNumber)
                breakingNews.postValue(HandleBreakingNews(response))
            } else {
                breakingNews.postValue(Resource.Error("No Internet Connection"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> breakingNews.postValue(Resource.Error("Network Failure"))
                else -> breakingNews.postValue(Resource.Error("Conversion Error"))
            }
        }


    }

    private fun HandleBreakingNews(response: Response<News>): Resource<News> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }

        }

        return Resource.Error(response.message())

    }

    private fun hasInternetConnection(): Boolean {

        val connectivityManager =
            getApplication<NewsApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }


    fun insertArticle(savedArt: SavedArticle) {
        insertNews(savedArt)
    }

    fun insertNews(savedArt: SavedArticle) = viewModelScope.launch(Dispatchers.IO) {


        newsRepo.insertNews(savedArt)
    }

    fun deleteArticle() = viewModelScope.launch(Dispatchers.IO) {
        deleteAllNews()

    }

    fun deleteAllNews() = viewModelScope.launch(Dispatchers.IO) {


        newsRepo.deleteAllArticles()
    }

}

