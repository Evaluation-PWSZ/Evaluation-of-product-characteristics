package pl.memstacja.bottomnavigation.ui.dashboard

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.memstacja.bottomnavigation.R


class ExampleAdapter(private val exampleList: List<ExampleItem>) : RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder>() {

    class ExampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView1: TextView = itemView.findViewById(R.id.text_view_1)
        val textView2: TextView = itemView.findViewById(R.id.text_view_2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_element, parent, false)

        return ExampleViewHolder(itemView)
    }

    override fun getItemCount() = exampleList.size

    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        val currentItem = exampleList[position]

        holder.textView1.text = currentItem.text1
        holder.textView2.text = currentItem.text2

        holder.itemView.setOnClickListener {
            val textView: TextView = it.findViewById(R.id.text_view_1);
            Log.d("APPLICATION", "Clicked element with title: " + textView.text)

            val context = holder.itemView.context
            val intent = Intent(context, PartsOpen::class.java)
            intent.putExtra("listName", currentItem.text1)
            intent.putExtra("listDescription", currentItem.text2)

            context.startActivity(intent)
        }
    }
}