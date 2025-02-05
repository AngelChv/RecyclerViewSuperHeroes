package com.example.recyclerviewsuperheroes.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclerviewsuperheroes.R
import com.example.recyclerviewsuperheroes.data.SuperHeroProvider
import com.example.recyclerviewsuperheroes.databinding.FragmentSuperHeroRecyclerBinding
import com.example.recyclerviewsuperheroes.presentation.dialogs.SuperHeroFormDialog
import com.example.recyclerviewsuperheroes.presentation.recycler.SuperHeroAdapter

class SuperHeroRecyclerFragment : Fragment() {
    private lateinit var binding: FragmentSuperHeroRecyclerBinding
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
                    editItem()
                    actionMode?.finish()
                    true
                }

                R.id.actionModeDelte -> {
                    deleteSelectedItems()
                    actionMode?.finish()
                    true
                }

                else -> false
            }
        }

        // Se llama cuando el usuario sale del menú
        override fun onDestroyActionMode(mode: ActionMode) {
            // Si se destruye el action mode pero todavía hay elementos seleccionados
            // se limpia la lista de las posiciones y se notifica el cambio
            val selectedItems = SuperHeroProvider.itemsSelected
            selectedItems.forEach { position ->
                binding.superHeroRecycler.adapter?.notifyItemChanged(position)
            }
            selectedItems.clear()
            actionMode = null
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Importante! crear el binding asi:
        binding = FragmentSuperHeroRecyclerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        // El manager establece como se gestiona el layout
        val layoutManager = LinearLayoutManager(binding.root.context)
        //val layoutManager = GridLayoutManager(this, 2)

        // Separador:
        // Se puede decir la horientación manualmente o a través del manager
        //val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        val decoration = DividerItemDecoration(binding.root.context, layoutManager.orientation)

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
            Toast.makeText(binding.root.context, superHero.superHeroName, Toast.LENGTH_SHORT).show()

            findNavController().navigate(
                SuperHeroRecyclerFragmentDirections
                    .actionSuperHeroRecyclerFragmentToSuperHeroDetailsFragment(position)
            )
        }
    }

    private fun onItemLongClick(position: Int) {
        if (actionMode == null) {
            // El atributo activity hace referencia al FragmentActivity que contiene a este fragmento
            // Pero es necesario castearlo a un AppCompatActivity para usar strartSuppportActionMode()
            val appCompactActivity = activity as AppCompatActivity
            actionMode = appCompactActivity.startSupportActionMode(actionModeCallBack)
        }

        onActionModeItemClick(position)
    }

    private fun onActionModeItemClick(position: Int) {
        Log.i("position", "$position")
        if (SuperHeroProvider.itemsSelected.contains(position)) {
            // Item ya seleccionado, se elimina de la lista de seleccionados
            SuperHeroProvider.itemsSelected.remove(position)
            // Si no hay ningún item seleccionado, se desactiva el actionMode
            if (SuperHeroProvider.itemsSelected.isEmpty()) {
                actionMode?.finish()
                actionMode = null
            }
        } else {
            // Item sin seleccionar se selecciona
            SuperHeroProvider.itemsSelected.add(position)
        }

        // Eliminar la opción de editar si hay mas de un elemento seleccionado.
        actionMode?.menu?.let {
            if (SuperHeroProvider.itemsSelected.size <= 1) {
                it.findItem(R.id.actionModeEdit).setVisible(true)
            } else {
                it.findItem(R.id.actionModeEdit).setVisible(false)
            }
        }

        // Mostrar cuenta
        actionMode?.title = "${SuperHeroProvider.itemsSelected.size}"

        // Notifcar los cambios
        binding.superHeroRecycler.adapter?.notifyItemChanged(position)
    }

    private fun editItem() {
        val itemSelectedPosition = SuperHeroProvider.itemsSelected.first()
        val superHeroFormDialog = SuperHeroFormDialog(itemSelectedPosition,
            onSubmit = {
                binding.superHeroRecycler.adapter?.notifyItemChanged(itemSelectedPosition)
            })
        activity?.let {
            superHeroFormDialog.show(
                it.supportFragmentManager,
                "CustomDialog"
            )
        }
    }

    private fun deleteSelectedItems() {
        // Eliminar elementos de la lista de manera inversa por si acaso hay problemas con los indices
        // y notificar las eliminaciones
        val selectedItems = SuperHeroProvider.itemsSelected.sortedDescending()

        AlertDialog.Builder(binding.root.context).apply {
            setTitle("Está seguro que desea eliminar ${selectedItems.size} super heroes?")
            setPositiveButton("Si") { _, _ ->
                selectedItems.forEach { position ->
                    SuperHeroProvider.superheroList.removeAt(position)
                    binding.superHeroRecycler.adapter?.notifyItemRemoved(position)
                }
                // Se limpia la lista de selección para que no se vuelva a limpiar en el onDestroyActionMode
                // y por lo tanto no se vuelvan a notificar cambios, ya que los elementos ya se ha eliminado.
                SuperHeroProvider.itemsSelected.clear()
            }
            setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }
        }.show()

    }
}