package com.example.recyclerviewsuperheroes.presentation

import android.content.DialogInterface.OnClickListener
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.recyclerviewsuperheroes.R
import com.example.recyclerviewsuperheroes.data.SuperHeroProvider
import com.example.recyclerviewsuperheroes.databinding.ItemSuperheroBinding
import com.example.recyclerviewsuperheroes.domain.SuperHero

class SuperHeroViewHolder(
    itemView: View,
    private val onClickListener: (SuperHero) -> Unit,
    private val onLongClickListener: (SuperHero) -> Unit,
) : ViewHolder(itemView) {
    private val binding = ItemSuperheroBinding.bind(itemView)

    fun render(superHero: SuperHero, isSelected: Boolean) {
        binding.apply {
            if (isSelected) {
                ivPhoto.setImageResource(R.drawable.check_box_24dp_e8eaed_fill0_wght400_grad0_opsz24)
            } else {
                Glide.with(ivPhoto.context).load(superHero.photo).into(ivPhoto)
            }
            tvSuperHeroName.text = superHero.superHeroName
            tvRealName.text = superHero.realName
            tvPublisher.text = superHero.publisher
            // Evento en una parte de la vista
            //ivPhoto.setOnClickListener {
            //    Toast.makeText(ivPhoto.context, superHero.realName, Toast.LENGTH_SHORT).show()
            //}

            // Evento en toda la vista.
            // Mediante itemView, no confundir con el parametro del constructor.
            // Es un atributo de ViewHolder que toma el valor del parametro, pero no es la misma variable
            //itemView.setOnClickListener {
                //Toast.makeText(ivPhoto.context, superHero.superHeroName, Toast.LENGTH_SHORT).show()
                // Si se cambiase algo de itemView, se estaría cambiando del viewHolder
                // por lo que si se hace scroll cada x items se repetiría ese cambio, por como
                // funciona recylcerview y como repite los viewHolders.
                //itemView.setBackgroundColor(Color.RED)
                // Otra forma de usar colores de recursos
                //itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.black))
            //}

            // Evento mediante lambda:
            itemView.setOnClickListener {
                onClickListener(superHero)
            }

            itemView.setOnLongClickListener {
                onLongClickListener(superHero)
                true
            }
        }
    }
}