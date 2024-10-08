package com.example.flashfeed.adapters
import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.RecyclerView
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

class ArticleAdapter : RecyclerView.Adapter<ArticleHolder>() {
    private var articles = listOf<Article>()
    private  var listener: ItemClickListener ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.newsitem, parent, false)
        val viewHolder = ArticleHolder(view)
        return viewHolder


    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {


        val article = articles[position]
        val requestOption = RequestOptions()

        holder.itemView.apply {
            holder.pb.visibility = View.VISIBLE
            Glide.with(this).load(article.urlToImage)
                .error(R.drawable.image_error)
                .placeholder(R.drawable.ic_search)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView)
            holder.pb.visibility = View.GONE

            holder.textTitle.text = article.title
            holder.tvSource.text = article.source!!.name
            holder.tvDescription.text = article.description
            holder.tvPubslishedAt.text = Utils.DateFormat(article.publishedAt)
        }



        holder.itemView.setOnClickListener {
            listener?.onItemClick(position,article)
        }

    }



    fun setOnItemClickListener(listener: ItemClickListener) {
        this.listener = listener
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setList(articles: List<Article>) {

this.articles=articles
        notifyDataSetChanged()

    }
@SuppressLint("NotifyDataSetChanged")
fun filteredLiset(filterArticle: List<Article>){

    this.articles=filterArticle
    notifyDataSetChanged()
}


}

class ArticleHolder(item: View) : ViewHolder(item) {


    val textTitle: TextView = itemView.findViewById(R.id.tvTitle)
    val tvSource: TextView = itemView.findViewById(R.id.tvSource)
    val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
    val tvPubslishedAt: TextView = itemView.findViewById(R.id.tvPublishedAt)
    val imageView: ImageView = itemView.findViewById(R.id.ivArticleImage)
    val pb: ProgressBar = itemView.findViewById(R.id.pbImage)


}

interface ItemClickListener {
    fun onItemClick(position: Int,article: Article)
}