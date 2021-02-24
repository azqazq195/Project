package com.project.vllo.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.project.vllo.R

class AddSettingFragment : Fragment() {

    private lateinit var btnPrevious: Button
    private lateinit var btnNext: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_setting, container, false)

        setFindViewById(view)
        setOnClickListener(view)

        return view
    }

    private fun setFindViewById(view: View) {
        btnPrevious = view.findViewById(R.id.btnPrevious)
        btnNext = view.findViewById(R.id.btnNext)
    }

    private fun setOnClickListener(view: View) {
        btnPrevious.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}