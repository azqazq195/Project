package com.project.foret.ui.main.annoymousForum

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.project.foret.ui.main.MainActivity
import com.project.foret.R
import com.project.foret.adapter.AnonymousBoardAdapter
import com.project.foret.repository.ForetRepository
import com.project.foret.ui.main.board.CreateBoardActivity
import com.project.foret.util.Resource
import com.project.foret.util.snackbar

class AnonymousForumFragment : Fragment(R.layout.fragment_anonymousforum) {

    lateinit var viewModel: AnonymousForumViewModel
    lateinit var anonymousBoardAdapter: AnonymousBoardAdapter

    lateinit var rvAnonyBoard: RecyclerView
    lateinit var tvRecent: TextView
    lateinit var tvLikeRank: TextView
    lateinit var tvCommentRank: TextView
    lateinit var progressBar: ProgressBar
    lateinit var btnBoardWrite: FloatingActionButton

    private val TAG = "AnonymousForumFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // initialize
        // ViewModel, Repository
        val foretRepository = ForetRepository()
        val viewModelProviderFactory = AnonymousForumViewModelProviderFactory(foretRepository)
        viewModel = ViewModelProvider(
            this,
            viewModelProviderFactory
        ).get(AnonymousForumViewModel::class.java)

        // layout
        progressBar = view.findViewById(R.id.progressBar)
        rvAnonyBoard = view.findViewById(R.id.rvAnonyBoard)
        tvRecent = view.findViewById(R.id.tvRecent)
        tvLikeRank = view.findViewById(R.id.tvLikeRank)
        tvCommentRank = view.findViewById(R.id.tvCommentRank)
        btnBoardWrite = view.findViewById(R.id.btnBoardWrite)

        // 정렬
        orderBy(1)

        setUpRecyclerView()
        setBoardData()
        setOnClickListener()
        setToolbar()
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


    private fun setOnClickListener() {
        anonymousBoardAdapter.setOnItemClickListener {
            view?.findNavController()
                ?.navigate(
                    R.id.action_anonymousForumFragment_to_boardFragment,
                    bundleOf(
                        "boardId" to it.id,
                        "isAnonymous" to true
                    )
                )
        }

        tvRecent.setOnClickListener { orderBy(1) }
        tvCommentRank.setOnClickListener { orderBy(2) }
        tvLikeRank.setOnClickListener { orderBy(3) }
        btnBoardWrite.setOnClickListener {
            val intent = Intent(context, CreateBoardActivity::class.java)
            intent.putExtra("isAnonymous", true)
            intent.putExtra("memberId", (activity as MainActivity).member?.id)
            intent.putExtra("memberName", (activity as MainActivity).member?.name)
            startActivityForResult(intent, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {
            when (resultCode) {
                AppCompatActivity.RESULT_OK -> {
                    val bundle = bundleOf(
                        "boardId" to data?.getLongExtra("boardId", 0L),
                        "isAnonymous" to true
                    )
                    view?.findNavController()
                        ?.navigate(
                            R.id.action_anonymousForumFragment_to_boardFragment,
                            bundle
                        )
                }
                AppCompatActivity.RESULT_CANCELED -> {
                    (activity as MainActivity).mainLayoutRoot.snackbar("게시글 생성 취소!")
                }
            }
        }
    }

    private fun orderBy(order: Int) {
        // viewModel.getAnonymousBoardList(order)
        when (order) {
            1 -> {
                viewModel.getAnonymousBoardList(1)
                tvRecent.setTextColor(Color.parseColor("#FF22997b"))
                tvRecent.setTypeface(tvRecent.typeface, Typeface.BOLD)
                tvCommentRank.setTextColor(Color.parseColor("#bbbbbb"))
                tvCommentRank.setTypeface(tvCommentRank.typeface, Typeface.NORMAL)
                tvLikeRank.setTextColor(Color.parseColor("#bbbbbb"))
                tvLikeRank.setTypeface(tvLikeRank.typeface, Typeface.NORMAL)
            }
            2 -> {
                viewModel.getAnonymousBoardList(2)
                tvRecent.setTextColor(Color.parseColor("#bbbbbb"))
                tvRecent.setTypeface(tvRecent.typeface, Typeface.NORMAL)
                tvCommentRank.setTextColor(Color.parseColor("#FF22997b"))
                tvCommentRank.setTypeface(tvCommentRank.typeface, Typeface.BOLD)
                tvLikeRank.setTextColor(Color.parseColor("#bbbbbb"))
                tvLikeRank.setTypeface(tvLikeRank.typeface, Typeface.NORMAL)
            }
            3 -> {
                viewModel.getAnonymousBoardList(1)
                tvRecent.setTextColor(Color.parseColor("#bbbbbb"))
                tvRecent.setTypeface(tvRecent.typeface, Typeface.NORMAL)
                tvCommentRank.setTextColor(Color.parseColor("#bbbbbb"))
                tvCommentRank.setTypeface(tvCommentRank.typeface, Typeface.NORMAL)
                tvLikeRank.setTextColor(Color.parseColor("#FF22997b"))
                tvLikeRank.setTypeface(tvLikeRank.typeface, Typeface.BOLD)
            }
        }
    }

    private fun setUpRecyclerView() {
        anonymousBoardAdapter = AnonymousBoardAdapter()

        rvAnonyBoard.apply {
            adapter = anonymousBoardAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun setBoardData() {
        viewModel.anonymousBoardList.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { boardResponse ->
                        anonymousBoardAdapter.differ.submitList(boardResponse.boards)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occured $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_anonymous, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.drawer_menu) {
            (activity as MainActivity).drawerLayout.openDrawer(GravityCompat.END)
        }
        return super.onOptionsItemSelected(item)
    }
}