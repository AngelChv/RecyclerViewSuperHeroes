package com.example.recyclerviewsuperheroes

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclerviewsuperheroes.data.SuperHeroProvider
import com.example.recyclerviewsuperheroes.databinding.ActivityMainBinding
import com.example.recyclerviewsuperheroes.presentation.SuperHeroAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var actionMode: ActionMode? = null
    private val actionModeCallBack = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            mode.menuInflater.inflate(R.menu.contextual_menu, menu)
            return true
        }

        // Se llama cada vez que el action mode se muestra. Siempre despúes de onCreateActionMode
        // y se llama multiples veces si el modo es invalidado.
        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false // Si no se hace nada se devuelve false.
        }

        // Es llamado cuando se pulsa un elemento del menú contextual.
        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.actionModeEdit -> {
                    TODO()
                    true
                }
                R.id.actionModeDelte -> {
                    TODO()
                    true
                }
                else -> false
            }
        }

        // Se llama cuando el usuario sale del menú
        override fun onDestroyActionMode(mode: ActionMode) {
            val selectedItems = SuperHeroProvider.itemsSelected
            binding.superHeroRecycler.adapter?.notifyItemRangeChanged(
                selectedItems.min(),
                selectedItems.max() - selectedItems.min() + 1
            )
            SuperHeroProvider.itemsSelected.clear()
            actionMode = null
        }
    }

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

        initToolbar()
        initRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // necesario para añadir el menú
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        //binding.toolbar.inflateMenu(R.menu.main_menu)
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
        binding.superHeroRecycler.adapter = SuperHeroAdapter(
            SuperHeroProvider.superheroList,
            onLongClickListener = { position -> onItemLongClick(position) },
            onClickListener = { position -> onItemSelected(position) },
        )

        // Establecer decoración
        binding.superHeroRecycler.addItemDecoration(decoration)
    }

    private fun onItemSelected(position: Int) {
        if (actionMode != null) {
         onActionModeItemClick(position)
        } else {
            val superHero = SuperHeroProvider.superheroList[position]
            Toast.makeText(this, superHero.superHeroName, Toast.LENGTH_SHORT).show()
        }
    }

    private fun onItemLongClick(position: Int) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallBack)
        }

        onActionModeItemClick(position)
    }

    private fun onActionModeItemClick(position: Int) {
        if (SuperHeroProvider.itemsSelected.contains(position)) {
            SuperHeroProvider.itemsSelected.remove(position)
        } else {
            SuperHeroProvider.itemsSelected.add(position)
        }
        binding.superHeroRecycler.adapter?.notifyItemChanged(position)
    }
}