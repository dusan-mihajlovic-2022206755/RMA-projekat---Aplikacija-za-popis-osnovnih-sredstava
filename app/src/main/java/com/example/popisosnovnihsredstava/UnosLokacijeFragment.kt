package com.example.popisosnovnihsredstava

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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

        // Set up OnClickListener for the button
        binding.buttonSecond.setOnClickListener {
            val inputText = binding.editTextUnosLokacije.text.toString()

            // If there's any text entered, navigate to RacunopolagacFragment
            if (inputText.isNotEmpty()) {
                findNavController().navigate(R.id.action_Second2Fragment_to_First2Fragment)
            }
        }

        // Optional: Handle "Enter" key press if user presses the "Enter" button on keyboard
        binding.editTextUnosLokacije.setOnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                val inputText = v.text.toString()
                if (inputText.isNotEmpty()) {
                    findNavController().navigate(R.id.action_Second2Fragment_to_First2Fragment)
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
