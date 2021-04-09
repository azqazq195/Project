package com.project.vllo.edit

import android.content.Context
import android.graphics.Bitmap
import android.media.*
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.project.vllo.R
import com.project.vllo.databinding.ActivityEditBinding
import com.project.vllo.model.Item
import com.project.vllo.utils.ScreenSizeUtil
import com.project.vllo.utils.TrimVideoUtils
import kotlin.math.ceil

class EditActivity : AppCompatActivity() {

    val TAG = "EditActivity"

    private lateinit var binding: ActivityEditBinding

    private lateinit var itemList: ArrayList<Item>
    private var mDuration: Int = 0
    private var mThumbnailList: ArrayList<ArrayList<Bitmap>> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        // 없으면 안댐 addFragment 에서 넘어올때 null 체크 필수
        itemList = intent.getParcelableArrayListExtra<Item>("selectedList")!!

        setUpDataBinding()
        initView()
        prepareVideo()

        setVideoDuration()

        mThumbnailList.add(getThumbnailList(itemList[0].uri, this))
        mThumbnailList.add(getThumbnailList(itemList[1].uri, this))

        setThumbnailListView()
    }

    private fun setThumbnailListView() {
        binding.rvVideoThumbnail.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = EditAdapter(mThumbnailList, context)
        }
        Log.e(TAG, "setThumbnailListView: ${binding.rvVideoThumbnail.adapter?.itemCount}", )
    }

    private fun setUpDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit)
        binding.edit = this@EditActivity
    }

    private fun initView() {
        binding.seekBar.apply {
            progress = binding.seekBar.max / 2
            isEnabled = false
        }
    }

    private fun prepareVideo() {
        val player = SimpleExoPlayer.Builder(this).build()
        val mediaItem1 = MediaItem.fromUri(itemList[0].uri)
        val mediaItem2 = MediaItem.fromUri(itemList[1].uri)
        player.apply {
            addMediaItem(mediaItem1)
            addMediaItem(mediaItem2)
            prepare()
        }
        binding.videoLoader.apply {
            hideController()
            useController = false
            setPlayer(player)
        }
        binding.tvVideoStartTime.text =
            String.format("%s", TrimVideoUtils.stringForTime(player.currentPosition.toFloat()))
        mDuration = itemList[0].duration!!
    }

    private fun setVideoDuration() {
        binding.tvVideoEndTime.text =
            String.format("%s", TrimVideoUtils.stringForTime(mDuration.toFloat()))
    }

    fun playVideo() {
        binding.btnVideoPause.visibility = View.VISIBLE
        binding.btnVideoPlay.visibility = View.INVISIBLE
        binding.videoLoader.player?.play()
    }

    fun pauseVideo() {
        binding.btnVideoPause.visibility = View.INVISIBLE
        binding.btnVideoPlay.visibility = View.VISIBLE
        binding.videoLoader.player?.pause()
    }

    fun previousVideo() {
        binding.videoLoader.player?.seekTo(0)
        pauseVideo()
    }

    fun nextVideo() {
        binding.videoLoader.player?.seekTo(
            binding.videoLoader.player?.duration!!.toLong()
        )
        pauseVideo()
    }

    private fun getThumbnailList(mSrc: Uri, context: Context) : ArrayList<Bitmap> {
        val thumbnailList = ArrayList<Bitmap>()
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(context, mSrc)

        val videoLengthInMs =
            (Integer.parseInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION))).toLong()
        val cropHeight = 150
        val cropWidth = ScreenSizeUtil(context).widthPixels / 4
        val interval = if (videoLengthInMs < 3000) videoLengthInMs else 3000

        for (i in 0..videoLengthInMs step interval) {
            val bitmap = mediaMetadataRetriever.getFrameAtTime(
                i * 1000,
                MediaMetadataRetriever.OPTION_CLOSEST_SYNC
            )
            bitmap?.let {
                thumbnailList.add(Bitmap.createScaledBitmap(bitmap, cropWidth, cropHeight, false))
            }
        }
        mediaMetadataRetriever.release()
        return thumbnailList
    }

    private fun getThumbnailList2(mSrc: Uri, context: Context) : ArrayList<Bitmap> {
        val thumbnailList = ArrayList<Bitmap>()
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(context, mSrc)
        val videoLengthInMs =
            (Integer.parseInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION))).toLong()
        val frameHeight = context.resources.getDimensionPixelOffset(R.dimen.frames_video_height)
        val initialBitmap = mediaMetadataRetriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
        val frameWidth = ((initialBitmap?.width?.toFloat()!! / initialBitmap.height.toFloat()) * frameHeight.toFloat()).toInt()
        var numTHumb = ceil((viewWi))
        val cropHeight = 150
        val cropWidth = ScreenSizeUtil(context).widthPixels / 4
        val interval = if (videoLengthInMs < 3000) videoLengthInMs else 3000

        for (i in 0..videoLengthInMs step interval) {
            val bitmap = mediaMetadataRetriever.getFrameAtTime(
                i * 1000,
                MediaMetadataRetriever.OPTION_CLOSEST_SYNC
            )
            bitmap?.let {
                thumbnailList.add(Bitmap.createScaledBitmap(bitmap, cropWidth, cropHeight, false))
            }
        }
        mediaMetadataRetriever.release()
        return thumbnailList
    }
}