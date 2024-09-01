package com.example.flashfeed.mvvm

import androidx.lifecycle.LiveData
import com.example.flashfeed.dp.SavedArticle
import com.example.flashfeed.service.RetrofitInstance

class NewsRepo(val newsDao: NewsDao) {

    //insert article to db
    suspend fun insertNews(savedArticle: SavedArticle) {
        newsDao.insert(savedArticle)
    }

    //get all saved articles from db
    fun getAllSavedArticles(): LiveData<List<SavedArticle>> {

        return newsDao.getSavedArticles()
    }

    //get news by id from db
    fun getNewsById(): LiveData<SavedArticle> {
        return newsDao.getNewsById()
    }

    //delete all articles from db
    fun deleteAllArticles() {
        newsDao.deleteAllArticles()
    }

    //get breaking news from api
    suspend fun getBreakingNews() = RetrofitInstance.api.getBreakingNews()

    suspend fun getBreakingNews(code: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(code, pageNumber)

    //get news by category from api
    suspend fun getCategoryNews(category: String) =
        RetrofitInstance.api.getByCategory(category)


}