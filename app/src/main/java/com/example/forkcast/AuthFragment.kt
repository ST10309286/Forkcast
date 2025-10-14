package com.example.forkcast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.forkcast.R
import com.example.forkcast.databinding.FragmentAuthBinding

class AuthFragment : Fragment() {

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)

        // Navigate to Login screen using correct action ID
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_auth_to_login)
        }

        // Navigate to Register screen using correct action ID
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_auth_to_register)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
