package com.project.brandi.ui.entrance

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navArgs
import com.google.gson.Gson
import com.project.brandi.App
import com.project.brandi.R
import com.project.brandi.data.product.Product
import com.project.brandi.data.user.User
import com.project.brandi.data.user.UserRepository
import com.project.brandi.ui.info.InfoFragment
import com.project.brandi.util.Resource
import com.project.brandi.util.snackBar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private lateinit var viewModel: EntranceViewModel

    private lateinit var rootLayout: ConstraintLayout
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignIn: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userRepository = UserRepository()
        val viewModelFactory = EntranceViewModelFactory(userRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(EntranceViewModel::class.java)

        setFindViewById(view)
        setOnClickListener()
        setLoginResponse()

        setEmailAndPassword()
        test()
    }

    private fun test() {
        etEmail.setText("azqazq195@gmail.com2")
        etPassword.setText("qwe123")
    }

    @SuppressLint("SetTextI18n")
    private fun setEmailAndPassword() {
        val id = arguments?.getString("email", "")
        val password = arguments?.getString("password", "")

        if(id == "" && password == "") {
            etEmail.setText("azqazq195@gmail.com")
            etPassword.setText("qwe123")
        } else {
            etEmail.setText(id)
            etPassword.setText(password)
        }
    }

    private fun setFindViewById(view: View) {
        rootLayout = view.findViewById(R.id.rootLayout)
        etEmail = view.findViewById(R.id.etEmail)
        etPassword = view.findViewById(R.id.etPassword)
        btnSignIn = view.findViewById(R.id.btnSignIn)
    }

    private fun setOnClickListener() {
        btnSignIn.setOnClickListener {
            if (!isBlank()) {
                login()
            }
        }
    }

    private fun login() {
        val user = User(
            null,
            null,
            etEmail.text.toString(),
            etPassword.text.toString()
        )

        val gson = Gson().toJson(user)

        viewModel.loginUser(
            gson.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        )
    }

    private fun setLoginResponse() {
        viewModel.loginUser.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        rootLayout.snackBar(it.message)
                        if (it.message == "Sign In Complete") {
                            App.prefs.putString("token", it.user.token!!)
                            App.prefs.putString("name", it.user.name!!)
                            App.prefs.putString("email", it.user.email!!)
                            findNavController().navigate(R.id.action_signInFragment_to_infoFragment)
                            this.onDestroy()
                        }
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
//        progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
//        progressBar.visibility = View.VISIBLE
    }

    private fun isBlank(): Boolean {
        if (etEmail.text.toString() == "") {
            rootLayout.snackBar("이메일을 입력해주세요.")
            return true
        }
        if (etPassword.text.toString() == "") {
            rootLayout.snackBar("비밀번호를 입력해주세요.")
            return true
        }
        return false
    }
}