package com.project.foret.ui.main.foret

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.project.foret.ui.main.MainActivity
import com.project.foret.R
import com.project.foret.model.Foret
import com.project.foret.model.Member
import com.project.foret.model.Tag
import com.project.foret.model.Region
import com.project.foret.repository.ForetRepository
import com.project.foret.response.UploadResponse
import com.project.foret.util.UploadRequestBody
import com.project.foret.util.getFileName
import com.project.foret.util.snackbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import retrofit2.Callback
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.*


class CreateForetActivity : AppCompatActivity(), UploadRequestBody.UploadCallback {

    lateinit var createForetLayoutRoot: ConstraintLayout
    lateinit var ivCancel: ImageView
    lateinit var layoutRoot: LinearLayout
    lateinit var ivForetImage: ImageView
    lateinit var btnPickPhoto: Button
    lateinit var btnCreateForet: Button
    lateinit var etForetName: EditText
    lateinit var etForetIntroduce: EditText
    lateinit var etNumberOfMembers: EditText
    lateinit var tvRegionSelect: TextView
    lateinit var tvTagSelect: TextView

    val TAG = "CreateForetActivity"

    private var selectedImage: Uri? = null

    var memberId = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_foret)

        memberId = intent.getLongExtra("memberId", 0L)

        setFindViewById()
        setOnClickListener()
    }

    private fun setFindViewById() {
        createForetLayoutRoot = findViewById(R.id.createForetLayoutRoot)
        layoutRoot = findViewById(R.id.layoutRoot)
        ivCancel = findViewById(R.id.ivCancel)
        ivForetImage = findViewById(R.id.ivForetImage)
        btnPickPhoto = findViewById(R.id.btnPickPhoto)
        btnCreateForet = findViewById(R.id.btnCreateForet)
        etForetName = findViewById(R.id.etForetName)
        etForetIntroduce = findViewById(R.id.etForetIntroduce)
        etNumberOfMembers = findViewById(R.id.etNumberOfMembers)
        tvRegionSelect = findViewById(R.id.tvRegionSelect)
        tvTagSelect = findViewById(R.id.tvTagSelect)
    }

    private fun setOnClickListener() {
        ivCancel.setOnClickListener {
            cancelDialog()
        }

        btnPickPhoto.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                val mimeTypes = arrayOf("image/jpeg", "image/png")
                it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                startActivityForResult(it, REQUEST_CODE_IMAGE_PICKER)
            }
        }

        btnCreateForet.setOnClickListener {
            createCheckDialog()
        }

        tvRegionSelect.setOnClickListener {
            regionDialog()
        }

        tvTagSelect.setOnClickListener {
            tagDialog()
        }
    }

    override fun onBackPressed() {
        cancelDialog()
    }

    private fun cancelDialog() {
        val alertDialog: AlertDialog = let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setMessage("작성 중인 내용은 저장되지 않습니다!\n\n종료하시겠습니까?")
                setPositiveButton("종료",
                    DialogInterface.OnClickListener { dialog, id ->
                        // 생성 취소
                        setResult(RESULT_CANCELED)
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

    private fun tagDialog() {
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
                        tvTagSelect.text =
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

    private fun regionDialog() {
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
                    this@CreateForetActivity,
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
                        if(spRegionGu.selectedItemPosition != 0){
                            tvRegionSelect.text =
                                spRegionSi.selectedItem.toString() + " " + spRegionGu.selectedItem.toString()
                        } else {
                            createForetLayoutRoot.snackbar("상세 지역을 선택해 주세요")
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

    private fun checkBlank(): Boolean {
        if (selectedImage == null) {
            layoutRoot.snackbar("사진을 선택해 주세요.")
            return false
        }

        if (etForetName.text.toString() == "") {
            layoutRoot.snackbar("포레의 이름을 입력해 주세요.")
            return false
        }

        if (etNumberOfMembers.text.toString() == "") {
            layoutRoot.snackbar("포레의 정원을 입력해 주세요.")
            return false
        }

        if (Integer.parseInt(etNumberOfMembers.text.toString()) < 5) {
            layoutRoot.snackbar("포레의 정원은 최소 5명 이상입니다.")
            return false
        }

        if (etForetIntroduce.text.toString() == "") {
            layoutRoot.snackbar("포레의 소개를 입력해 주세요.")
            return false
        }

        if (tvRegionSelect.text.toString() == getString(R.string.default_region_selector)) {
            layoutRoot.snackbar("포레의 지역을 입력해 주세요.")
            return false
        }

        if (tvTagSelect.text.toString() == getString(R.string.default_tag_selector)) {
            layoutRoot.snackbar("포레의 태그를 입력해 주세요.")
            return false
        }
        return true
    }

    private fun createCheckDialog() {
        if (!checkBlank()) return
        val alertDialog: AlertDialog = let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setMessage("포레를 만드시겠습니까?")
                setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->
                        createForet()
                    })
                setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            }
            builder.create()
        }
        alertDialog.show()
    }

    private fun createForet() {
        // 사진 처리
        val parcelFileDescriptor =
            contentResolver.openFileDescriptor(selectedImage!!, "r", null) ?: return
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(cacheDir, contentResolver.getFileName(selectedImage!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        val body = UploadRequestBody(file, "image", this)

        // 포레 처리
        val leader = Member(memberId)
        val tag = Tag(tvTagSelect.text.toString())
        val regionString = tvRegionSelect.text.toString()
        val regionSi = regionString.substring(0, regionString.indexOf(" "))
        val regionGu = regionString.substring(regionString.indexOf(" ")+1)
        val region = Region(regionSi, regionGu)

        val foret = Foret(
            leader,
            etForetName.text.toString(),
            etForetIntroduce.text.toString(),
            etNumberOfMembers.text.toString().toInt(),
            mutableListOf(tag),
            mutableListOf(region)
        )
        val gson = Gson().toJson(foret)

        ForetRepository().createForet(
            MultipartBody.Part.createFormData("files", file.name, body),
            gson.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        ).enqueue(object : Callback<UploadResponse> {
            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                val intent = Intent()
                intent.putExtra("foretId", response.body()?.id)
                intent.putExtra("message", response.body()?.message.toString())
                setResult(RESULT_OK, intent)
                finish()
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                createForetLayoutRoot.snackbar(t.message!!)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_IMAGE_PICKER -> {
                    selectedImage = data?.data
                    Glide.with(this)
                        .load(data!!.data)
                        .into(ivForetImage)
                }
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_IMAGE_PICKER = 100
    }

    override fun onProgressUpdate(percentage: Int) {

    }
}