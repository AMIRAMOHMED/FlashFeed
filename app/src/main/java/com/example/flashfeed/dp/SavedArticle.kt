package com.example.flashfeed.dp
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NEWSARTICLE")
data class SavedArticle(
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  val id: Int = 0,

  @ColumnInfo(name = "description")
  val description: String,

  @ColumnInfo(name = "publishedAt")
  val publishedAt: String,

  @ColumnInfo(name = "source")
  val source: Source,

  @ColumnInfo(name = "title")
  val title: String,

  @ColumnInfo(name = "url")
  val url: String,

  @ColumnInfo(name = "urlToImage")
  val urlToImage: String
)