package com.example.flashfeed.adapters

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.flashfeed.R
import com.example.flashfeed.Utils
import com.example.flashfeed.dp.Article
import com.example.flashfeed.dp.SavedArticle

class SavedArticleAdapter : RecyclerView.Adapter<SavedArticleHolder>() {
    private var articles = listOf<SavedArticle>()
    private  var listener: ItemClickListener ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedArticleHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.newlist, parent, false)
        val viewHolder = SavedArticleHolder(view)
        return viewHolder


    }



    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onBindViewHolder(holder: SavedArticleHolder, position: Int) {


        val article = articles[position]
        val requestOption = RequestOptions()

        holder.itemView.apply {

            Glide.with(this)
                .load(article.urlToImage)
                .apply(requestOption)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {


                        holder.pb.visibility = View.VISIBLE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.pb.visibility = View.GONE
                        return false
                    }
                }).transition(DrawableTransitionOptions.withCrossFade()).into(holder.imageView)
            holder.textTitle.text = article.title
            holder.tvSource.text = article.source!!.name

            holder.tvDescription.text = article.description
            holder.tvPubslishedAt.text = Utils.DateFormat(article.publishedAt)

        }


    }



    fun setItemClickListener(listener : ItemClickListener){
        this.listener = listener

    }

    @SuppressLint("NotifyDataSetChanged")
    fun setlist(articles: List<SavedArticle>) {
        this.articles = articles
        notifyDataSetChanged()

    }



}

class SavedArticleHolder(item: View) : ViewHolder(item) {


    val textTitle: TextView = itemView.findViewById(R.id.tvTitle)
    val tvSource: TextView = itemView.findViewById(R.id.tvSource)
    val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
    val tvPubslishedAt: TextView = itemView.findViewById(R.id.tvPublishedAt)
    val imageView: ImageView = itemView.findViewById(R.id.ivArticleImage)
    val pb: ProgressBar = itemView.findViewById(R.id.pbImage)


}

