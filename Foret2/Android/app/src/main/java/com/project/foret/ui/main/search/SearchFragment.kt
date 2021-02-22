package com.project.foret.ui.main.search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.foret.R
import com.project.foret.adapter.ForetAdapter
import com.project.foret.repository.ForetRepository
import com.project.foret.ui.main.MainActivity
import com.project.foret.ui.main.foret.CreateForetActivity
import com.project.foret.util.Resource
import com.project.foret.util.snackbar

class SearchFragment : Fragment(R.layout.fragment_search) {

    lateinit var viewModel: SearchViewModel
    lateinit var foretAdapter: ForetAdapter
    lateinit var rvForetRank: RecyclerView
    lateinit var ivCreateForet: ImageView
    lateinit var tvNullSearchResult: TextView

    lateinit var clMyTag: ConstraintLayout
    lateinit var clTagRank: ConstraintLayout
    lateinit var clCreateForet: ConstraintLayout
    lateinit var boardView: View
    lateinit var clForetRank: ConstraintLayout

    lateinit var tvForetRank: TextView

    lateinit var animInRight: Animation
    lateinit var animInBottom: Animation

    val TAG = "SearchFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // initialize
        // ViewModel, Repository
        val foretRepository = ForetRepository()
        val viewModelProviderFactory = SearchViewModelProviderFactory(foretRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(SearchViewModel::class.java)

        viewModel.getRankForetsByPage(0, 5)

        setFindViewById(view)
        setToolbar()
        setUpRecyclerView()
        setOnClickListener()
        setDataObserve()
        setAnimation()
    }

    private fun setDataObserve() {
        viewModel.rankForets.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    // hideProgressBar()
                    response.data?.let { foretResponse ->
                        foretAdapter.differ.submitList(foretResponse.forets)
                    }
                }
                is Resource.Error -> {
                    // hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occured $message")
                    }
                }
                is Resource.Loading -> {
                    // showProgressBar()
                }
            }
        })
        viewModel.searchForets.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    // hideProgressBar()
                    response.data?.let { foretResponse ->
                        if (foretResponse.total == 0) {
                            tvNullSearchResult.visibility = View.VISIBLE
                            rvForetRank.visibility = View.GONE
                        } else {
                            tvNullSearchResult.visibility = View.GONE
                            rvForetRank.visibility = View.VISIBLE
                            foretAdapter.differ.submitList(foretResponse.forets)
                        }
                    }
                }
                is Resource.Error -> {
                    // hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occured $message")
                    }
                }
                is Resource.Loading -> {
                    // showProgressBar()
                }
            }
        })
    }

    private fun setUpRecyclerView() {
        foretAdapter = ForetAdapter()

        rvForetRank.apply {
            adapter = foretAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun setFindViewById(view: View) {
        ivCreateForet = view.findViewById(R.id.ivCreateForet)
        rvForetRank = view.findViewById(R.id.rvForetRank)
        tvForetRank = view.findViewById(R.id.tvForetRank)
        tvNullSearchResult = view.findViewById(R.id.tvNullSearchResult)

        clMyTag = view.findViewById(R.id.clMyTag)
        clTagRank = view.findViewById(R.id.clTagRank)
        clCreateForet = view.findViewById(R.id.clCreateForet)
        clForetRank = view.findViewById(R.id.clForetRank)
        boardView = view.findViewById(R.id.view4)
    }

    private fun setAnimation() {
        animInRight = AnimationUtils.loadAnimation(
            (activity as MainActivity),
            R.anim.slide_in_right
        )
        animInBottom = AnimationUtils.loadAnimation(
            (activity as MainActivity),
            R.anim.slide_in_bottom
        )
    }

    private fun setOnClickListener() {
        ivCreateForet.setOnClickListener {
            val intent = Intent(context, CreateForetActivity::class.java)
            intent.putExtra("memberId", (activity as MainActivity).member?.id)
            startActivityForResult(intent, 0)
        }

        foretAdapter.setOnItemClickListener {
            val bundle = bundleOf("foretId" to it.id)
            view?.findNavController()
                ?.navigate(
                    R.id.action_searchFragment_to_foretFragment,
                    bundle
                )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {
            when (resultCode) {
                AppCompatActivity.RESULT_OK -> {
                    val bundle = bundleOf("foretId" to data?.getLongExtra("foretId", 0L))
                    view?.findNavController()
                        ?.navigate(
                            R.id.action_searchFragment_to_foretFragment,
                            bundle
                        )
                }
                AppCompatActivity.RESULT_CANCELED -> {
                    (activity as MainActivity).mainLayoutRoot.snackbar("포레 생성 취소!")
                }
            }
        }
    }

    private fun setToolbar() {
        setHasOptionsMenu(true)
        val mContext = (activity as MainActivity)
        mContext.toolbar.setBackgroundColor(
            ContextCompat.getColor(mContext, R.color.white)
        )
        mContext.ivToolbar.setImageDrawable(
            ContextCompat.getDrawable(mContext, R.drawable.foret_logo)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_search, menu)
        val menuItem = menu.findItem(R.id.action_search)
        val searchView = menuItem.actionView as SearchView
        searchView.queryHint = "포레의 이름으로 찾기"

        menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                // 검색 누를 때
                clMyTag.visibility = View.GONE
                clTagRank.visibility = View.GONE
                clCreateForet.visibility = View.GONE
                clForetRank.visibility = View.GONE
                boardView.visibility = View.GONE

                clForetRank.visibility = View.VISIBLE
                clForetRank.startAnimation(animInBottom)
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                // 검색 닫을 때
                clMyTag.visibility = View.VISIBLE
                clTagRank.visibility = View.VISIBLE
                clCreateForet.visibility = View.VISIBLE
                boardView.visibility = View.VISIBLE
                clMyTag.startAnimation(animInRight)
                clTagRank.startAnimation(animInRight)
                clCreateForet.startAnimation(animInRight)
                tvForetRank.text = "인기 포레"
                viewModel.getRankForetsByPage(0, 5)
                return true
            }
        })
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            @SuppressLint("SetTextI18n")
            override fun onQueryTextSubmit(query: String): Boolean {
                tvForetRank.text = "\"${query}\" 검색결과"
                foretAdapter.differ.submitList(null)
                viewModel.getSearchForets(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.drawer_menu -> {
                (activity as MainActivity).drawerLayout.openDrawer(GravityCompat.END)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}