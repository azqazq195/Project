package com.project.vllo.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.vllo.edit.EditActivity
import com.project.vllo.utils.GetFilesFromGallery
import com.project.vllo.R
import com.project.vllo.adapter.*
import com.project.vllo.adapter.GalleryAdapter.ItemListener
import com.project.vllo.model.Item
import com.project.vllo.utils.Constants
import com.project.vllo.utils.snackBar


class AddFragment : Fragment() {

    private lateinit var layoutRoot: LinearLayout
    private lateinit var btnPrevious: Button
    private lateinit var btnNext: Button
    private lateinit var tvAll: TextView
    private lateinit var tvVideo: TextView
    private lateinit var tvPhoto: TextView
    private lateinit var tvGif: TextView
    private lateinit var tvArray: Array<TextView>
    private lateinit var rvGallery: RecyclerView
    private lateinit var rvSelected: RecyclerView
    private lateinit var galleryAdapter: GalleryAdapter
    private lateinit var selectedAdapter: SelectedAdapter

    private lateinit var itemList: MutableList<Item>
    private lateinit var selectedList: MutableList<Item>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        setFindViewById(view)
        btnPrevious.setOnClickListener {
            requireActivity().onBackPressed()
        }

        // check permission
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                Constants.READ_PERMISSION_CODE
            )
        } else {
            setRecyclerView()
            setOnClickListener(view)
            
            // 임시
            loadVideo()
        }
        return view
    }

    private fun setRecyclerView() {
        itemList = GetFilesFromGallery().listOfPhoto(requireContext())
        galleryAdapter = GalleryAdapter(requireContext(), itemList, object : ItemListener {
            override fun onItemClick(item: Item) {
                selectedList.add(item)
                selectedAdapter.notifyDataSetChanged()
                rvSelected.smoothScrollToPosition(selectedList.size - 1)
            }
        })
        rvGallery.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = galleryAdapter
        }

        selectedList = mutableListOf()
        selectedAdapter =
            SelectedAdapter(requireContext(), selectedList, object : SelectedAdapter.ItemListener {
                override fun onItemClick(item: Item, position: Int) {
                    selectedList.removeAt(position)
                    selectedAdapter.notifyDataSetChanged()
                }
            })
        rvSelected.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = selectedAdapter
        }
    }

    private fun loadAll() {
        itemList = GetFilesFromGallery().listOfPhoto(requireContext())
        galleryAdapter = GalleryAdapter(requireContext(), itemList, object : ItemListener {
            override fun onItemClick(item: Item) {
                selectedList.add(item)
                selectedAdapter.notifyDataSetChanged()
                rvSelected.smoothScrollToPosition(selectedList.size - 1)
            }
        })
        rvGallery.adapter = galleryAdapter
    }

    private fun loadVideo() {
        itemList = GetFilesFromGallery().listOfVideo(requireContext())
        galleryAdapter = GalleryAdapter(requireContext(), itemList, object : ItemListener {
            override fun onItemClick(item: Item) {
                selectedList.add(item)
                selectedAdapter.notifyDataSetChanged()
                rvSelected.smoothScrollToPosition(selectedList.size - 1)
            }
        })
        rvGallery.adapter = galleryAdapter
    }

    private fun loadPhoto() {
        itemList = GetFilesFromGallery().listOfPhoto(requireContext())
        galleryAdapter = GalleryAdapter(requireContext(), itemList, object : ItemListener {
            override fun onItemClick(item: Item) {
                selectedList.add(item)
                selectedAdapter.notifyDataSetChanged()
                rvSelected.smoothScrollToPosition(selectedList.size - 1)
            }
        })
        rvGallery.adapter = galleryAdapter
    }

    private fun loadGif() {
        itemList = GetFilesFromGallery().listOfGif(requireContext())
        galleryAdapter = GalleryAdapter(requireContext(), itemList, object : ItemListener {
            override fun onItemClick(item: Item) {
                selectedList.add(item)
                selectedAdapter.notifyDataSetChanged()
                rvSelected.smoothScrollToPosition(selectedList.size - 1)
            }
        })
        rvGallery.adapter = galleryAdapter
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Constants.READ_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                layoutRoot.snackBar("권한 허용")
                loadAll()
            } else {
                layoutRoot.snackBar("권한 거절")
            }
        }
    }

    private fun setFindViewById(view: View) {
        btnPrevious = view.findViewById(R.id.btnPrevious)
        btnNext = view.findViewById(R.id.btnNext)
        tvAll = view.findViewById(R.id.tvAll)
        tvVideo = view.findViewById(R.id.tvVideo)
        tvPhoto = view.findViewById(R.id.tvPhoto)
        tvGif = view.findViewById(R.id.tvGif)
        rvGallery = view.findViewById(R.id.rvGallery)
        rvSelected = view.findViewById(R.id.rvSelected)
        layoutRoot = view.findViewById(R.id.layoutRoot)

        tvArray = arrayOf(tvAll, tvVideo, tvPhoto, tvGif)
    }

    private fun setOnClickListener(view: View) {
        btnNext.setOnClickListener {
            val intent = Intent(requireContext(), EditActivity::class.java)
            val test = selectedList as ArrayList<Item>
            intent.putExtra("selectedList", test)
            startActivity(intent)
            activity?.finish()
//            Navigation.findNavController(view)
//                .navigate(R.id.action_addFragment_to_addSettingFragment)
        }
        tvAll.setOnClickListener {
            textViewClicked(tvAll)
            loadAll()
        }
        tvVideo.setOnClickListener {
            textViewClicked(tvVideo)
            loadVideo()
        }
        tvPhoto.setOnClickListener {
            textViewClicked(tvPhoto)
            loadPhoto()
        }
        tvGif.setOnClickListener {
            textViewClicked(tvGif)
            loadGif()
        }
    }

    private fun textViewClicked(textView: TextView) {
        for (i in tvArray) {
            if (i == textView) {
                i.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_clicked))
                continue
            }
            i.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_white))
        }
    }

}