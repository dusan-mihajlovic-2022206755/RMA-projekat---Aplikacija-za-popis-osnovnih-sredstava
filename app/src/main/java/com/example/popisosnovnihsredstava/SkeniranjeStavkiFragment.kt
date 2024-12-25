package com.example.popisosnovnihsredstava

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.popisosnovnihsredstava.databinding.FragmentSkeniranjeStavkiBinding
import com.example.popisosnovnihsredstava.entities.PopisStavka
import com.example.popisosnovnihsredstava.helpers.SQLiteSifarnikHelper
import java.time.LocalDate
import java.time.LocalDateTime

class SkeniranjeStavkiFragment : Fragment() {

    private var _binding: FragmentSkeniranjeStavkiBinding? = null
    private val binding get() = _binding!!
    private var lokacijaID : Int = 0
    private var racunopolagacID : Int = 0
    private  var popisID : Int = 0
    private  var artikalID : Int = 0
    private var userID : Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSkeniranjeStavkiBinding.inflate(inflater, container, false)
        binding.textviewLokacija.text = arguments?.getString("lokacija_naziv")
        binding.textviewRacunopolagac.text = arguments?.getString("racunopolagac_naziv")
        lokacijaID = arguments?.getString("id_lokacija")!!.toInt()
        racunopolagacID = arguments?.getString("id_racunopolagac")!!.toInt()
        popisID = arguments?.getString("id_popis")!!.toInt()
        val sharedPreferences = activity?.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        if (sharedPreferences != null) {
            userID = sharedPreferences.getInt("id_user", 0)
        }

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.edittextUnosstavke.requestFocus()
        binding.edittextUnosstavke.setOnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                val inputText = v.text.toString()
                if (inputText.isNotEmpty()) {
                    val sifarnik = SQLiteSifarnikHelper(requireContext())
                    val srchRes = sifarnik.searchArtikli(inputText.trim());
                    //temp
                    if (srchRes[0] != null){
                        artikalID = srchRes[0].id;
                    }
                    ///
                    val stavka = PopisStavka(
                        id = 0,
                        idPopis = popisID,
                        idArtikal = artikalID,
                        idLokacija = lokacijaID,
                        kolicina = 1,
                        idUser = userID,
                        idRacunopolagac = racunopolagacID,
                        vremePopisivanja = LocalDateTime.now()
                    )
                    unesiStavku(stavka)
                }
            }
            false
        }
    }
    fun unesiStavku(stavka : PopisStavka){
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}