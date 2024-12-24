package com.example.popisosnovnihsredstava

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.popisosnovnihsredstava.databinding.FragmentRacunopolagacBinding

class RacunopolagacFragment : Fragment() {

    private var _binding: FragmentRacunopolagacBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRacunopolagacBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // You can implement any functionality here after navigation
        // For example, a simple button click to go back
        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_Second2Fragment_to_First2Fragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
