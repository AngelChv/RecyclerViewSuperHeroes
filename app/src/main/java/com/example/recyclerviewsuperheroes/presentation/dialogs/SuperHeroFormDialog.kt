package com.example.recyclerviewsuperheroes.presentation.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.recyclerviewsuperheroes.data.SuperHeroProvider
import com.example.recyclerviewsuperheroes.databinding.FragmentSuperHeroFormBinding

class SuperHeroFormDialog(
    private val itemPosition: Int,
    private val onSubmit: () -> Unit,
) : DialogFragment() {
    private lateinit var binding: FragmentSuperHeroFormBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentSuperHeroFormBinding.inflate(layoutInflater)

        binding.bttnConfirmForm.setOnClickListener {
            val superHero = SuperHeroProvider.superheroList[itemPosition]
            superHero.superHeroName = binding.etNameForm.text.toString()
            superHero.realName = binding.etRealNameForm.text.toString()
            superHero.publisher = binding.etPublisherForm.text.toString()
            onSubmit()
            dialog?.dismiss()
        }

        return AlertDialog.Builder(requireContext()).apply {
            setTitle("Editar Super Heroe")
            setView(binding.root)
        }.create()
    }
}