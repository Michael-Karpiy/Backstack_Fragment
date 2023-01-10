package com.backstackfragment.BackStack

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import com.tarkovinfo.Tools.BackStackKT.BackStackEntry
import com.tarkovinfo.Tools.BackStackKT.BackStackManager

abstract class BackStackActivity : AppCompatActivity() {

    private var backStackManager: BackStackManager? = null
    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        backStackManager = BackStackManager()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        backStackManager = null
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(STATE_BACK_STACK_MANAGER, backStackManager!!.saveState())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        backStackManager!!.restoreState(savedInstanceState.getParcelable(STATE_BACK_STACK_MANAGER))
    }

    protected fun pushFragmentToBackStack(hostId: Int, fragment: Fragment) {
        try {
            val entry = BackStackEntry.create(
                supportFragmentManager, fragment
            )
            backStackManager!!.push(hostId, entry)
        } catch (e: Exception) {
            Log.e("MultiBackStack", "Failed to add fragment to back stack", e)
        }
    }

    protected fun popFragmentFromBackStack(hostId: Int): Fragment? {
        val entry = backStackManager!!.pop(hostId)
        return entry?.toFragment(this)
    }

    protected fun popFragmentFromBackStack(): Pair<Int, Fragment>? {
        val pair = backStackManager!!.pop()
        return if (pair != null) Pair.create(pair.first, pair.second.toFragment(this)) else null
    }

    protected fun resetBackStackToRoot(hostId: Int) {
        backStackManager!!.resetToRoot(hostId)
    }

    companion object {
        lateinit var sharedPreferences: SharedPreferences
        lateinit var editor: SharedPreferences.Editor
        var PREFERENCE_FILE = "preference_file"
        private const val STATE_BACK_STACK_MANAGER = "back_stack_manager"

        @JvmField
        var Theme0 = 0
        var Theme1 = 1
        var Theme2 = 2
    }
}