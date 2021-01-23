package pl.memstacja.bottomnavigation.ui.dashboard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pwszproducts.myapplication.data.model.StaticUserData
import pl.memstacja.bottomnavigation.R
import pl.memstacja.bottomnavigation.data.model.dashboard.DegustationItem
import pl.memstacja.bottomnavigation.ui.Updators.DegustationUpdateActivity
import pl.memstacja.bottomnavigation.ui.dashboard.parts.PartsOpen


class DashboardAdapter(private val list: MutableList<DegustationItem>)
    : RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder>() {

    lateinit var context: Context
    lateinit var activity: Activity
    private val UPDATE = 2

    class DashboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val description: TextView = itemView.findViewById(R.id.description)
        val buttonViewOption: TextView = itemView.findViewById(R.id.more_options)
        //val editButton: Button = itemView.findViewById(R.id.edit_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.degustation_item, parent, false)

        return DashboardViewHolder(itemView)
    }

    fun addToList(degustationItem: DegustationItem) {
        list.add(0, degustationItem)

        notifyDataSetChanged()
        notifyItemInserted(list.size - 1)
    }

    fun addToListDesc(degustationItem: DegustationItem) {
        list.add(degustationItem)

        notifyDataSetChanged()
        notifyItemInserted(list.size - 1)
    }

    fun removeWithList(id: Int) {
        val itemList = list.find { it.id == id }
        list.remove(itemList)

        notifyDataSetChanged()
        notifyItemInserted(list.size - 1)
    }

    fun updateInList(degustationItem: DegustationItem) {
        list.filter { it.id == degustationItem.id }.forEach {
            it.name = degustationItem.name
            it.description = degustationItem.description
        }
        notifyDataSetChanged()
        notifyItemInserted(list.size - 1)
    }

    fun resetAdapter() {
        list.clear()
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val currentItem = list[position]

        holder.buttonViewOption.setOnClickListener {
            val popup = PopupMenu(holder.itemView.context, holder.buttonViewOption)
            popup.inflate(R.menu.degustation_item_menu)

            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
                when(item!!.itemId) {
                    R.id.edit_menu -> {
                        val intent = Intent(context, DegustationUpdateActivity::class.java)
                        intent.putExtra("id", currentItem.id)
                        intent.putExtra("name", currentItem.name)
                        intent.putExtra("description", currentItem.description)
                        activity.startActivityForResult(intent, UPDATE)
                    }
                    R.id.remove_menu -> {
                        removeWithList(currentItem.id)
                    }
                }
                true
            })

            popup.show()
        }

        holder.name.text = currentItem.name
        if(currentItem.description != null) {
            if (currentItem.description.toString().length < 41)
                holder.description.text = currentItem.description
            else
                holder.description.text = "${currentItem.description.toString().substring(0, 40)}..."
        }

        /*if(currentItem.owner_id != StaticUserData.user.id) {
            holder.editButton.isEnabled = false
        }*/

        holder.itemView.setOnClickListener {
            val textView: TextView = holder.name
            Log.d("APPLICATION", "Clicked element with title: " + textView.text)

            val context = holder.itemView.context
            val intent = Intent(context, PartsOpen::class.java)
            intent.putExtra("id", currentItem.id)
            intent.putExtra("listName", currentItem.name)
            intent.putExtra("listDescription", currentItem.description)

            context.startActivity(intent)
        }
    }
}
