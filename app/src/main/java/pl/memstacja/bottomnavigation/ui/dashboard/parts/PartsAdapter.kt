package pl.memstacja.bottomnavigation.ui.dashboard.parts

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.memstacja.bottomnavigation.R
import pl.memstacja.bottomnavigation.data.model.dashboard.ProductItem
import pl.memstacja.bottomnavigation.ui.Updators.ProductUpdateActivity
import pl.memstacja.bottomnavigation.ui.dashboard.FeaturesOpen


class PartsAdapter(var productList: MutableList<ProductItem>) : RecyclerView.Adapter<PartsAdapter.FeaturesViewHolder>() {

    class FeaturesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val buttonEdit: TextView = itemView.findViewById(R.id.update_product)
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

    fun updateInList(id: Int, name: String) {
        Log.d("LIST_UPDATE", "Update id: $id with text: $name")
        /*productList.filter { it.id == id }.forEach {
            it.name = name
        }*/
        productList.find { it.id == id }?.name = name
        notifyDataSetChanged()
        notifyItemInserted(productList.size - 1)
    }

    override fun getItemCount() = productList.size

    override fun onBindViewHolder(holder: FeaturesViewHolder, position: Int) {
        val currentItem = productList[position]

        holder.name.text = currentItem.name

        holder.itemView.setOnClickListener {
            Log.d("APPLICATION", "Clicked element with title: " + holder.name)

            val context = holder.itemView.context
            val intent = Intent(context, FeaturesOpen::class.java)
            intent.putExtra("id", currentItem.id)
            intent.putExtra("productName", currentItem.name)

            context.startActivity(intent)
        }

        holder.buttonEdit.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ProductUpdateActivity::class.java)
            Log.d("DEGUSTATION", "${currentItem.degustation_id}")
            intent.putExtra("degustation_id", currentItem.degustation_id)
            intent.putExtra("id", currentItem.id)
            intent.putExtra("name", currentItem.name)

            (context as Activity).startActivityForResult(intent, 2)
        }

    }
}