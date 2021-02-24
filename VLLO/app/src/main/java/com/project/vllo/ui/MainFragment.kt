package com.project.vllo.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import com.project.vllo.R

class MainFragment : Fragment() {

    private lateinit var constraintLayoutPlus: ConstraintLayout
    private lateinit var btnSetting: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container,false)

        setFindViewById(view)
        setOnClickListener(view)

        return view
    }

    private fun setFindViewById(view: View) {
        constraintLayoutPlus = view.findViewById<ConstraintLayout>(R.id.ConstraintLayoutPlus)
        btnSetting = view.findViewById<Button>(R.id.btnSetting)
    }

    private fun setOnClickListener(view: View) {
        constraintLayoutPlus.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_addFragment)
        }
        btnSetting.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_settingFragment)
        }
    }

}