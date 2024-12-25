package com.example.popisosnovnihsredstava

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.popisosnovnihsredstava.databinding.FragmentUnoslokacijeBinding

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

        binding.editTextUnosLokacije.setOnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                val inputText = v.text.toString()
                if (inputText.isNotEmpty()) {
                    val bundle = Bundle().apply {
                        putString("lokacija_naziv", inputText)
                        putString("id_lokacija", inputText)
                    }
                    findNavController().navigate(R.id.action_to_racunopolagac, bundle)
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
