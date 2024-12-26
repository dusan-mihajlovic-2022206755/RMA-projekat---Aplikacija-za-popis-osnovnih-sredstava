package com.example.popisosnovnihsredstava

import adapters.PopisStavkaAdapter
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.popisosnovnihsredstava.databinding.FragmentSkeniranjeStavkiBinding
import com.example.popisosnovnihsredstava.entities.PopisStavka
import com.example.popisosnovnihsredstava.helpers.SQLiteSifarnikHelper
import java.time.LocalDateTime

class SkeniranjeStavkiFragment : Fragment() {

    private var _binding: FragmentSkeniranjeStavkiBinding? = null
    private val binding get() = _binding!!
    private var lokacijaID : Int = 0
    private var racunopolagacID : Int = 0
    private  var popisID : Int = 0
    private  var artikalID : Int = 0
    private var userID : Int = 0
    private val sharedViewModel: SkeniranjeSharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSkeniranjeStavkiBinding.inflate(inflater, container, false)

        lokacijaID = arguments?.getInt("id_lokacija")!!.toInt()
        racunopolagacID = arguments?.getInt("id_racunopolagac")!!.toInt()
        popisID = arguments?.getInt("id_popis")!!.toInt()
        val sharedPreferences = activity?.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        if (sharedPreferences != null) {
            userID = sharedPreferences.getInt("id_user", 0)
        }
        binding.textviewLokacija.text = "Lokacija" + arguments?.getString("lokacija_naziv")
        binding.textviewRacunopolagac.text = "Računopolagač" + arguments?.getString("racunopolagac_naziv")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.edittextUnosstavke.requestFocus()

        sharedViewModel.popisID.observe(viewLifecycleOwner) { id ->
            popisID = id
        }

        binding.edittextUnosstavke.setOnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                val inputText = v.text.toString()
                if (inputText.isNotEmpty()) {
                    popisiStavku(inputText)
                }
            }
            false
        }
    }

    private fun popisiStavku(inputText: String) {
        val sifarnik = SQLiteSifarnikHelper(requireContext())
        val srchRes = sifarnik.searchArtikli(inputText.trim());
        if (srchRes.isNotEmpty()) {
            artikalID = srchRes[0].id //napraviti popup da bira?
        }
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
        unesiStavkuUBazu(stavka)
        Toast.makeText(requireContext(), "Stavka uspešno popisana!", Toast.LENGTH_SHORT).show()

        val dbHelper = SQLitePopisHelper(requireContext())
        val popisStavke = dbHelper.getPopisStavkeByIdPopis(popisID)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = PopisStavkaAdapter(popisStavke)
    }

    private fun unesiStavkuUBazu(stavka: PopisStavka): Boolean {
        try {
            val popisBaza = SQLitePopisHelper(requireContext())
            popisBaza.insertPopisStavka(
                idPopis = stavka.idPopis,
                idArtikal = stavka.idArtikal,
                idLokacija = stavka.idLokacija,
                kolicina = stavka.kolicina,
                idUser = stavka.idUser,
                vremePopisivanja = stavka.vremePopisivanja,
                idRacunopolagac = stavka.idRacunopolagac
            )
            return true
        } catch (ex: Exception) {
            return false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}