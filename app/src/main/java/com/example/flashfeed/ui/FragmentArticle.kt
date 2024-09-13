package com.example.flashfeed.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.flashfeed.R
import com.example.flashfeed.Utils
import com.example.flashfeed.dp.SavedArticle
import com.example.flashfeed.dp.Source
import com.example.flashfeed.mvvm.NewsRepo
import com.example.flashfeed.mvvm.NewsViewModel
import com.example.flashfeed.mvvm.NewsViewModelFac
import com.example.newsapiapp.mvvm.NewsDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FragmentArticle : Fragment() {

    private lateinit var viewModel: NewsViewModel


    private lateinit var args: FragmentArticleArgs

    private var stringCheck = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        (activity as AppCompatActivity).supportActionBar?.title = "Article View"


        val dao = NewsDatabase.getInstance(requireActivity()).newsDao
        val repository = NewsRepo(dao)
        val factory = NewsViewModelFac(repository, requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[NewsViewModel::class.java]

        args = FragmentArticleArgs.fromBundle(requireArguments())

        // initialize the views of Art Frag


        val fab = view.findViewById<FloatingActionButton>(R.id.fab)

        val textTitle: TextView = view.findViewById(R.id.tvTitle)
        val tSource: TextView = view.findViewById(R.id.tvSource)
        val tDescription: TextView = view.findViewById(R.id.tvDescription)
        val tPubslishedAt: TextView = view.findViewById(R.id.tvPublishedAt)
        val imageView: ImageView = view.findViewById(R.id.articleImage)

        val source = Source(args.article.source!!.id, args.article.source!!.name)


        textTitle.text = (args.article.title)
        tSource.text = (source.name)
        tDescription.text = args.article.description
        tPubslishedAt.text = Utils.DateFormat(args.article.publishedAt)

        Glide.with(requireActivity()).load(args.article.urlToImage).into(imageView)


        // all the news are saved in the list
        viewModel.getSavedNews.observe(viewLifecycleOwner) {


            for (i in it) {


                if (args.article.title == i.title) {

                    stringCheck = i.title


                }


            }


        }


        fab.setOnClickListener {


            if (args.article.title == stringCheck) {


                Log.e("fragArg", "exists")

                // Toast.makeText(context, "Article exists in saved list", Toast.LENGTH_SHORT).show()

            } else {


                viewModel.insertArticle(
                    SavedArticle(
                        0, args.article.description!!,
                        args.article.publishedAt!!, source,
                        args.article.title!!, args.article.url!!,
                        args.article.urlToImage!!
                    )
                )



                Log.e("fragArg", "saved")
                // Toast.makeText(context, "SAVED SUCCESSFULLY", Toast.LENGTH_SHORT).show()
                view.findNavController().navigate(R.id.action_fragmentArticle_to_fragmentSavedNews)


            }


        }


    }


}