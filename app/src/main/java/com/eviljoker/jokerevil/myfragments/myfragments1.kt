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
import com.eviljoker.jokerevil.databinding.Myfragments1Binding
import com.eviljoker.jokerevil.myadapters.myadapter1

private var igra: GridView? = null
private var igraada: myadapter1? = null
private var timeigra: Chronometer? = null


class myfragments1 : Fragment() {
    var igraSummary = 0

    var igra1: Int = 2

    var igra2 = 0
    var igra2down: Int = 3

    private var binding : Myfragments1Binding? = null
    private val binding1 get() = binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = Myfragments1Binding.inflate(inflater, container, false)
        return binding1!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


         binding!!.back.setOnClickListener {

             bindingMains.container1.visibility = View.GONE
             bindingMains.games.visibility = View.VISIBLE
         }

        
        
        igra = binding!!.gameView
        igra!!.numColumns = igra1
        igra!!.isEnabled = true

        igraada = myadapter1(requireContext(), igra1, igra2down)
        igra!!.adapter = igraada

        timeigra = binding!!.timers
        binding!!.progresssumm.text = "0"
        timeigra!!.start()


        igra!!.onItemClickListener =
            AdapterView.OnItemClickListener { parent, v, position, id ->
                igraada!!.OSA()
                igraada!!.posclears(position)
                igra2++
                binding1!!.progresssumm.setText(igra2.toString())
                var igranumer = (-5..10).random()
                var igrasumvel = igranumer + igraSummary
                igraSummary = igrasumvel
                binding1!!.pointsumm.text = igrasumvel.toString()
                Log.d("nums", "$igrasumvel")
                Log.d("nums", "${igraSummary}")
                if (igraSummary >= 350 && igra2 >= 200) {
                    igra = binding!!.gameView as GridView
                    igra!!.numColumns = igra2down
                    igra!!.isEnabled = true

                    igraada = myadapter1(requireContext(), igra1, igra2down)
                    igra!!.adapter = igraada
                    Toast.makeText(requireContext(), "LOSE", Toast.LENGTH_SHORT).show()

                    igraSummary = 0
                }else if(igraada!!.gmlastover() )
                {
                    if (igra2down <= 9){
                        var yroven = igra2down
                        igra2down = yroven + 2

                        igra = binding!!.gameView
                        igra!!.numColumns = igra1
                        igra!!.isEnabled = true
                        Toast.makeText(requireContext(), "No", Toast.LENGTH_SHORT).show()
                        igraSummary = 0
                        binding!!.pointsumm.text = igraSummary.toString()
                        igraada = myadapter1(requireContext(), igra1, igra2down)
                        igra!!.adapter = igraada
                    }
                    else{
                        Toast.makeText(requireContext(), "Yes", Toast.LENGTH_SHORT).show()

                    }

                }
            }
    }
    companion object {

        fun newInstance() = myfragments1()
    }
}