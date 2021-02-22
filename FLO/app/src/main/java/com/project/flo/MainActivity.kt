package com.project.flo

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.ToggleButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.flo.adapter.LyricsAdapter
import com.project.flo.model.Lyrics
import com.project.flo.model.Song
import com.project.flo.repository.SongRepository
import kotlinx.coroutines.Runnable
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), LyricsAdapter.OnItemClickListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var lyricsAdapter: LyricsAdapter

    private lateinit var layoutLyrics: ConstraintLayout
    private lateinit var toggleBtnTarget: ToggleButton
    private lateinit var ivCross: ImageView
    private lateinit var tvTitle2: TextView
    private lateinit var tvSinger2: TextView
    private lateinit var rvLyrics: RecyclerView

    private lateinit var toggleBtnPlayPause: ToggleButton
    private lateinit var tvTitle: TextView
    private lateinit var tvSinger: TextView
    private lateinit var tvAlbum: TextView
    private lateinit var tvStart: TextView
    private lateinit var tvEnd: TextView
    private lateinit var tvLyrics1: TextView
    private lateinit var tvLyrics2: TextView
    private lateinit var ivAlbum: ImageView
    private lateinit var seekBar: SeekBar

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    private var sTime: Int = 0
    private var eTime: Int = 0

    private lateinit var animInBottom: Animation
    private lateinit var animOutBottom: Animation

    private val TAG = "뭐씨발"

    private val lyricsList = ArrayList<Lyrics>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setFindViewById()
        setListener()

        val songRepository = SongRepository()
        val viewModelProviderFactory = MainViewModelProviderFactory(songRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(MainViewModel::class.java)
        viewModel.getSong()
        setDataObserve()
    }

    private fun setFindViewById() {
        // 가사 레이아웃
        layoutLyrics = findViewById(R.id.layoutLyrics)
        rvLyrics = findViewById(R.id.rvLyrics)
        ivCross = findViewById(R.id.ivCross)
        tvTitle2 = findViewById(R.id.tvTitle2)
        tvSinger2 = findViewById(R.id.tvSinger2)
        toggleBtnTarget = findViewById(R.id.toggleBtnTarget)

        // 메인 레이아웃
        toggleBtnPlayPause = findViewById(R.id.toggleBtnPlayPause)
        tvTitle = findViewById(R.id.tvTitle)
        tvSinger = findViewById(R.id.tvSinger)
        tvAlbum = findViewById(R.id.tvAlbum)
        tvLyrics1 = findViewById(R.id.tvLyrics1)
        tvLyrics2 = findViewById(R.id.tvLyrics2)
        tvStart = findViewById(R.id.tvStart)
        tvEnd = findViewById(R.id.tvEnd)
        ivAlbum = findViewById(R.id.ivAlbum)
        seekBar = findViewById(R.id.seekBar)

        animInBottom = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom)
        animOutBottom = AnimationUtils.loadAnimation(this, R.anim.slide_out_bottom)
    }

    private fun setListener() {
        toggleBtnPlayPause.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mediaPlayer.start()
            } else {
                mediaPlayer.pause()
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // 유저가 눌렀을 때
                if (fromUser) mediaPlayer.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // 누를 때 음악 멈춤
                mediaPlayer.pause()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // 땔 때 음악 재생
                if (toggleBtnPlayPause.isChecked) {
                    mediaPlayer.start()
                }
            }
        })

        tvLyrics1.setOnClickListener {
            layoutLyrics.visibility = View.VISIBLE
            layoutLyrics.startAnimation(animInBottom)
        }
        tvLyrics2.setOnClickListener {
            layoutLyrics.visibility = View.VISIBLE
            layoutLyrics.startAnimation(animInBottom)
        }
        ivCross.setOnClickListener {
            layoutLyrics.visibility = View.GONE
            layoutLyrics.startAnimation(animOutBottom)
        }
    }

    override fun onItemClick(position: Int) {
        if (toggleBtnTarget.isChecked) {
            mediaPlayer.seekTo(lyricsList[position].time.toSecond())
            lyricsAdapter.notifyDataSetChanged()
        } else {
            layoutLyrics.visibility = View.GONE
            layoutLyrics.startAnimation(animOutBottom)
        }
    }

    private fun setAdapter() {
        lyricsAdapter = LyricsAdapter(lyricsList, this@MainActivity)
        rvLyrics.apply {
            adapter = lyricsAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun setDataObserve() {
        viewModel.song.observe(this, Observer { response ->
            setUpSing(response)
            setAdapter()
        })
    }

    private fun setUpSing(song: Song) {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(song.file)
            prepare()
        }
        eTime = mediaPlayer.duration.toLong().toInt()
        sTime = mediaPlayer.currentPosition.toLong().toInt()
        seekBar.max = eTime
        // oTime = 1

        tvTitle.text = song.title
        tvTitle2.text = song.title
        tvSinger.text = song.singer
        tvSinger2.text = song.singer
        tvAlbum.text = song.album
        tvEnd.text = String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(eTime.toLong()),
            TimeUnit.MILLISECONDS.toSeconds(eTime.toLong()) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(
                    eTime.toLong()
                )
            )
        )
        tvStart.text = String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(sTime.toLong()),
            TimeUnit.MILLISECONDS.toSeconds(sTime.toLong()) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(
                    sTime.toLong()
                )
            )
        )

        Glide.with(this)
            .load(song.image)
            .into(ivAlbum)

        val allLyrics = song.lyrics.split("\n")
        for (i in allLyrics) {
            val time = i.substring(1, 10)
            val lyrics = i.substring(11)
            lyricsList.add(Lyrics(false, time, lyrics))
        }

        handler = Handler(Looper.getMainLooper())
        handler.postDelayed(updateSongTime, 0)
    }

    private val updateSongTime: Runnable = object : Runnable {
        override fun run() {
            sTime = mediaPlayer.currentPosition
            tvStart.text = String.format(
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(sTime.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(sTime.toLong()) - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(sTime.toLong())
                )
            )

            setLyrics()
            setReducedLyrics()

            seekBar.progress = sTime
            handler.postDelayed(this, 1000)
        }
    }

    private fun setLyrics() {
        for(i in 0 until lyricsList.size){
            if(sTime > lyricsList[i].time.toSecond()){
                for (j in 0 until lyricsList.size) {
                    if (j == i) continue
                    lyricsList[j].isNow = false
                }
                lyricsList[i].isNow = true
                lyricsAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun setReducedLyrics() {
        if(sTime < lyricsList[0].time.toSecond()){
            tvLyrics1.text = lyricsList[0].lyrics
            tvLyrics2.text = lyricsList[0].lyrics
            tvLyrics1.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.grey
                )
            )
            return
        }

        for (i in 0 until lyricsList.size) {
            if (sTime > lyricsList[i].time.toSecond()) {
                tvLyrics1.text = lyricsList[i].lyrics
                tvLyrics1.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.white
                    )
                )
                if (i != lyricsAdapter.itemCount - 1) {
                    tvLyrics2.text = lyricsList[i + 1].lyrics
                } else {
                    tvLyrics2.text = ""
                }
            }
        }
    }

    private fun String.toSecond(): Int {
        val factors = arrayOf(60000, 1000, 1)
        var value = 0
        this.split(":").forEachIndexed { i, s -> value += factors[i] * s.toInt() }
        return value
    }
}