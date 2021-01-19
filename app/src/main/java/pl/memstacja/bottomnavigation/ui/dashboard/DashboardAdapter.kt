package pl.memstacja.bottomnavigation.ui.dashboard

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.memstacja.bottomnavigation.R
import pl.memstacja.bottomnavigation.data.model.dashboard.DegustationItem


class DashboardAdapter(var list: MutableList<DegustationItem>) : RecyclerView.Adapter<DashboardAdapter.ExampleViewHolder>() {

    class ExampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView1: TextView = itemView.findViewById(R.id.text_view_1)
        val textView2: TextView = itemView.findViewById(R.id.text_view_2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_element, parent, false)

        return ExampleViewHolder(itemView)
    }

    fun addToList(degustationItem: DegustationItem) {
        list.add(0, degustationItem)

        notifyDataSetChanged()
        notifyItemInserted(list.size - 1)
    }

    fun removeWithList(id: Int) {
        val itemList = list.find { it.id == id }
        list.remove(itemList)

        notifyDataSetChanged()
        notifyItemInserted(list.size - 1)
    }

    fun updateInList(id: Int, name: String) {
        list.filter { it.id == id }.forEach {
            it.name = name
        }
        notifyDataSetChanged()
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        val currentItem = list[position]

        holder.textView1.text = currentItem.name
        if(currentItem.description.toString().length < 21)
            holder.textView2.text = currentItem.description
        else
            holder.textView2.text = "${currentItem.description.toString().substring(20)}..."

        holder.itemView.setOnClickListener {
            val textView: TextView = it.findViewById(R.id.text_view_1);
            Log.d("APPLICATION", "Clicked element with title: " + textView.text)

            val context = holder.itemView.context
            val intent = Intent(context, PartsOpen::class.java)
            intent.putExtra("listName", currentItem.name)
            intent.putExtra("listDescription", currentItem.description)

            context.startActivity(intent)
        }
    }
}