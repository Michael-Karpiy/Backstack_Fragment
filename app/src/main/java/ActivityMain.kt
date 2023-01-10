package com.backstackfragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.cardview.widget.CardView
import androidx.core.view.*
import androidx.fragment.app.Fragment
import com.backstackfragment.BackStack.BackStackActivity
import com.backstackfragment.databinding.ActivityMainBinding
import com.backstackfragment.ui.Fragment1

class ActivityMain : BackStackActivity() {

    private lateinit var binding: ActivityMainBinding

    private var curTabId = 0

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        context = this

        tvTitle = findViewById(R.id.tvTitle)
        cvBack = findViewById(R.id.cvBack)

        if (state == null) {
            showFragment(Fragment1())
        }
    }

    fun showFragment(fragment: Fragment): Fragment {
        showFragmentRP(fragment, true)
        return fragment
    }

    fun showFragmentRP(fragment: Fragment, addToBackStack: Boolean) {
        if (curFragment != null && addToBackStack) {
            pushFragmentToBackStack(curTabId, curFragment!!)
        }
        replaceFragment(fragment)
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val tr = fragmentManager.beginTransaction()
        tr.setCustomAnimations(
            R.anim.fragment_fade_enter,
            R.anim.fragment_fade_exit,
            R.anim.fragment_fade_enter,
            R.anim.fragment_fade_exit
        )
        tr.replace(R.id.container, fragment)
        tr.commitAllowingStateLoss()
        curFragment = fragment
    }

    override fun onBackPressed() {
        val pair = popFragmentFromBackStack()
        if (pair != null) {
            backTo(pair.first!!, pair.second!!)
        } else {
            super.onBackPressed()
        }
    }

    private fun backTo(tabId: Int, fragment: Fragment) {
        if (tabId != curTabId) {
            curTabId = tabId
        }
        replaceFragment(fragment)
        supportFragmentManager.executePendingTransactions()
    }

    private fun adjustMarginTop() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.clAppbar) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = insets.top
            }

            windowInsets
        }
    }

    private fun adjustPaddingBottom() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.container) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(bottom = insets.bottom)

            windowInsets
        }
    }

    companion object {
        private lateinit var context: Context
        lateinit var tvTitle: TextView
        lateinit var cvBack: CardView

        @JvmField
        var curFragment: Fragment? = null
    }

    override fun onStart() {
        super.onStart()

        val colorAppBar = TypedValue()
        val themeColor4 = this.theme
        themeColor4.resolveAttribute(R.attr.ColorAppBar, colorAppBar, true)
        @ColorInt val ColorAppBar = colorAppBar.data

        window.navigationBarColor = Color.TRANSPARENT
        window.statusBarColor = ColorAppBar
        adjustMarginTop()
        adjustPaddingBottom()
    }
}