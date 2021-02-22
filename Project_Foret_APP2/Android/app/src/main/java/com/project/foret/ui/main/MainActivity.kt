package com.project.foret.ui.main

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.project.foret.R
import com.project.foret.model.Member
import com.project.foret.repository.ForetRepository
import com.project.foret.ui.entrance.signIn.SignInActivity
import com.project.foret.util.Constants.Companion.BASE_URL
import com.project.foret.util.Resource
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var drawerNavigationView: NavigationView
    private lateinit var foretNavHostFragment: Fragment
    lateinit var viewModel: MainViewModel
    private lateinit var progressBar: ProgressBar

    lateinit var drawerLayout: DrawerLayout
    lateinit var mainLayoutRoot: ConstraintLayout

    lateinit var toolbar: Toolbar
    lateinit var ivToolbar: ImageView

    private lateinit var ivDrawerCancel: ImageView
    private lateinit var tvLogout: TextView

    var member: Member? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val foretRepository = ForetRepository()
        val viewModelProviderFactory = MainViewModelProviderFactory(foretRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(MainViewModel::class.java)

        setFindViewById()

        viewModel.getMember(intent.getLongExtra("id", 0L))

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        foretNavHostFragment =
            supportFragmentManager.findFragmentById(R.id.foretNavHostFragment) as NavHostFragment
        bottomNavigationView.setupWithNavController(foretNavHostFragment.findNavController())
        setOnClickListener()
        setMemberData()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END)
            return
        }
        cancelDialog()
    }

    private fun cancelDialog() {
        val alertDialog: AlertDialog = let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setMessage("Foret을 종료하시겠습니까?")
                setPositiveButton("종료",
                    DialogInterface.OnClickListener { dialog, id ->
                        super.onBackPressed()
                        finish()
                    })
                setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            }
            builder.create()
        }
        alertDialog.show()
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun setDrawerMember() {
        val format = SimpleDateFormat("yyyy-MM-dd")
        val reg_date: String = format.format(member?.reg_date)

        val header: View = drawerNavigationView.getHeaderView(0)
        header.findViewById<TextView>(R.id.tvDrawerMemberEmail).text = member?.email
        header.findViewById<TextView>(R.id.tvDrawerMemberName).text = member?.name
        header.findViewById<TextView>(R.id.tvDrawerMemberRegDate).text = "포레 가입일: $reg_date"
        if(member?.photos != null) {
            Glide.with(this)
                .load("${BASE_URL}${member?.photos?.get(0)?.dir}/${member?.photos?.get(0)?.filename}")
                .thumbnail(0.1f)
                .into(header.findViewById(R.id.ivDrawerProfile))
        }

    }

    private fun setMemberData() {
        viewModel.member.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    member = response.data!!
                    member?.id = intent.getLongExtra("id", 0L)
                    setDrawerMember()
                }
                is Resource.Error -> {
                    hideProgressBar()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun setFindViewById() {
        progressBar = findViewById(R.id.progressBar)
        drawerLayout = findViewById(R.id.drawerLayout)
        mainLayoutRoot = findViewById(R.id.mainLayoutRoot)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        drawerNavigationView = findViewById(R.id.drawerNavigationView)
        toolbar = findViewById(R.id.toolbar)
        ivToolbar = findViewById(R.id.ivToolbar)

        ivDrawerCancel = findViewById(R.id.ivDrawerCancel)
        tvLogout = findViewById(R.id.tvLogout)
    }

    private fun setOnClickListener() {
        ivDrawerCancel.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.END)
        }
        tvLogout.setOnClickListener {
            Log.e("TAG", "setOnClickListener: logout")
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }
}