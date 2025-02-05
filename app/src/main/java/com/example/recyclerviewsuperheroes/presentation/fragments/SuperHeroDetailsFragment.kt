package com.example.recyclerviewsuperheroes.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.recyclerviewsuperheroes.data.SuperHeroProvider
import com.example.recyclerviewsuperheroes.databinding.FragmentSuperHeroDetailsBinding


class SuperHeroDetailsFragment : Fragment() {
    private val safeArgs: SuperHeroDetailsFragmentArgs by navArgs()

    private lateinit var binding: FragmentSuperHeroDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSuperHeroDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener SuperHero
        val superHero = SuperHeroProvider.superheroList[safeArgs.position]

        // Establecer imagen
        Glide.with(binding.root.context).load(superHero.photo).into(binding.imageView)

        // Establecer campos de texto
        binding.tvSuperHeroNameDetails.text = superHero.superHeroName
        binding.tvRealNameDetails.text = superHero.realName
        binding.tvPublisherDetails.text = superHero.publisher
    }
}