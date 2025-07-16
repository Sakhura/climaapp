package com.sakhura.climaapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sakhura.climaapp.R
import com.sakhura.climaapp.model.PronosticoDia

class PronosticoAdapter : RecyclerView.Adapter<PronosticoAdapter.ViewHolder>() {
    private val lista = mutableListOf<PronosticoDia>()

    fun actualizarLista(nueva: List<PronosticoDia>) {
        lista.clear()
        lista.addAll(nueva)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_dia_pronostico, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: PronosticoDia) {
            val tvFecha = itemView.findViewById<TextView>(R.id.tvFecha)
            val tvTemp = itemView.findViewById<TextView>(R.id.tvTemp)
            val tvDesc = itemView.findViewById<TextView>(R.id.tvDesc)
            tvFecha.text = item.dt_txt
            tvTemp.text = "${item.main.temp}°C"
            tvDesc.text = item.weather[0].description
        }
    }
}