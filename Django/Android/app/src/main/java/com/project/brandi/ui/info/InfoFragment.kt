package com.project.brandi.ui.info

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import com.project.brandi.App
import com.project.brandi.R

class InfoFragment : Fragment(R.layout.fragment_info) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(isMember()){
            member()
        } else {
            nonMember()
        }

    }

    private fun isMember() : Boolean {
        return App.prefs.getString("token") != ""
    }

    private fun member() {
        view?.findViewById<LinearLayout>(R.id.nonMember)?.visibility = View.INVISIBLE
        view?.findViewById<LinearLayout>(R.id.member)?.visibility = View.VISIBLE

        view?.findViewById<TextView>(R.id.tvName)?.text = App.prefs.getString("name")
        view?.findViewById<TextView>(R.id.tvEmail)?.text = App.prefs.getString("email")

        view?.findViewById<TextView>(R.id.tvLogout)?.setOnClickListener{
            App.prefs.removeAll()
            requireFragmentManager().beginTransaction().apply {
                detach(this@InfoFragment)
                attach(this@InfoFragment)
                commit()
            }
        }

        view?.findViewById<Button>(R.id.btnOrder)?.setOnClickListener {
            view?.findNavController()?.navigate(
                R.id.action_infoFragment_to_orderFragment
            )
        }
    }

    private fun nonMember() {
        view?.findViewById<LinearLayout>(R.id.nonMember)?.visibility = View.VISIBLE
        view?.findViewById<LinearLayout>(R.id.member)?.visibility = View.INVISIBLE

        view?.findViewById<TextView>(R.id.tvSignIn)?.setOnClickListener{
            view?.findNavController()?.navigate(
                R.id.action_infoFragment_to_signInFragment
            )
        }

        view?.findViewById<TextView>(R.id.tvSignUP)?.setOnClickListener {
            view?.findNavController()?.navigate(
                R.id.action_infoFragment_to_signUpFragment
            )
        }
    }
}