package com.example.flashfeed.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flashfeed.R
import com.example.flashfeed.adapters.ArticleAdapter
import com.example.flashfeed.adapters.ItemClickListener
import com.example.flashfeed.dp.Article
import com.example.flashfeed.mvvm.NewsRepo
import com.example.flashfeed.mvvm.NewsViewModel
import com.example.flashfeed.mvvm.NewsViewModelFac
import com.example.flashfeed.wrapper.Resource
import com.example.newsapiapp.mvvm.NewsDatabase

class FragmentBreakingNews : Fragment(), ItemClickListener {
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: ArticleAdapter
    lateinit var rv: RecyclerView
    lateinit var pb: ProgressBar
    lateinit var tec: TextView
    lateinit var general: TextView
    lateinit var sports: TextView
    lateinit var health: TextView
    var isClicked: Boolean = false

    lateinit var noWifi: ImageView
    lateinit var noWifiText: TextView
    var addingResponselist = arrayListOf<Article>()

    private val selectedBackground: Drawable? by lazy { ContextCompat.getDrawable(requireContext(), R.drawable.rounded_button) }
    private val defaultBackground: Drawable? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_breaking_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val doa = NewsDatabase.getInstance(requireActivity()).newsDao
        val repository = NewsRepo(doa)
        val factory = NewsViewModelFac(repository, requireActivity().application)
        viewModel = factory.create(NewsViewModel::class.java)

        rv = view.findViewById(R.id.rvBreakingNews)
        pb = view.findViewById(R.id.paginationProgressBar)
        tec = view.findViewById(R.id.technology)
        general = view.findViewById<TextView>(R.id.general)
        sports = view.findViewById<TextView>(R.id.sport)
        health = view.findViewById<TextView>(R.id.health)
        noWifi = view.findViewById<ImageView>(R.id.noWifi)
        noWifiText = view.findViewById<TextView>(R.id.noWifiText)

        val cm = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nInfo = cm.activeNetworkInfo
        if (nInfo != null && nInfo.isConnected) {
            setUpRecyclerView()
            loadBreakingNews()
            isClicked = true
        } else {
            noWifi.visibility = View.VISIBLE
            noWifiText.visibility = View.VISIBLE
        }

        val catListener = View.OnClickListener {
            when (it.id) {
                R.id.technology -> {
                    viewModel.getCategory("technology")
                    loadCategoryNews()
                    isClicked = true
                    selectCategory(tec)
                }
                R.id.general -> {
                    viewModel.getCategory("general")
                    loadCategoryNews()
                    isClicked = true
                    selectCategory(general)
                }
                R.id.sport -> {
                    viewModel.getCategory("sports")
                    loadCategoryNews()
                    isClicked = true
                    selectCategory(sports)
                }
                R.id.health -> {
                    viewModel.getCategory("health")
                    loadCategoryNews()
                    isClicked = true
                    selectCategory(health)
                }
            }
        }

        tec.setOnClickListener(catListener)
        general.setOnClickListener(catListener)
        sports.setOnClickListener(catListener)
        health.setOnClickListener(catListener)
    }

    private fun selectCategory(selectedTextView: TextView) {
        // Reset all backgrounds to default
        tec.background = defaultBackground
        general.background = defaultBackground
        sports.background = defaultBackground
        health.background = defaultBackground

        // Set the selected background to the clicked category
        selectedTextView.background = selectedBackground
    }

    private fun loadBreakingNews() {
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        addingResponselist = newsResponse.articles as ArrayList<Article>
                        newsAdapter.setList(addingResponselist)
                        Log.i("BreakingFragment", newsResponse.articles[1].toString())
                        newsAdapter.notifyDataSetChanged()
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.data?.let { message ->
                        Log.i("BreakingFragment", message.toString())
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun loadCategoryNews() {
        viewModel.categoryNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        addingResponselist = newsResponse.articles as ArrayList<Article>
                        newsAdapter.setList(addingResponselist)
                        newsAdapter.notifyDataSetChanged()
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.data?.let { message ->
                        Log.i("CategoryFragment", message.toString())
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun setUpRecyclerView() {
        newsAdapter = ArticleAdapter()
        newsAdapter.setOnItemClickListener(this)
        rv.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun showProgressBar() {
        pb.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        pb.visibility = View.INVISIBLE
    }

    override fun onItemClick(position: Int, article: Article) {
        // Handle item click
    }
}