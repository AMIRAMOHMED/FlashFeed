package com.example.flashfeed.ui
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flashfeed.R
import com.example.flashfeed.adapters.SavedArticleAdapter
import com.example.flashfeed.mvvm.NewsRepo
import com.example.flashfeed.mvvm.NewsViewModel
import com.example.flashfeed.mvvm.NewsViewModelFac
import com.example.newsapiapp.mvvm.NewsDatabase


class FragmentSavedNews : Fragment() , MenuProvider {
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: SavedArticleAdapter
    private lateinit var rv: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_news, container, false)
    }

override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    (activity as AppCompatActivity).supportActionBar?.title = " Saved News"


    val menuHost: MenuHost = requireActivity()

    menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.CREATED)

    rv = view.findViewById(R.id.rvSavedNews)

    val dao = NewsDatabase.getInstance(requireActivity()).newsDao
    val repository = NewsRepo(dao)
    val factory = NewsViewModelFac(repository, requireActivity().application)
    viewModel = ViewModelProvider(this, factory)[NewsViewModel::class.java]
    newsAdapter = SavedArticleAdapter()


    viewModel.getSavedNews.observe(viewLifecycleOwner) {

        newsAdapter.setlist(it)
        setUpRecyclerView()


    }


}

    private fun setUpRecyclerView() {
        rv.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu, menu)
        val searchIcon=menu.findItem(R.id.searchNews)
        val savedIcon=menu.findItem(R.id.savedNewsFrag)
        searchIcon.setVisible(false)
        savedIcon.setVisible(false)
        super.onCreateOptionsMenu(menu, menuInflater)

    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
if (
    menuItem.itemId == R.id.deleteAll
) {
    val builder=AlertDialog.Builder(requireContext())
    builder.setTitle("Delete All Articles")
    builder.setMessage("Are you sure you want to delete all articles?")
    builder.setPositiveButton("Yes") { dialog, which ->
        viewModel.deleteArticle()
        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
        view?.findNavController()?.navigate(R.id.action_fragmentSavedNews_to_fragmentBreakingNews)
    }
    builder.setNegativeButton("No") { dialog, which ->


dialog.dismiss()
    }
val dialog=builder.create()
    dialog.show()

}


        return true
    }
}