package com.project.foret.ui.entrance.signUp

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.lifecycle.Observer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.project.foret.R
import com.project.foret.model.Member
import com.project.foret.model.Region
import com.project.foret.model.Tag
import com.project.foret.repository.ForetRepository
import com.project.foret.ui.main.MainActivity
import com.project.foret.util.Resource
import com.project.foret.util.UploadRequestBody
import com.project.foret.util.getFileName
import com.project.foret.util.snackbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class SignUpInfoActivity : AppCompatActivity(), UploadRequestBody.UploadCallback {

    lateinit var viewModel: SignUpViewModel
    lateinit var layoutRoot: LinearLayout
    lateinit var progressBar: ProgressBar

    lateinit var layoutPrevNext: LinearLayout

    lateinit var layoutIntroduce: LinearLayout
    lateinit var layoutTag: LinearLayout
    lateinit var layoutRegion: LinearLayout
    lateinit var layoutPhoto: LinearLayout
    lateinit var layoutFinish: LinearLayout
    lateinit var layoutInputs: Array<LinearLayout>

    lateinit var tvPrevious: TextView
    lateinit var tvNext: TextView

    lateinit var tvTagSelect: TextView
    lateinit var tvRegionSelect: TextView
    lateinit var tvPhotoSelect: TextView
    lateinit var tvTagSelected: TextView
    lateinit var tvRegionSelected: TextView
    lateinit var tvFinishMessage: TextView

    lateinit var ivProfile: ImageView

    lateinit var animInRight: Animation
    lateinit var animInLeft: Animation
    lateinit var animOutRight: Animation
    lateinit var animOutLeft: Animation

    lateinit var btnNext: Button

    private var position = 0
    private var prevPosition = 0
    private var selectedImage: Uri? = null

    private val TAG = "SignUpInfoActivity"

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_info)

        val foretRepository = ForetRepository()
        val viewModelProviderFactory = SignUpViewModelProviderFactory(foretRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(SignUpViewModel::class.java)

        setFindViewById()
        setAnimation()
        setOnClickListener()
        setFinishText()
        setCreateResponse()
    }

    private fun setAnimation() {
        animInRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)
        animInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
        animOutRight = AnimationUtils.loadAnimation(this, R.anim.slide_out_right)
        animOutLeft = AnimationUtils.loadAnimation(this, R.anim.slide_out_left)
    }

    private fun setFindViewById() {
        layoutRoot = findViewById(R.id.layoutRoot)
        progressBar = findViewById(R.id.progressBar)

        layoutPrevNext = findViewById(R.id.layoutPrevNext)

        layoutIntroduce = findViewById(R.id.layoutIntroduce)
        layoutTag = findViewById(R.id.layoutTag)
        layoutRegion = findViewById(R.id.layoutRegion)
        layoutPhoto = findViewById(R.id.layoutPhoto)
        layoutFinish = findViewById(R.id.layoutFinish)
        layoutInputs = arrayOf(
            layoutIntroduce,
            layoutTag,
            layoutRegion,
            layoutPhoto,
            layoutFinish
        )

        tvPrevious = findViewById(R.id.tvPrevious)
        tvNext = findViewById(R.id.tvNext)

        tvTagSelect = findViewById(R.id.tvTagSelect)
        tvRegionSelect = findViewById(R.id.tvRegionSelect)
        tvPhotoSelect = findViewById(R.id.tvPhotoSelect)
        tvFinishMessage = findViewById(R.id.tvFinishMessage)
        tvTagSelected = findViewById(R.id.tvTagSelected)
        tvRegionSelected = findViewById(R.id.tvRegionSelected)

        ivProfile = findViewById(R.id.ivProfile)

        btnNext = findViewById(R.id.btnNext)
    }

    private fun setOnClickListener() {
        tvPrevious.setOnClickListener {
            movePrevLayout()
        }
        tvNext.setOnClickListener {
            moveNextLayout()
        }
        tvTagSelect.setOnClickListener {
            selectTag()
        }
        tvRegionSelect.setOnClickListener {
            selectRegion()
        }
        tvPhotoSelect.setOnClickListener {
            selectPhoto()
        }
        btnNext.setOnClickListener {
            createMember()
        }
    }

    private fun setFinishText() {
        val name = intent.getStringExtra("name").toString()
        val spannableString = SpannableString("${name}님")
        spannableString.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    this,
                    R.color.textForet
                )
            ), 0, name.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tvFinishMessage.text = spannableString
    }

    private fun movePrevLayout() {
        if (position > 0) {
            layoutInputs[position].clearAnimation()
            layoutInputs[prevPosition].clearAnimation()
            prevPosition = position--
            changeLayout(false)
        }
    }

    private fun moveNextLayout() {
        if (position < layoutInputs.size - 1) {
            layoutInputs[position].clearAnimation()
            layoutInputs[prevPosition].clearAnimation()
            prevPosition = position++
            changeLayout(true)
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

        if (position == layoutInputs.size - 1) {
            tvNext.visibility = View.INVISIBLE
            tvPrevious.visibility = View.INVISIBLE
        } else tvNext.visibility = View.VISIBLE
    }

    private fun selectTag() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_tag_selector, null)
        val spTag = dialogView.findViewById<Spinner>(R.id.spTag)

        ArrayAdapter.createFromResource(
            this,
            R.array.spTag,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spTag.adapter = adapter
        }

        val alertDialog: AlertDialog = let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setView(dialogView)
                setPositiveButton("추가",
                    DialogInterface.OnClickListener { dialog, id ->
                        tvTagSelected.text =
                            spTag.selectedItem.toString()
                    })
                setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            }
            builder.create()
        }
        alertDialog.show()
    }

    private fun selectRegion() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_region_selector, null)
        val spRegionSi = dialogView.findViewById<Spinner>(R.id.spRegionSi)
        val spRegionGu = dialogView.findViewById<Spinner>(R.id.spRegionGu)

        // 시 스피너
        ArrayAdapter.createFromResource(
            this,
            R.array.spRegionSi,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spRegionSi.adapter = adapter
        }
        // 구 스피너
        ArrayAdapter.createFromResource(
            this,
            R.array.spRegionGu,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spRegionGu.adapter = adapter
            spRegionGu.isEnabled = false
        }


        spRegionSi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var regionSi = R.array.spRegionGu
                when (parent?.getItemAtPosition(position)) {
                    " 시/도 " -> regionSi = R.array.spRegionGu
                    "서울" -> regionSi = R.array.seuol
                    "인천" -> regionSi = R.array.incheon
                    "세종" -> regionSi = R.array.sejong
                    "대전" -> regionSi = R.array.daejeon
                    "광주" -> regionSi = R.array.gwangju
                    "대구" -> regionSi = R.array.daegu
                    "울산" -> regionSi = R.array.ulsan
                    "부산" -> regionSi = R.array.busan
                    "경기" -> regionSi = R.array.gyeonggi
                    "강원" -> regionSi = R.array.gangwon
                    "충북" -> regionSi = R.array.chungbuk
                    "충남" -> regionSi = R.array.chungnam
                    "전북" -> regionSi = R.array.jeonbuk
                    "전남" -> regionSi = R.array.jeonnam
                    "경북" -> regionSi = R.array.gyeongbuk
                    "경남" -> regionSi = R.array.gyeongnam
                    "제주" -> regionSi = R.array.jeju
                }

                // 구 스피너
                ArrayAdapter.createFromResource(
                    applicationContext,
                    regionSi,
                    android.R.layout.simple_spinner_item
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spRegionGu.adapter = adapter
                    spRegionGu.isEnabled = regionSi != R.array.spRegionGu
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        val alertDialog: AlertDialog = let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setView(dialogView)
                setPositiveButton("추가",
                    DialogInterface.OnClickListener { dialog, id ->
                        if (spRegionGu.selectedItemPosition != 0) {
                            tvRegionSelected.text =
                                spRegionSi.selectedItem.toString() + " " + spRegionGu.selectedItem.toString()
                        } else {
                            layoutRoot.snackbar("상세 지역을 선택해 주세요")
                        }

                    })
                setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            }
            builder.create()
        }
        alertDialog.show()
    }

    private fun selectPhoto() {
        Intent(Intent.ACTION_GET_CONTENT).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                1 -> {
                    selectedImage = data?.data
                    Glide.with(this)
                        .load(data!!.data)
                        .into(ivProfile)
                }
            }
        }
    }

    private fun createMember() {
        val name = intent.getStringExtra("name").toString()
        val nickname = intent.getStringExtra("nickname").toString()
        val email = intent.getStringExtra("email").toString()
//        val birthString = intent.getStringExtra("birth").toString()
//        val birth = SimpleDateFormat("yyyyMMdd").parse(birthString)
        val birth = intent.getStringExtra("birth").toString()
        val password = intent.getStringExtra("password").toString()
        val tag = Tag(tvTagSelected.text.toString())
        val regionString = tvRegionSelected.text.toString()
        val regionSi = regionString.substring(0, regionString.indexOf(" "))
        val regionGu = regionString.substring(regionString.indexOf(" ") + 1)
        val region = Region(regionSi, regionGu)

        val member = Member(
            name,
            nickname,
            email,
            birth,
            password,
            mutableListOf(tag),
            mutableListOf(region),
        )

        // 사진 처리
        val parcelFileDescriptor =
            contentResolver.openFileDescriptor(selectedImage!!, "r", null) ?: return
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(cacheDir, contentResolver.getFileName(selectedImage!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        val body = UploadRequestBody(file, "image", this)

        val gson = Gson().toJson(member)

        viewModel.createMember(
            MultipartBody.Part.createFormData("files", file.name, body),
            gson.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        )
    }

    private fun setCreateResponse() {
        viewModel.createMember.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    // 멤버 생성 성공
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("id", response.data?.id)
                    startActivity(intent)
                    finish()
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

    override fun onProgressUpdate(percentage: Int) {
    }

}