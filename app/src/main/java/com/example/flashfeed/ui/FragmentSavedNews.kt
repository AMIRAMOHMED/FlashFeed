package com.example.flashfeed.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flashfeed.R
import com.example.flashfeed.adapters.ArticleAdapter
import com.example.flashfeed.adapters.SavedArticleAdapter
import com.example.flashfeed.mvvm.NewsRepo
import com.example.flashfeed.mvvm.NewsViewModel
import com.example.flashfeed.mvvm.NewsViewModelFac
import com.example.newsapiapp.mvvm.NewsDatabase


class FragmentSavedNews : Fragment() {
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: SavedArticleAdapter
    lateinit var rv: RecyclerView
    lateinit var pb: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_news, container, false)
    }

override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    (activity as AppCompatActivity).supportActionBar?.setTitle(" Saved News")
    rv = view.findViewById(R.id.rvSavedNews)

    val dao = NewsDatabase.getInstance(requireActivity()).newsDao
    val repository = NewsRepo(dao)
    val factory = NewsViewModelFac(repository, requireActivity().application)
    viewModel = ViewModelProvider(this, factory)[NewsViewModel::class.java]
    newsAdapter = SavedArticleAdapter()


    viewModel.getSavedNews.observe(viewLifecycleOwner, Observer {

        newsAdapter.setlist(it)
        setUpRecyclerView()


    })


}

    private fun setUpRecyclerView() {
        rv.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }
}