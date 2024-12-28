package adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.popisosnovnihsredstava.R
import com.example.popisosnovnihsredstava.entities.PopisStavka
import com.example.popisosnovnihsredstava.formatirajDateTime
import com.example.popisosnovnihsredstava.helpers.SQLiteSifarnikHelper

class PopisStavkaAdapter(private val context: Context, private val stavke: List<PopisStavka>) :
    RecyclerView.Adapter<PopisStavkaAdapter.PopisStavkaViewHolder>() {

    inner class PopisStavkaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idArtikalTextView: TextView = itemView.findViewById(R.id.artikal_naziv)
        val kolicinaTextView: TextView = itemView.findViewById(R.id.kolicina)
        val vremeTextView: TextView = itemView.findViewById(R.id.vreme_popisivanja)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopisStavkaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_popis_stavka, parent, false)
        return PopisStavkaViewHolder(view)
    }

    override fun onBindViewHolder(holder: PopisStavkaViewHolder, position: Int) {
        val stavka = stavke[position]
        val nazivArtikla = SQLiteSifarnikHelper(context).getArtikalById(stavka.idArtikal)?.naziv

        holder.idArtikalTextView.text = nazivArtikla
        holder.kolicinaTextView.text = "Količina: " + stavka.kolicina.toString()
        holder.vremeTextView.text = formatirajDateTime(stavka.vremePopisivanja.toString()).toString()
    }

    override fun getItemCount(): Int = stavke.size
}
