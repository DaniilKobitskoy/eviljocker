package com.eviljoker.jokerevil.myfragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Chronometer
import android.widget.GridView
import android.widget.Toast
import com.eviljoker.jokerevil.bindingMains
import com.eviljoker.jokerevil.databinding.Myfragments2Binding
import com.eviljoker.jokerevil.myadapters.myadapter2


private var igra: GridView? = null
private var igraada: myadapter2? = null
private var igravremya: Chronometer? = null



class myfragments2 : Fragment() {
    var igrahod = 0
    var igraview: Int = 4
    var igraview2: Int = 3
    var igrasum = 0

    private var binding2 : Myfragments2Binding? = null
    private val binding22 get() = binding2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding2 = Myfragments2Binding.inflate(inflater, container, false)
        return binding22!!.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding22!!.back.setOnClickListener {

            bindingMains.container2.visibility = View.GONE
            bindingMains.games.visibility = View.VISIBLE
        }


    igra = binding2!!.gameView
    igra!!.numColumns = igraview
    igra!!.isEnabled = true

        igraada = myadapter2(requireContext(), igraview,
            igraview2
        )
    igra!!.adapter = igraada


        igravremya = binding2!!.timers
    binding2!!.progresssumm.text = "0"
        igravremya!!.start()


    igra!!.onItemClickListener =
    AdapterView.OnItemClickListener { parent, v, position, id ->
        igraada!!.OSA()
        igraada!!.posclears(position)
        igrasum++
        binding22!!.progresssumm.setText(igrasum.toString())
        var iganumer = (-5..10).random()
        var igrasum = iganumer + igrahod
        igrahod = igrasum
        binding22!!.pointsumm.text = igrasum.toString()
        Log.d("nums", "$igrasum")
        Log.d("nums", "${igrahod}")
        if (igrahod >= 200 && this.igrasum >= 200) {
            igra = binding2!!.gameView as GridView
            igra!!.numColumns = igraview
            igra!!.isEnabled = true

            igraada = myadapter2(requireContext(), igraview,
                igraview2
            )
            igra!!.adapter = igraada
            Toast.makeText(requireContext(), "NO", Toast.LENGTH_SHORT).show()

            igrahod = 0
        }else if(igraada!!.gmlastover() )
        {
            if (igraview2 <= 9){
                var igrayroven = igraview2
                igraview2 = igrayroven + 2

                igra = binding2!!.gameView
                igra!!.numColumns = igraview
                igra!!.isEnabled = true
                Toast.makeText(requireContext(), "Next LVL", Toast.LENGTH_SHORT).show()
                igrahod = 0
                binding2!!.pointsumm.text = igrahod.toString()
                igraada = myadapter2(requireContext(), igraview,
                    igraview2
                )
                igra!!.adapter = igraada
            }
            else{
                Toast.makeText(requireContext(), "Yes", Toast.LENGTH_SHORT).show()

            }

        }
    }



    }
    companion object {

        fun newInstance() =
            myfragments2()
    }
}