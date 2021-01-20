package pl.memstacja.bottomnavigation.ui.dashboard.parts

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.memstacja.bottomnavigation.R
import pl.memstacja.bottomnavigation.data.model.dashboard.ProductItem
import pl.memstacja.bottomnavigation.ui.dashboard.FeaturesOpen


class PartsAdapter(private val productList: MutableList<ProductItem>) : RecyclerView.Adapter<PartsAdapter.FeaturesViewHolder>() {

    class FeaturesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView1: TextView = itemView.findViewById(R.id.text_view_1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturesViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.parts_item, parent, false)

        return FeaturesViewHolder(itemView)
    }

    fun addToList(degustationItem: ProductItem) {
        productList.add(0, degustationItem)

        notifyDataSetChanged()
        notifyItemInserted(productList.size - 1)
    }

    fun addToListDesc(degustationItem: ProductItem) {
        productList.add(degustationItem)

        notifyDataSetChanged()
        notifyItemInserted(productList.size - 1)
    }

    fun removeWithList(id: Int) {
        val itemList = productList.find { it.id == id }
        productList.remove(itemList)

        notifyDataSetChanged()
        notifyItemInserted(productList.size - 1)
    }

    fun updateInList(id: Int, name: String, description: String) {
        productList.filter { it.id == id }.forEach {
            it.name = name
            it.description = description
        }
        notifyDataSetChanged()
        notifyItemInserted(productList.size - 1)
    }

    fun resetAdapter() {
        productList.clear()
    }

    override fun getItemCount() = productList.size

    override fun onBindViewHolder(holder: FeaturesViewHolder, position: Int) {
        val currentItem = productList[position]

        holder.textView1.text = currentItem.name

        holder.itemView.setOnClickListener {
            val textView: TextView = it.findViewById(R.id.text_view_1);
            Log.d("APPLICATION", "Clicked element with title: " + textView.text)

            val context = holder.itemView.context
            val intent = Intent(context, FeaturesOpen::class.java)
            intent.putExtra("productName", currentItem.name)
            //intent.putExtra("listDescription", currentItem.text2)

            context.startActivity(intent)
        }
    }
}