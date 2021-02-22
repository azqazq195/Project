package com.project.foret.ui.entrance.signIn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.project.foret.R
import com.project.foret.model.Member
import com.project.foret.model.SignUp
import com.project.foret.repository.ForetRepository
import com.project.foret.ui.entrance.signUp.SignUpActivity
import com.project.foret.ui.entrance.signUp.SignUpViewModel
import com.project.foret.ui.entrance.signUp.SignUpViewModelProviderFactory
import com.project.foret.ui.main.MainActivity
import com.project.foret.util.Resource
import com.project.foret.util.snackbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class SignInActivity : AppCompatActivity() {

    lateinit var viewModel: SignInViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutRoot: ConstraintLayout
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignIn: Button
    private lateinit var tvSignUp: TextView
    private lateinit var tvFindPassword: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val foretRepository = ForetRepository()
        val viewModelProviderFactory = SignInViewModelProviderFactory(foretRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(SignInViewModel::class.java)

        setFindViewById()
        setOnClickListener()
        setSignInResponse()

        autoLogin1()
    }
    private fun autoLogin1() {
        etEmail.setText("q@f.kk")
        etPassword.setText("qwe123")
        signIn()
    }
    private fun autoLogin2() {
        etEmail.setText("suth@gmail.com")
        etPassword.setText("qwe123")
        signIn()
    }


    private fun setFindViewById() {
        progressBar = findViewById(R.id.progressBar)
        layoutRoot = findViewById(R.id.layoutRoot)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnSignIn = findViewById(R.id.btnSignIn)
        tvSignUp = findViewById(R.id.tvSignUp)
        tvFindPassword = findViewById(R.id.tvFindPassword)
    }

    private fun setOnClickListener() {
        btnSignIn.setOnClickListener {
            signIn()
        }
        tvSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        tvFindPassword.setOnClickListener {
            layoutRoot.snackbar("비밀번호 찾기")
        }
    }

    private fun signIn() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (email == "") {
            layoutRoot.snackbar("이메일을 입력해 주세요.")
            return
        }
        if (password == "") {
            layoutRoot.snackbar("비밀번호를 입력해 주세요.")
            return
        }

        val member: HashMap<String, String> = HashMap()
        member["email"] = email
        member["password"] = password
        viewModel.signIn(member)
    }

    private fun setSignInResponse() {
        viewModel.signIn.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    if (response.data?.id == null) {
                        layoutRoot.snackbar(response.data?.message.toString())
                    } else {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("id", response.data.id)
                        startActivity(intent)
                        finish()
                    }
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

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

}