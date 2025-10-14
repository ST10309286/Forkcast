package com.example.forkcast

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.forkcast.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private val splashDelay = 2000L // 2 seconds
    private val handler = Handler(Looper.getMainLooper())
    private val navigateRunnable = Runnable {
        if (isAdded && view != null) {
            try {
                findNavController().navigate(R.id.action_splash_to_auth)
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Schedule navigation
        handler.postDelayed(navigateRunnable, splashDelay)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Cancel navigation if user leaves splash early
        handler.removeCallbacks(navigateRunnable)
        _binding = null
    }
}