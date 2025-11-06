package com.example.forkcast

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.forkcast.databinding.FragmentProfileBinding
import java.util.Locale

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val languages = listOf("English", "isiZulu", "Afrikaans")
    private val languageCodes = listOf("en", "zu", "af")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val prefs = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)

        // 🟢 Language Spinner
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerLanguage.adapter = adapter

        val currentLang = prefs.getString("language", "en")
        val selectedIndex = languageCodes.indexOf(currentLang)
        if (selectedIndex >= 0) binding.spinnerLanguage.setSelection(selectedIndex)

        binding.spinnerLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedLang = languageCodes[position]
                if (selectedLang != currentLang) {
                    prefs.edit().putString("language", selectedLang).apply()
                    setLocale(selectedLang)
                    requireActivity().recreate()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // 🟢 Load user info
        val userPrefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val username = userPrefs.getString("name", "Guest")
        val email = userPrefs.getString("email", "Not logged in")

        binding.profileName.text = username


        return binding.root
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        requireContext().resources.updateConfiguration(config, requireContext().resources.displayMetrics)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
