package com.tarkovinfo.Tools.BackStackKT

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import androidx.core.util.Pair
import java.util.*

open class BackStackManager {
    private val backStacks = LinkedList<BackStack>()
    fun push(hostId: Int, entry: BackStackEntry) {
        var backStack = peekBackStack(hostId)
        if (backStack == null) {
            backStack = BackStack(hostId)
            backStacks.push(backStack)
        }
        backStack.push(entry)
    }

    fun pop(hostId: Int): BackStackEntry? {
        val backStack = peekBackStack(hostId) ?: return null
        return pop(backStack)
    }

    fun pop(): Pair<Int, BackStackEntry>? {
        val backStack = peekBackStack() ?: return null
        return Pair.create(backStack.hostId, pop(backStack))
    }

    private fun pop(backStack: BackStack): BackStackEntry {
        val entry = backStack.pop()!!
        if (backStack.empty()) {
            backStacks.remove(backStack)
        }
        return entry
    }

    fun resetToRoot(hostId: Int) {
        val backStack = getBackStack(hostId) ?: return
        resetToRoot(backStack)
    }

    private fun resetToRoot(backStack: BackStack) {
        while (true) {
            val entry = backStack.pop()!!
            if (backStack.empty()) {
                backStack.push(entry)
                return
            }
        }
    }

    private fun peekBackStack(hostId: Int): BackStack? {
        val index = indexOfBackStack(hostId)
        if (index == UNDEFINED_INDEX) {
            return null
        }
        val backStack = backStacks[index]
        if (index != FIRST_INDEX) {
            backStacks.removeAt(index)
            backStacks.push(backStack)
        }
        return backStack
    }

    private fun peekBackStack(): BackStack? {
        return backStacks.peek()
    }

    private fun getBackStack(hostId: Int): BackStack? {
        val index = indexOfBackStack(hostId)
        return if (index == UNDEFINED_INDEX) {
            null
        } else backStacks[index]
    }

    private fun indexOfBackStack(hostId: Int): Int {
        val size = backStacks.size
        for (i in 0 until size) {
            if (backStacks[i].hostId == hostId) {
                return i
            }
        }
        return UNDEFINED_INDEX
    }

    fun saveState(): Parcelable {
        return SavedState(backStacks)
    }

    fun restoreState(state: Parcelable?) {
        if (state != null) {
            val savedState = state as SavedState
            backStacks.addAll(savedState.backStacks)
        }
    }

    internal class SavedState : Parcelable {
        val backStacks: MutableList<BackStack>

        constructor(backStacks: MutableList<BackStack>) {
            this.backStacks = backStacks
        }

        private constructor(`in`: Parcel) {
            val size = `in`.readInt()
            backStacks = ArrayList(size)
            for (i in 0 until size) {
                backStacks.add(BackStack.CREATOR.createFromParcel(`in`)!!)
            }
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            val size = backStacks.size
            out.writeInt(size)
            for (i in 0 until size) {
                backStacks[i].writeToParcel(out, flags)
            }
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object {
            @JvmField
            val CREATOR: Creator<SavedState?> = object : Creator<SavedState?> {
                override fun createFromParcel(`in`: Parcel): SavedState? {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    companion object {
        private const val FIRST_INDEX = 0
        private const val UNDEFINED_INDEX = -1
    }
}