package com.example.popisosnovnihsredstava

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.popisosnovnihsredstava.databinding.FragmentRacunopolagacBinding
import com.example.popisosnovnihsredstava.helpers.SQLiteSifarnikHelper

class RacunopolagacFragment : Fragment() {

    private var _binding: FragmentRacunopolagacBinding? = null
    private val binding get() = _binding!!
    private var lokacijaID :Int = 0
    private var lokacijaNaziv : String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRacunopolagacBinding.inflate(inflater, container, false)
        lokacijaID = arguments?.getInt("id_lokacija")!!
        lokacijaNaziv = arguments?.getString("lokacija_naziv")!!
        binding.textViewLokacija.text = "Lokacija: " + lokacijaNaziv

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editTextUnosRacunopolagaca.setOnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                val inputText = v.text.toString()
                if (inputText.isNotEmpty()) {
                    try {
                        val sifarnik = SQLiteSifarnikHelper(requireContext())
                        val pronadjeniRacunopolagaci = sifarnik.searchRacunopolagac(inputText)
                        if (pronadjeniRacunopolagaci.isNotEmpty()){
                            val racunopolagacID = pronadjeniRacunopolagaci[0].id
                            val racunopolagacNaziv = pronadjeniRacunopolagaci[0].sifra + " - " + pronadjeniRacunopolagaci[0].ime + " - " + pronadjeniRacunopolagaci[0].prezime
                            val bundle = Bundle().apply {
                                putString("lokacija_naziv", lokacijaNaziv)
                                putInt("id_lokacija", lokacijaID)
                                putString("racunopolagac_naziv", racunopolagacNaziv)
                                putInt("id_racunopolagac", racunopolagacID)
                            }
                            findNavController().navigate(R.id.action_to_skeniranje_stavki, bundle)
                        }
                        else{
                            Toast.makeText(requireContext(), "Nije pronađen nijedan artikal za ovaj unos!", Toast.LENGTH_SHORT).show()
                        }

                    }
                    catch (ex:Exception){
                        Toast.makeText(requireContext(), ex.message, Toast.LENGTH_SHORT).show()
                    }
                    return@setOnEditorActionListener true
                }
            }
            false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
