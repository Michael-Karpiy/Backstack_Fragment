package com.backstackfragment.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.backstackfragment.ActivityMain
import com.backstackfragment.ActivityMain.Companion.cvBack
import com.backstackfragment.ActivityMain.Companion.tvTitle
import com.backstackfragment.R

class Fragment1 : Fragment() {

    private lateinit var cvFragment2: CardView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        cvBack.visibility = View.GONE

        tvTitle.text = "Fragment 1"

        cvFragment2 = requireView().findViewById(R.id.cvFragment2)
        cvFragment2.setOnClickListener {

            (context as ActivityMain).showFragment(Fragment2())

        }
    }
}