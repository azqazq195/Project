package com.project.foret.ui.main.board

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.project.foret.ui.main.MainActivity
import com.project.foret.R
import com.project.foret.model.Board
import com.project.foret.model.Foret
import com.project.foret.model.Member
import com.project.foret.repository.ForetRepository
import com.project.foret.response.UploadResponse
import com.project.foret.util.UploadRequestBody
import com.project.foret.util.getFileName
import com.project.foret.util.snackbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class CreateBoardActivity : AppCompatActivity(), UploadRequestBody.UploadCallback {

    lateinit var createBoardLayoutRoot: ConstraintLayout
    lateinit var progressBar: ProgressBar
    lateinit var etContent: EditText
    lateinit var etSubject: EditText
    lateinit var tvWriter: TextView
    lateinit var btnPickImage: Button
    lateinit var btnCreateBoard: Button
    lateinit var spBoardType: Spinner
    lateinit var layoutImages: LinearLayout
    lateinit var toolbar: Toolbar

    lateinit var ivBoardImage: Array<ImageView>
    lateinit var tvBoardImageCancel: Array<TextView>

    val TAG = "CreateBoardActivity"

    private var selectedImage = mutableListOf<Uri?>()
    var isAnonymous = true
    var memberId = 0L
    var memberName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_board)

        findViewById()

        getIntentData()
        setBoardViewType()
        setOnClickListener()
    }

    private fun findViewById(){
        createBoardLayoutRoot = findViewById(R.id.createBoardLayoutRoot)
        etContent = findViewById(R.id.etContent)
        etSubject = findViewById(R.id.etSubject)
        tvWriter = findViewById(R.id.tvWriter)
        btnPickImage = findViewById(R.id.btnPickImage)
        btnCreateBoard = findViewById(R.id.btnCreateBoard)
        spBoardType = findViewById(R.id.spBoardType)
        layoutImages = findViewById(R.id.layoutImages)
        toolbar = findViewById(R.id.cosutom_toolbar)

        ivBoardImage = arrayOf(
            findViewById(R.id.ivBoardImage1),
            findViewById(R.id.ivBoardImage2),
            findViewById(R.id.ivBoardImage3),
            findViewById(R.id.ivBoardImage4),
            findViewById(R.id.ivBoardImage5)
        )

        tvBoardImageCancel = arrayOf(
            findViewById(R.id.tvBoardImageCancel1),
            findViewById(R.id.tvBoardImageCancel2),
            findViewById(R.id.tvBoardImageCancel3),
            findViewById(R.id.tvBoardImageCancel4),
            findViewById(R.id.tvBoardImageCancel5)
        )
    }

    private fun setOnClickListener() {
        btnCreateBoard.setOnClickListener {
            createCheckDialog()
        }

        btnPickImage.setOnClickListener{
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                val mimeTypes = arrayOf("image/jpeg", "image/png")
                it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                it.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                startActivityForResult(it, REQUEST_CODE_IMAGE_PICKER)
            }
        }

        for(i in tvBoardImageCancel.indices){
            tvBoardImageCancel[i].setOnClickListener {
                selectedImage.removeAt(i)
                setPickImageView()
            }
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

    private fun setPickImageView() {
        for(i in selectedImage.indices){
            Glide.with(this)
                .load(selectedImage[i])
                .into(ivBoardImage[i])
            tvBoardImageCancel[i].visibility = View.VISIBLE
        }
        for(i in selectedImage.size until ivBoardImage.size){
            ivBoardImage[i].setImageResource(R.drawable.upload_photo)
            tvBoardImageCancel[i].visibility = View.GONE
        }
    }

    private fun createCheckDialog() {
        if (!checkBlank()) return
        val alertDialog: AlertDialog = let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setMessage("게시글을 생성하시겠습니까?")
                setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->
                        createBoard()
                    })
                setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            }
            builder.create()
        }
        alertDialog.show()
    }

    private fun createBoard() {
        // 사진 처리
        var photos: MutableList<MultipartBody.Part>? = mutableListOf()
        if(selectedImage.size != 0){
            for(i in selectedImage){
                val parcelFileDescriptor =
                    contentResolver.openFileDescriptor(i!!, "r", null) ?: return
                val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
                val file = File(cacheDir, contentResolver.getFileName(i))
                val outputStream = FileOutputStream(file)
                inputStream.copyTo(outputStream)
                val body = UploadRequestBody(file, "image", this)
                photos?.add(MultipartBody.Part.createFormData("files", file.name, body))
            }
        } else {
            photos = null
        }

        // 게시글 처리
        val member = Member(memberId)
        val subject = etSubject.text.toString()
        val content = etContent.text.toString()
        val foret_id = intent.getLongExtra("foretId", 0L)
        val foret = if(foret_id != 0L) Foret(foret_id) else null
        var type = 0
        when (spBoardType.selectedItem) {
            "공지" -> {
                type = 1
            }
            "일반" -> {
                type = 3
            }
            "익명" -> {
                type = 4
            }
        }
        val board = Board(type, subject, content, member, foret)
        val gson = Gson().toJson(board)

        ForetRepository().createBoard(
            photos,
            gson.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        ).enqueue(object : Callback<UploadResponse> {
            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                val intent = Intent()
                intent.putExtra("boardId", response.body()?.id)
                intent.putExtra("message", response.body()?.message.toString())
                setResult(RESULT_OK, intent)
                finish()
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                createBoardLayoutRoot.snackbar(t.message!!)
            }
        })

        // 생성 완료
//        val intent = Intent()
//         intent.putExtra("boardId", response.body()?.id)
//        setResult(RESULT_OK, intent)
//        finish()
    }

    private fun checkBlank(): Boolean {
        if (etSubject.text.toString() == "") {
            createBoardLayoutRoot.snackbar("제목을 입력해 주세요.")
            return false
        }

        if (etContent.text.toString() == "") {
            createBoardLayoutRoot.snackbar("내용을 입력해 주세요.")
            return false
        }

        if (spBoardType.selectedItem == "선택") {
            createBoardLayoutRoot.snackbar("게시글 타입을 선택해주세요!")
            return false
        }
        return true
    }

    private fun setBoardViewType() {
        isAnonymous = intent.getBooleanExtra("isAnonymous", true)
        if (isAnonymous) {
            setAnonymousBoardWriteView()
        } else {
            setForetBoardWriteView()
        }
    }

    private fun getIntentData() {
        isAnonymous = intent.getBooleanExtra("isAnonymous", true)
        memberId = intent.getLongExtra("memberId", 0L)
        memberName = intent.getStringExtra("memberName").toString()
    }

    private fun setAnonymousBoardWriteView() {
        tvWriter.text = ""
        ArrayAdapter.createFromResource(
            this,
            R.array.spAnonymousBoard,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spBoardType.adapter = adapter
            spBoardType.isEnabled = false
        }
        btnPickImage.visibility = View.GONE
        layoutImages.visibility = View.GONE
    }

    private fun setForetBoardWriteView() {
        tvWriter.text = memberName
        ArrayAdapter.createFromResource(
            this,
            R.array.spForetBoard,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spBoardType.adapter = adapter
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_IMAGE_PICKER -> {
                    data?.let {
                        when {
                            it.data != null -> {
                                if(selectedImage.size >= 5) {
                                    createBoardLayoutRoot.snackbar("최대 5장의 이미지만 등록 가능합니다 ㅠ")
                                    return@let
                                }
                                selectedImage.add(it.data)
                            }

                            it.clipData != null -> {
                                val clip = it.clipData
                                val size = clip?.itemCount
                                for(i in 0 until size!!){
                                    if(selectedImage.size >= 5) {
                                        createBoardLayoutRoot.snackbar("최대 5장의 이미지만 등록 가능합니다 ㅠ")
                                        return@let
                                    }
                                    selectedImage.add(clip.getItemAt(i).uri)
                                }
                            }
                            else -> {}
                        }
                    }
                    setPickImageView()
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