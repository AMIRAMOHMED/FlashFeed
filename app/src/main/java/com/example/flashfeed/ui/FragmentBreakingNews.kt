package com.example.flashfeed.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
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

class FragmentBreakingNews : Fragment(), ItemClickListener, MenuProvider {
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: ArticleAdapter
    private lateinit var rv: RecyclerView
    private lateinit var pb: ProgressBar
    private lateinit var tec: TextView
    private lateinit var general: TextView
    private lateinit var sports: TextView
    private lateinit var health: TextView
    private var isClicked: Boolean = false
    private var isOpened: Boolean = false
    private var addingResponseList = arrayListOf<Article>()
    private lateinit var noWifi: ImageView
    private lateinit var noWifiText: TextView

    private val selectedBackground: Drawable? by lazy {
        ContextCompat.getDrawable(
            requireContext(),
            R.drawable.rounded_button
        )
    }
    private val defaultBackground: Drawable? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_breaking_news, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Breaking News"


        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.CREATED)

        val doa = NewsDatabase.getInstance(requireActivity()).newsDao
        val repository = NewsRepo(doa)
        val factory = NewsViewModelFac(repository, requireActivity().application)
        viewModel = factory.create(NewsViewModel::class.java)

        rv = view.findViewById(R.id.rvBreakingNews)
        pb = view.findViewById(R.id.paginationProgressBar)
        tec = view.findViewById(R.id.technology)
        general = view.findViewById(R.id.general)
        sports = view.findViewById(R.id.sport)
        health = view.findViewById(R.id.health)
        noWifi = view.findViewById(R.id.noWifi)
        noWifiText = view.findViewById(R.id.noWifiText)

        val cm =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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

    @SuppressLint("NotifyDataSetChanged")
    private fun loadBreakingNews() {
        viewModel.breakingNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        addingResponseList = newsResponse.articles as ArrayList<Article>
                        newsAdapter.setList(addingResponseList)
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
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadCategoryNews() {
        viewModel.categoryNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        addingResponseList = newsResponse.articles as ArrayList<Article>
                        newsAdapter.setList(addingResponseList)
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
        }
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
        val action =
            FragmentBreakingNewsDirections.actionFragmentBreakingNewsToFragmentArticle(article)
        view?.findNavController()?.navigate(action)
        Toast.makeText(context, "check ${article.title}", Toast.LENGTH_SHORT).show()

    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu, menu)
        val menuItem = menu.findItem(R.id.searchNews)
        val searchView = menuItem.actionView as? SearchView

        searchView?.setOnClickListener {
            val savedIcon = menu.findItem(R.id.savedNewsFrag)
            savedIcon.isVisible = false
            isOpened = true
        }

        searchView?.queryHint = "Search News"

        searchView?.setOnCloseListener {
            val savedIcon = menu.findItem(R.id.savedNewsFrag)
            savedIcon.isVisible = true
            isOpened = false
            false // Return false to allow default close behavior
        }

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(pO: String?): Boolean {
                newFilter(pO)
                return true
            }

            override fun onQueryTextChange(pO: String?): Boolean {

                newFilter(pO)
                return true

            }
        })

        super.onCreateOptionsMenu(menu, menuInflater)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun newFilter(pO: String?) {
        val newFilteredList = ArrayList<Article>()

        // Ensure the query is not null or empty
        if (!pO.isNullOrEmpty()) {
            for (article in addingResponseList) {
                // Check if the article title contains the query string (case-insensitive)
                article.title?.let {
                    if (it.contains(pO, ignoreCase = true)) {
                        newFilteredList.add(article)
                    }
                }
            }
        } else {
            // If the query is empty, reset the filtered list to show all articles
            newFilteredList.addAll(addingResponseList)
        }

        // Update the adapter with the filtered list
        newsAdapter.setList(newFilteredList)
        newsAdapter.notifyDataSetChanged()  // Ensure the list is refreshed in the UI
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {


        if (menuItem.itemId == R.id.savedNewsFrag) {

            view?.findNavController()
                ?.navigate(R.id.action_fragmentBreakingNews_to_fragmentSavedNews)
        }


        return true

    }
}