package com.example.recyclerviewsuperheroes

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclerviewsuperheroes.data.SuperHeroProvider
import com.example.recyclerviewsuperheroes.databinding.ActivityMainBinding
import com.example.recyclerviewsuperheroes.domain.SuperHero
import com.example.recyclerviewsuperheroes.presentation.SuperHeroAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        // El manager establece como se gestiona el layout
        val layoutManager = LinearLayoutManager(this)
        //val layoutManager = GridLayoutManager(this, 2)

        // Separador:
        // Se puede decir la horientación manualmente o a través del manager
        //val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        val decoration = DividerItemDecoration(this, layoutManager.orientation)

        // Establecer como se gestiona el layout
        binding.superHeroRecycler.layoutManager = layoutManager

        // Establecer el adaptador, pasando la lista:
        binding.superHeroRecycler.adapter = SuperHeroAdapter(SuperHeroProvider.superheroList) { superHero ->
            onItemSelected(superHero)
        }

        // Establecer decoración
        binding.superHeroRecycler.addItemDecoration(decoration)
    }

    private fun onItemSelected(superHero: SuperHero) {
        Toast.makeText(this, superHero.superHeroName, Toast.LENGTH_SHORT).show()
    }
}