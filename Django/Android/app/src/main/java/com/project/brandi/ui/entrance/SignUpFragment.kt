package com.project.brandi.ui.entrance

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.project.brandi.R
import com.project.brandi.data.user.User
import com.project.brandi.data.user.UserRepository
import com.project.brandi.util.Resource
import com.project.brandi.util.snackBar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private lateinit var viewModel: EntranceViewModel

    private lateinit var rootLayout: ConstraintLayout
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignUp: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userRepository = UserRepository()
        val viewModelFactory = EntranceViewModelFactory(userRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(EntranceViewModel::class.java)

        setFindViewById(view)
        setOnClickListener()
        setCreateResponse()

        test()
    }

    @SuppressLint("SetTextI18n")
    private fun test() {
        etName.setText("name")
        etEmail.setText("email")
        etPassword.setText("password")
    }

    private fun setFindViewById(view: View) {
        rootLayout = view.findViewById(R.id.rootLayout)
        etName = view.findViewById(R.id.etName)
        etEmail = view.findViewById(R.id.etEmail)
        etPassword = view.findViewById(R.id.etPassword)
        btnSignUp = view.findViewById(R.id.btnSignUp)
    }

    private fun setOnClickListener() {
        btnSignUp.setOnClickListener {
            if (!isBlank()) {
                createUser()
            }
        }
    }

    private fun createUser() {
        val user = User(
            null,
            etName.text.toString(),
            etEmail.text.toString(),
            etPassword.text.toString()
        )

        val gson = Gson().toJson(user)

        viewModel.createUser(
            gson.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        )
    }

    private fun setCreateResponse() {
        viewModel.createUser.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    Log.e("TAG", "setCreateResponse: ${response.data}")
                    rootLayout.snackBar("회원가입")
                    findNavController().navigate(
                        R.id.action_signUpFragment_to_signInFragment,
                        bundleOf(
                            "email" to etEmail.text.toString(),
                            "password" to etPassword.text.toString()
                        )
                    )
                    this.onDestroy()
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
        if (etName.text.toString() == "") {
            rootLayout.snackBar("이름을 입력해주세요.")
            return true
        }
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