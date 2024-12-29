package com.example.popisos.skeniranje

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.popisosnovnihsredstava.R
import com.example.popisosnovnihsredstava.databinding.FragmentUnoslokacijeBinding
import com.google.android.material.appbar.MaterialToolbar
import sqlite.SQLiteSifarnikHelper

class UnosLokacijeFragment : Fragment() {

    private var _binding: FragmentUnoslokacijeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUnoslokacijeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editTextUnosLokacije.requestFocus()
        binding.editTextUnosLokacije.selectAll()

        binding.editTextUnosLokacije.setOnEditorActionListener { v, _, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                val inputText = v.text.toString()
                if (inputText.isNotEmpty()) {
                    try {
                        val sifarnik = SQLiteSifarnikHelper(requireContext())
                        val pronadjeneLokacije = sifarnik.searchLokacije(inputText)
                        if (pronadjeneLokacije.isNotEmpty()){
                            val lokacijaID = pronadjeneLokacije[0].id
                            val lokacijaNaziv = pronadjeneLokacije[0].naziv
                            val bundle = Bundle().apply {
                                putString("lokacija_naziv", lokacijaNaziv)
                                putInt("id_lokacija", lokacijaID)
                            }
                            binding.editTextUnosLokacije.setText("")
                            findNavController().navigate(R.id.fragment_racunopolagac, bundle)
                        }
                        else{
                            Toast.makeText(requireContext(), "Nije pronađena nijedna lokacija za ovaj unos!", Toast.LENGTH_SHORT).show()
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
