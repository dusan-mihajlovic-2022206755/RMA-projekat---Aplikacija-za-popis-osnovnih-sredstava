package com.example.popisos.skeniranje

import com.example.popisosnovnihsredstava.adapters.PopisStavkaAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.popisosnovnihsredstava.MainActivity
import com.example.popisosnovnihsredstava.databinding.FragmentSkeniranjeStavkiBinding
import com.example.popisosnovnihsredstava.entities.PopisStavka
import sqlite.SQLitePopisHelper
import sqlite.SQLiteSifarnikHelper
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
        binding.textviewLokacija.text = "Lokacija: " + arguments?.getString("lokacija_naziv")
        binding.textviewRacunopolagac.text = "Računopolagač: " + arguments?.getString("racunopolagac_naziv")

        binding.buttonToMainActivity?.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            intent.putExtra("id_popis", popisID)
            requireActivity().finish()
        }

        return binding.root
    }

    private fun InitRecyclerView() {
        val dbHelper = SQLitePopisHelper(requireContext())
        val popisStavke = dbHelper.getPopisStavkeByIdPopis(popisID)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = PopisStavkaAdapter(requireContext(), popisStavke.toMutableList())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.edittextUnosstavke.requestFocus()
        binding.edittextUnosstavke.selectAll()

        sharedViewModel.popisID.observe(viewLifecycleOwner) { id ->
            popisID = id
            InitRecyclerView() //da se osigura da bude nakon fetchovanja popisID...
        }

        binding.edittextUnosstavke.setOnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                val inputText = v.text.toString()
                if (inputText.isNotEmpty()) {
                    popisiStavku(inputText)
                }
                binding.edittextUnosstavke.setText("")
                binding.edittextUnosstavke.requestFocus()
            }
            false
        }
    }

    private fun popisiStavku(inputText: String) {
        try {
            val sifarnik = SQLiteSifarnikHelper(requireContext())
            val srchRes = sifarnik.searchArtikli(inputText.trim());
            if (srchRes.isEmpty()) {
                Toast.makeText(requireContext(), "Nije pronađen nijedan artikal za ovaj unos!", Toast.LENGTH_SHORT).show()
                return
            }
            artikalID = srchRes[0].id //napraviti popup da bira?

            val popisDB = SQLitePopisHelper(requireContext())
            val postojeciID : Int = popisDB.checkIfPopisStavkaExists(artikalID, popisID, lokacijaID, racunopolagacID)
            if (postojeciID == -1){
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
                if(!unesiStavkuUBazu(stavka)){
                    Toast.makeText(requireContext(), "Greška!", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                popisDB.incrementKolicinaById(postojeciID)
            }
            Toast.makeText(requireContext(), "Stavka uspešno popisana!", Toast.LENGTH_SHORT).show()
            //binding.recyclerView.adapter?.notifyItemInserted(0)
            //binding.recyclerView.adapter?.notifyDataSetChanged()
            InitRecyclerView()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
        }
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
            throw ex
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}