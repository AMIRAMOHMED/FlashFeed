package com.example.flashfeed.ui
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
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
    var addingResponselist = arrayListOf<Article>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
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

        val cm = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nInfo = cm.activeNetworkInfo
        if (nInfo !=null && nInfo.isConnected){
            setUpRecyclerView()
            loadBreakingNews()
            }

        }
    private fun loadCategoryNews() {

        viewModel.categoryNews.observe(viewLifecycleOwner, Observer {response->


            when (response){

                is Resource.Success->{
                    hideProgressBar()
                    response.data?.let{newsresponse->

                        addingResponselist = newsresponse.articles as ArrayList<Article>
//                        newsAdapter.setlist(newsresponse.articles)

                    }
                }


                is Resource.Error->{
                    hideProgressBar()
                    response.message?.let{messsage->
                        Log.i("BREAKING FRAG", messsage.toString())

                    }
                }

                is Resource.Loading->{
                    showProgressBar()
                }

            }




        })
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
                        newsAdapter.notifyDataSetChanged() // Ensure this is called



                    }

                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.data?.let { message ->
                        {
                            Log.i("BreakingFragment", message.toString())
                        }

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
        TODO("Not yet implemented")
    }
}