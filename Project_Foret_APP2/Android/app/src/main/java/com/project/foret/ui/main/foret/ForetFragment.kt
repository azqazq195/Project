package com.project.foret.ui.main.foret

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.project.foret.ui.main.MainActivity
import com.project.foret.R
import com.project.foret.adapter.BoardAdapter
import com.project.foret.model.Foret
import com.project.foret.repository.ForetRepository
import com.project.foret.ui.main.board.CreateBoardActivity
import com.project.foret.util.Constants.Companion.BASE_URL
import com.project.foret.util.Resource
import com.project.foret.util.snackbar

class ForetFragment : Fragment(R.layout.fragment_foret) {

    lateinit var viewModel: ForetViewModel
    lateinit var noticeAdapter: BoardAdapter
    lateinit var feedAdapter: BoardAdapter

    lateinit var progressBar: ProgressBar
    lateinit var layoutRoot: ConstraintLayout

    lateinit var ivForet: ImageView
    lateinit var btnForetSignIn: Button
    lateinit var btnForetSignUp: Button
    lateinit var btnForetModify: Button
    lateinit var tvForetName: TextView
    lateinit var tvForetTag: TextView
    lateinit var tvForetRegion: TextView
    lateinit var tvForetMember: TextView
    lateinit var tvForetMaster: TextView
    lateinit var tvForetRegDate: TextView
    lateinit var tvForetIntroduce: TextView
    lateinit var rvNotice: RecyclerView
    lateinit var rvFeed: RecyclerView
    lateinit var btnBoardWrite: FloatingActionButton

    private val TAG = "ForetFragment"

    private var id: Long = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // initialize
        // ViewModel, Repository
        val foretRepository = ForetRepository()
        val viewModelProviderFactory = ForetViewModelProviderFactory(foretRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(ForetViewModel::class.java)

        id = arguments?.getLong("foretId")!!

        viewModel.getForetDetails(id)
        viewModel.getBoardList(id, 1)
        viewModel.getBoardList(id, 3)

        setFindViewById(view)
        setUpRecyclerView()
        setDataObserve()
        setClickListener()
        setToolbar()
    }

    private fun setFindViewById(view: View) {
        layoutRoot = view.findViewById(R.id.layoutRoot)
        progressBar = view.findViewById(R.id.progressBar)
        ivForet = view.findViewById(R.id.ivForet)
        btnForetSignIn = view.findViewById(R.id.btnForetSignIn)
        btnForetSignUp = view.findViewById(R.id.btnForetSignUp)
        btnForetModify = view.findViewById(R.id.btnForetModify)
        tvForetName = view.findViewById(R.id.tvForetName)
        tvForetTag = view.findViewById(R.id.tvForetTag)
        tvForetRegion = view.findViewById(R.id.tvForetRegion)
        tvForetMember = view.findViewById(R.id.tvForetMember)
        tvForetMaster = view.findViewById(R.id.tvForetMaster)
        tvForetRegDate = view.findViewById(R.id.tvForetRegDate)
        tvForetIntroduce = view.findViewById(R.id.tvForetIntroduce)
        rvNotice = view.findViewById(R.id.rvForetTag)
        rvFeed = view.findViewById(R.id.rvFeed)
        btnBoardWrite = view.findViewById(R.id.btnBoardWrite)
    }

    private fun setToolbar() {
        setHasOptionsMenu(true)
        val mContext = (activity as MainActivity)
        mContext.toolbar.setBackgroundColor(
            ContextCompat.getColor(mContext, R.color.textForet)
        )
        mContext.ivToolbar.setImageDrawable(
            ContextCompat.getDrawable(mContext, R.drawable.foret_logo_white)
        )
    }

    private fun setClickListener() {
        noticeAdapter.setOnItemClickListener {
            view?.findNavController()
                ?.navigate(
                    R.id.action_foretFragment_to_boardFragment,
                    bundleOf(
                        "boardId" to it.id,
                        "isAnonymous" to false
                    )
                )
        }
        feedAdapter.setOnItemClickListener {
            view?.findNavController()
                ?.navigate(
                    R.id.action_foretFragment_to_boardFragment,
                    bundleOf(
                        "boardId" to it.id,
                        "isAnonymous" to false
                    )
                )
        }
        btnBoardWrite.setOnClickListener {
            val intent = Intent(context, CreateBoardActivity::class.java)
            intent.putExtra("foretId", id)
            intent.putExtra("isAnonymous", false)
            intent.putExtra("memberId", (activity as MainActivity).member?.id)
            intent.putExtra("memberName", (activity as MainActivity).member?.name)
            startActivityForResult(intent, 0)
        }
        btnForetSignUp.setOnClickListener {
            viewModel.signUp(id, (activity as MainActivity).member?.id!!)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {
            when (resultCode) {
                AppCompatActivity.RESULT_OK -> {
                    val bundle = bundleOf("boardId" to data?.getLongExtra("boardId", 0L))
                    view?.findNavController()
                        ?.navigate(
                            R.id.action_foretFragment_to_boardFragment,
                            bundle
                        )
                }
                AppCompatActivity.RESULT_CANCELED -> {
                    (activity as MainActivity).mainLayoutRoot.snackbar("게시글 생성 취소!")
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        noticeAdapter = BoardAdapter()
        feedAdapter = BoardAdapter()

        rvNotice.apply {
            adapter = noticeAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        rvFeed.apply {
            adapter = feedAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDataObserve() {
        viewModel.foret.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { foretResponse ->
                        // 포레를 불러오면
                        tvForetName.text = foretResponse.name
                        var tagList = ""
                        for (i in foretResponse.tags!!) {
                            tagList += "${i.tagName} "
                        }
                        tvForetTag.text = tagList
                        var regionList = ""
                        for (i in foretResponse.regions!!) {
                            regionList += "${i.regionSi} ${i.regionGu}, "
                        }
                        regionList = regionList.substring(0, regionList.length - 2)
                        tvForetRegion.text = regionList
                        tvForetMember.text =
                            "최대 인원 ${foretResponse.members?.size} / ${foretResponse.max_member.toString()}"
                        tvForetMaster.text = foretResponse.leader?.name.toString()
                        tvForetRegDate.text = foretResponse.reg_date.toString()
                        tvForetIntroduce.text = foretResponse.introduce
                        if (foretResponse.photos != null) {
                            Glide.with(this)
                                .load("${BASE_URL}${foretResponse.photos[0].dir}/${foretResponse.photos[0].filename}")
                                .into(ivForet)
                        }
                        checkMyStatus(foretResponse)
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
        viewModel.noticeBoardList.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { boardResponse ->
                        noticeAdapter.differ.submitList(boardResponse.boards)
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
        viewModel.feedBoardList.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { boardResponse ->
                        feedAdapter.differ.submitList(boardResponse.boards)
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
        viewModel.signUp.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { uploadResponse ->
                        layoutRoot.snackbar(uploadResponse.message)
                        viewModel.getForetDetails(id)
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

    private fun checkMyStatus(foret: Foret) {
        btnForetSignIn.visibility = View.INVISIBLE
        btnForetSignUp.visibility = View.INVISIBLE
        btnForetModify.visibility = View.INVISIBLE

        val memberId = (activity as MainActivity).member?.id
        val foretLeaderId = foret.leader?.id

        if (memberId == foretLeaderId) {
            btnForetModify.visibility = View.VISIBLE
            return
        }
        for(i in foret.members!!) {
            if(memberId == i.id) {
                btnForetSignIn.visibility = View.VISIBLE
                return
            }
        }
        btnForetSignUp.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_foret, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.drawer_menu) {
            (activity as MainActivity).drawerLayout.openDrawer(GravityCompat.END)
        }
        return super.onOptionsItemSelected(item)
    }
}