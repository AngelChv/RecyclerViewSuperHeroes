package com.example.recyclerviewsuperheroes.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewsuperheroes.R
import com.example.recyclerviewsuperheroes.data.SuperHeroProvider
import com.example.recyclerviewsuperheroes.domain.SuperHero

class SuperHeroAdapter(
    private val superHeroList: MutableList<SuperHero>,
    private val onClickListener: (Int) -> Unit,
    private val onLongClickListener: (Int) -> Unit,
) :
    RecyclerView.Adapter<SuperHeroViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperHeroViewHolder {
        // Obtener el inflater a través del contexto.
        val inflater = LayoutInflater.from(parent.context)
        // El inflater transforma un xml a una vista, necesita el propio xml y el view padre.
        // Con attachToRoot false indicas que la vista no se añada al padre directamente.
        val superHeroView = inflater.inflate(R.layout.item_superhero, parent, false)
        // La vista se pasa al ViewHolder que se encarga de cargar los datos.
        return SuperHeroViewHolder(superHeroView, onClickListener, onLongClickListener)
    }

    override fun getItemCount(): Int = superHeroList.size

    override fun onBindViewHolder(holder: SuperHeroViewHolder, position: Int) {
        // Importante! no enviar esta position al holder, el ya tiene una propiedad
        // adapterPosition que no da problemas
        holder.render(
            superHeroList[position],
            SuperHeroProvider.itemsSelected.contains(position)
        )
    }
}