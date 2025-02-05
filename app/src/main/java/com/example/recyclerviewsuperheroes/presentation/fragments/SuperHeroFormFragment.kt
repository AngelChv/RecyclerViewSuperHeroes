package com.example.recyclerviewsuperheroes.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.recyclerviewsuperheroes.databinding.FragmentSuperHeroFormBinding

class SuperHeroFormFragment : Fragment() {
    private lateinit var binding: FragmentSuperHeroFormBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSuperHeroFormBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bttnConfirmForm.setOnClickListener {
            Log.i("form", "button")

            // Salir del dialog en caso de que sea el contenedor
            if (parentFragment is DialogFragment) {
                (parentFragment as DialogFragment).dismiss()
            }
        }
    }
}