package com.example.flashfeed.mvvm

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.flashfeed.dp.SavedArticle

@Dao
interface NewsDao {
@Insert
suspend fun insert(savedArticle: SavedArticle)

@Query("SELECT * FROM NEWSARTICLE")
 fun getSavedArticles():LiveData<List<SavedArticle>>

 @Query("SeLECT * FROM NEWSARTICLE")
 fun getNewsById():LiveData<SavedArticle>

 @Query("DELETE FROM NEWSARTICLE")
 fun deleteAllArticles()

}