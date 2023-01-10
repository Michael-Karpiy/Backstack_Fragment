package com.tarkovinfo.Tools.BackStackKT

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import java.util.*


class BackStack : Parcelable {
    val hostId: Int
    private val entriesStack = Stack<BackStackEntry>()

    constructor(hostId: Int) {
        this.hostId = hostId
    }

    fun push(entry: BackStackEntry) {
        entriesStack.push(entry)
    }

    fun pop(): BackStackEntry? {
        return if (empty()) null else entriesStack.pop()
    }

    fun empty(): Boolean {
        return entriesStack.empty()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        out.writeInt(hostId)
        val size = entriesStack.size
        out.writeInt(size)
        for (i in 0 until size) {
            entriesStack[i].writeToParcel(out, flags)
        }
    }

    private constructor(`in`: Parcel) {
        hostId = `in`.readInt()
        val size = `in`.readInt()
        for (i in 0 until size) {
            entriesStack.push(BackStackEntry.CREATOR.createFromParcel(`in`))
        }
    }

    companion object {
        @JvmField
        val CREATOR: Creator<BackStack?> = object : Creator<BackStack?> {
            override fun createFromParcel(`in`: Parcel): BackStack? {
                return BackStack(`in`)
            }

            override fun newArray(size: Int): Array<BackStack?> {
                return arrayOfNulls(size)
            }
        }
    }
}