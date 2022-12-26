package com.eviljoker.jokerevil.myadapters

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.eviljoker.jokerevil.R
import java.util.*
import kotlin.collections.ArrayList

internal class myadapter1(
    private val conth: Context,
    private val mama: Int,
    private val ridishnik: Int) : BaseAdapter() {
    private val pynkts2: ArrayList<String?> = ArrayList()
    private val tytatam : String
    private val zdestyt : Resources
    private enum class siryn {
        koko, pipiska, lol
    }
    private enum class itnumers {
        numbersOne, numbersTwo
    }
    private val pIm : ArrayList<siryn> = ArrayList()
    private val gListok: ArrayList<itnumers>
    init {
        gListok = ArrayList()
        tytatam = "joker"
        zdestyt = conth.resources
        iconspages()
        posclears()
    }

    private fun iconspages() {
        pynkts2.clear()
        for (i in 0 until mama * ridishnik / 2) {
            pynkts2.add(tytatam + Integer.toString(i))
            pynkts2.add(tytatam + Integer.toString(i))
        }
        Collections.shuffle(pynkts2)
    }

    private fun posclears() {
        pIm.clear()
        for (i in 0 until mama * ridishnik) pIm.add(siryn.pipiska)
    }

    override fun getCount(): Int {
        return mama * ridishnik
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(ps: Int, cp: View?, VG: ViewGroup): View {
        val imagecounts: ImageView =
            if (cp == null) ImageView(conth) else cp as ImageView
        when (pIm[ps]) {
            siryn.koko -> {
                val readID = zdestyt.getIdentifier(pynkts2[ps], "drawable", conth.packageName)
                imagecounts.setImageResource(readID)

            }

            siryn.pipiska -> imagecounts.setImageResource(R .drawable.closecard)
            else -> imagecounts.setImageResource(R.drawable.carddelete)

        }
        return imagecounts
    }
    fun gmlastover(): Boolean {
        return if (pIm.indexOf(siryn.pipiska) < 0) true else false
    }
    fun posclears(doyouloveme: Int): Boolean {
        if (pIm[doyouloveme] === siryn.lol || pIm[doyouloveme] === siryn.koko) return false
        if (pIm[doyouloveme] !== siryn.lol) pIm[doyouloveme] = siryn.koko
        notifyDataSetChanged()
        return true
    }
    fun OSA() {
        val OSAowe = pIm.indexOf(siryn.koko)
        val OSAtwo = pIm.lastIndexOf(siryn.koko)
        if (OSAowe == OSAtwo) return
        if (pynkts2[OSAowe] == pynkts2[OSAtwo]) {
            pIm[OSAowe] = siryn.lol
            pIm[OSAtwo] = siryn.lol
        } else {
            pIm[OSAowe] = siryn.pipiska
            pIm[OSAtwo] = siryn.pipiska
        }
        return
    }

}