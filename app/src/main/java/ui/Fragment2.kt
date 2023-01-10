package com.backstackfragment.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.backstackfragment.ActivityMain.Companion.cvBack
import com.backstackfragment.ActivityMain.Companion.tvTitle
import com.backstackfragment.R
import com.backstackfragment.databinding.ActivityMainBinding

class Fragment2 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        cvBack.visibility = View.VISIBLE

        tvTitle.text = "Fragment 2"

        cvBack.setOnClickListener{
            requireActivity().onBackPressed()
        }
    }
}