package adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.popisosnovnihsredstava.R
import com.example.popisosnovnihsredstava.entities.PopisStavka

class PopisStavkaAdapter(private val stavke: List<PopisStavka>) :
    RecyclerView.Adapter<PopisStavkaAdapter.PopisStavkaViewHolder>() {

    inner class PopisStavkaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idArtikalTextView: TextView = itemView.findViewById(R.id.id_artikal)
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
        holder.idArtikalTextView.text = stavka.idArtikal.toString()
        holder.kolicinaTextView.text = stavka.kolicina.toString()
        holder.vremeTextView.text = stavka.vremePopisivanja.toString() // Format date/time if needed
    }

    override fun getItemCount(): Int = stavke.size
}
