package com.tarkovinfo.Tools.BackStackKT

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.backstackfragment.BackStack.BackStackActivity

class BackStackEntry : Parcelable {
    val fname: String?
    val state: Fragment.SavedState?
    val args: Bundle?

    constructor(fname: String, state: Fragment.SavedState?, args: Bundle?) {
        this.fname = fname
        this.state = state
        this.args = args
    }

    fun toFragment(context: BackStackActivity): Fragment {
        val f = Fragment.instantiate(context, fname!!)
        f.setInitialSavedState(state)
        f.arguments = args
        return f
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        out.writeString(fname)
        out.writeBundle(args)
        if (state == null) {
            out.writeInt(NO_STATE)
        } else if (state.javaClass == Fragment.SavedState::class.java) {
            out.writeInt(SAVED_STATE)
            state.writeToParcel(out, flags)
        } else {
            out.writeInt(PARCELABLE_STATE)
            out.writeParcelable(state, flags)
        }
    }

    private constructor(`in`: Parcel) {
        val loader = javaClass.classLoader
        fname = `in`.readString()
        args = `in`.readBundle(loader)
        state = when (`in`.readInt()) {
            NO_STATE -> null
            SAVED_STATE -> Fragment.SavedState.CREATOR.createFromParcel(`in`)
            PARCELABLE_STATE -> `in`.readParcelable(loader)
            else -> throw IllegalStateException()
        }
    }

    companion object {
        fun create(fm: FragmentManager, f: Fragment): BackStackEntry {
            val fname = f.javaClass.name
            val state = fm.saveFragmentInstanceState(f)
            val args = f.arguments
            return BackStackEntry(fname, state, args)
        }

        private const val NO_STATE = -1
        private const val SAVED_STATE = 0
        private const val PARCELABLE_STATE = 1
        @JvmField
        val CREATOR: Creator<BackStackEntry?> = object : Creator<BackStackEntry?> {
            override fun createFromParcel(`in`: Parcel): BackStackEntry? {
                return BackStackEntry(`in`)
            }

            override fun newArray(size: Int): Array<BackStackEntry?> {
                return arrayOfNulls(size)
            }
        }
    }
}