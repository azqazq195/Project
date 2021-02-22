package com.project.foret.ui.entrance.signUp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.project.foret.R
import com.project.foret.repository.ForetRepository
import com.project.foret.util.Resource
import com.project.foret.util.snackbar
import java.util.*

class SignUpActivity : AppCompatActivity() {

    private val DELAY = 1000L

    private val explainText =
        arrayOf(
            "이름을 입력해주세요.",
            "닉네임을 입력해주세요.",
            "이메일을 입력해주세요.",
            "생일을 입력해주세요.",
            "비밀번호를 입력해주세요.",
            "비밀번호를 확인해주세요."
        )
    private val errorText =
        arrayOf(
            "이름을 입력해주세요.",
            "닉네임을 입력해주세요.",
            "이메일을 입력해주세요.",
            "생년월일 8자리를 입력해주세요.",
            "숫자, 영문 조합 6자리",
            "비밀번호가 다릅니다."
        )
    private val hintText =
        arrayOf(
            "문성하", "귤맘", "foret@gmail.com", "20210207", "숫자, 영문 조합 6자리", "비밀번호 확인"
        )
    private val validation =
        arrayOf(
            // 이름
            Regex("^[A-z|가-힣]([A-z|가-힣]*){2,4}$"),
            // 닉네임
            Regex("^[A-z|가-힣|0-9]([A-z|가-힣|0-9]*){2,6}$"),
            // 이메일
            Regex("^[A-z|0-9]([A-z|0-9]*)(@)([A-z]*)(\\.)([a-zA-Z]){2,3}$"),
            // 생년월일
            Regex("^[0-9]{8}$"),
            // 비밀번호
            Regex("^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#$%^&*])(?=.*[0-9!@#$%^&*])[A-Za-z[0-9]$@$!%*#?&].{5,12}$"),
            Regex("^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#$%^&*])(?=.*[0-9!@#$%^&*])[A-Za-z[0-9]$@$!%*#?&].{5,12}$")
        )

    lateinit var layoutRoot: LinearLayout

    lateinit var layoutPrevNext: LinearLayout
    lateinit var layoutNextBtn: LinearLayout

    lateinit var layoutName: LinearLayout
    lateinit var layoutNickname: LinearLayout
    lateinit var layoutEmail: LinearLayout
    lateinit var layoutBirth: LinearLayout
    lateinit var layoutPassword1: LinearLayout
    lateinit var layoutPassword2: LinearLayout
    lateinit var layoutInputs: Array<LinearLayout>

    lateinit var tvPrevious: TextView
    lateinit var tvNext: TextView

    lateinit var tvExplain: TextView

    lateinit var tvNameError: TextView
    lateinit var tvNicknameError: TextView
    lateinit var tvEmailError: TextView
    lateinit var tvBirthError: TextView
    lateinit var tvPasswordError1: TextView
    lateinit var tvPasswordError2: TextView
    lateinit var tvErrors: Array<TextView>

    lateinit var etName: EditText
    lateinit var etNickname: EditText
    lateinit var etEmail: EditText
    lateinit var etBirth: EditText
    lateinit var etPassword1: EditText
    lateinit var etPassword2: EditText
    lateinit var etInputs: Array<EditText>

    lateinit var ivCheckName: ImageView
    lateinit var ivCheckNickname: ImageView
    lateinit var ivCheckEmail: ImageView
    lateinit var ivCheckBirth: ImageView
    lateinit var ivCheckPassword1: ImageView
    lateinit var ivCheckPassword2: ImageView
    lateinit var ivChecks: Array<ImageView>

    lateinit var btnNext: Button

    lateinit var animInRight: Animation
    lateinit var animInLeft: Animation
    lateinit var animOutRight: Animation
    lateinit var animOutLeft: Animation

    private var position = 0
    private var prevPosition = 0
    private var isFirst = true
    lateinit var viewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val foretRepository = ForetRepository()
        val viewModelProviderFactory = SignUpViewModelProviderFactory(foretRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(SignUpViewModel::class.java)

        setFindViewById()
        setDefaultView()
        setAnimation()
        setOnClickListener()
        setText()
        addTextChangedListener()
        setCheckEmailObserve()
    }

    private fun setAnimation() {
        animInRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)
        animInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
        animOutRight = AnimationUtils.loadAnimation(this, R.anim.slide_out_right)
        animOutLeft = AnimationUtils.loadAnimation(this, R.anim.slide_out_left)
    }

    private fun setFindViewById() {
        layoutPrevNext = findViewById(R.id.layoutPrevNext)
        layoutNextBtn = findViewById(R.id.layoutNextBtn)

        layoutRoot = findViewById(R.id.layoutRoot)

        layoutName = findViewById(R.id.layoutTag)
        layoutNickname = findViewById(R.id.layoutNickname)
        layoutEmail = findViewById(R.id.layoutEmail)
        layoutBirth = findViewById(R.id.layoutBirth)
        layoutPassword1 = findViewById(R.id.layoutPassword1)
        layoutPassword2 = findViewById(R.id.layoutPassword2)
        layoutInputs = arrayOf(
            layoutName,
            layoutNickname,
            layoutEmail,
            layoutBirth,
            layoutPassword1,
            layoutPassword2
        )

        tvPrevious = findViewById(R.id.tvPrevious)
        tvNext = findViewById(R.id.tvNext)

        tvExplain = findViewById(R.id.tvExplain)

        tvNameError = findViewById(R.id.textView1)
        tvNicknameError = findViewById(R.id.tvNicknameError)
        tvEmailError = findViewById(R.id.tvEmailError)
        tvBirthError = findViewById(R.id.tvBirthError)
        tvPasswordError1 = findViewById(R.id.tvPasswordError1)
        tvPasswordError2 = findViewById(R.id.tvPasswordError2)
        tvErrors = arrayOf(
            tvNameError,
            tvNicknameError,
            tvEmailError,
            tvBirthError,
            tvPasswordError1,
            tvPasswordError2
        )

        etName = findViewById(R.id.etName)
        etNickname = findViewById(R.id.etNickname)
        etEmail = findViewById(R.id.etEmail)
        etBirth = findViewById(R.id.etBirth)
        etPassword1 = findViewById(R.id.etPassword1)
        etPassword2 = findViewById(R.id.etPassword2)
        etInputs = arrayOf(
            etName,
            etNickname,
            etEmail,
            etBirth,
            etPassword1,
            etPassword2
        )

        ivCheckName = findViewById(R.id.ivCheckName)
        ivCheckNickname = findViewById(R.id.ivCheckNickname)
        ivCheckEmail = findViewById(R.id.ivCheckEmail)
        ivCheckBirth = findViewById(R.id.ivCheckBirth)
        ivCheckPassword1 = findViewById(R.id.ivCheckPassword1)
        ivCheckPassword2 = findViewById(R.id.ivCheckPassword2)
        ivChecks = arrayOf(
            ivCheckName,
            ivCheckNickname,
            ivCheckEmail,
            ivCheckBirth,
            ivCheckPassword1,
            ivCheckPassword2,
        )

        btnNext = findViewById(R.id.btnNext)
    }

    private fun setDefaultView() {
        for (i in layoutInputs) {
            i.visibility = View.GONE
        }
        for (i in tvErrors) {
            i.visibility = View.INVISIBLE
        }
        layoutInputs[0].visibility = View.VISIBLE
        layoutPrevNext.visibility = View.VISIBLE
        layoutNextBtn.visibility = View.GONE
        tvPrevious.visibility = View.INVISIBLE
    }

    private fun setOnClickListener() {
        tvPrevious.setOnClickListener {
            movePrevLayout()
        }
        tvNext.setOnClickListener {
            moveNextLayout()
        }
        btnNext.setOnClickListener {
            val name = etName.text.toString()
            val nickname = etNickname.text.toString()
            val email = etEmail.text.toString()
            val birth = etBirth.text.toString()
            val password = etPassword1.text.toString()

            val intent = Intent(this, SignUpInfoActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("nickname", nickname)
            intent.putExtra("email", email)
            intent.putExtra("birth", birth)
            intent.putExtra("password", password)
            startActivity(intent)
            finish()
        }
    }

    private fun movePrevLayout() {
        if (position > 0) {
            layoutInputs[position].clearAnimation()
            layoutInputs[prevPosition].clearAnimation()
            prevPosition = position--
            changeLayout(false)
            setNextBtnView()
        }
    }

    private fun moveNextLayout() {
        if (position < layoutInputs.size - 1) {
            layoutInputs[position].clearAnimation()
            layoutInputs[prevPosition].clearAnimation()
            prevPosition = position++
            if (position == layoutInputs.size - 1) isFirst = false
            changeLayout(true)
            setNextBtnView()
        }
    }

    private fun changeLayout(isNext: Boolean) {
        layoutInputs[position].visibility = View.VISIBLE
        layoutInputs[prevPosition].visibility = View.GONE
        if (isNext) {
            layoutInputs[position].startAnimation(animInRight)
            layoutInputs[prevPosition].startAnimation(animOutLeft)
        } else {
            layoutInputs[position].startAnimation(animInLeft)
            layoutInputs[prevPosition].startAnimation(animOutRight)
        }

        if (position == 0) tvPrevious.visibility = View.INVISIBLE
        else tvPrevious.visibility = View.VISIBLE

        if (position == layoutInputs.size - 1) tvNext.visibility = View.INVISIBLE
        else tvNext.visibility = View.VISIBLE

        setExplainText()
    }

    private fun addTextChangedListener() {
        for (i in etInputs.indices) {
            etInputs[i].addTextChangedListener(object : TextWatcher {
                var timer = Timer()
                override fun afterTextChanged(s: Editable) {
                    timer.cancel()
                    timer = Timer()
                    timer.schedule(object : TimerTask() {
                        override fun run() {
                            runOnUiThread {
                                if (s.matches(validation[i])) {
                                    if (etInputs[i] != etPassword2) {
                                        // 평소
                                        if (etInputs[i] == etEmail) {
                                            // 이메일
                                            Log.e("ss", "run: emailcheck")
                                            viewModel.checkEmail(etEmail.text.toString().trim())
                                        } else {
                                            // 나머지
                                            tvErrors[i].visibility = View.INVISIBLE
                                            ivChecks[i].visibility = View.VISIBLE
                                            setExplainText()
                                            requestFocus()
                                        }

                                    } else {
                                        // 비번 확인
                                        if (etPassword1.text.toString() == etPassword2.text.toString()) {
                                            tvErrors[i].visibility = View.INVISIBLE
                                            ivChecks[i].visibility = View.VISIBLE
                                            setExplainText()
                                            setNextBtnView()
                                        } else {
                                            tvErrors[i].visibility = View.VISIBLE
                                        }
                                    }
                                } else {
                                    tvErrors[i].visibility = View.VISIBLE
                                }
                            }
                        }
                    }, DELAY)
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    ivChecks[i].visibility = View.INVISIBLE
                    if (ivChecks[i] == ivCheckPassword1) {
                        etPassword2.setText("")
                        ivCheckPassword2.visibility = View.INVISIBLE
                    }
                    setExplainText()
                    setNextBtnView()
                }
            })
        }
    }

    private fun setCheckEmailObserve() {
        viewModel.checkEmail.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { createResponse ->
                        if (createResponse.result == "OK") {
                            tvEmailError.visibility = View.INVISIBLE
                            ivCheckEmail.visibility = View.VISIBLE
                            setExplainText()
                            requestFocus()
                            layoutRoot.snackbar(createResponse.message)
                        } else {
                            tvExplain.text = createResponse.message
                            tvExplain.setTextColor(
                                ContextCompat.getColor(
                                    this,
                                    R.color.textWarring
                                )
                            )
                            layoutRoot.snackbar(createResponse.message)
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

    private fun isComplete(): Boolean {
        for (i in ivChecks) {
            if (i.visibility == View.INVISIBLE) return false
        }
        return true
    }

    private fun setText() {
        for (i in tvErrors.indices) {
            tvErrors[i].text = errorText[i]
        }
        for (i in etInputs.indices) {
            etInputs[i].hint = hintText[i]
        }
        tvExplain.text = explainText[0]
    }

    private fun setExplainText() {
        if (isComplete()) tvExplain.text = "다음 버튼을 눌러주세요."
        else {
            tvExplain.text = explainText[position]
        }
        tvExplain.setTextColor(ContextCompat.getColor(this, R.color.textForet))
    }

    private fun requestFocus() {
        if (isFirst) {
            moveNextLayout()
            etInputs[position].requestFocus()
        }
    }

    private fun setNextBtnView() {
        if (isComplete() && position == layoutInputs.size - 1) {
            layoutNextBtn.visibility = View.VISIBLE
        } else {
            layoutNextBtn.visibility = View.GONE
        }

    }

    private fun showProgressBar() {}
    private fun hideProgressBar() {}
}

