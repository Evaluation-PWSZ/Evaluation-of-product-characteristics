package pl.memstacja.bottomnavigation.ui.dashboard

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.memstacja.bottomnavigation.R
import pl.memstacja.bottomnavigation.data.model.dashboard.DegustationItem
import pl.memstacja.bottomnavigation.data.model.dashboard.FeatureItem


class FeaturesAdapter(private val degustationList: List<FeatureItem>) : RecyclerView.Adapter<FeaturesAdapter.FeaturesViewHolder>() {

    class FeaturesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val feature: TextView = itemView.findViewById(R.id.feature)
        val seekBar: SeekBar = itemView.findViewById(R.id.seekBar)
        val textStar: TextView = itemView.findViewById(R.id.stars)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturesViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.features_item, parent, false)

        return FeaturesViewHolder(itemView)
    }

    override fun getItemCount() = degustationList.size

    override fun onBindViewHolder(holder: FeaturesViewHolder, position: Int) {
        val currentItem = degustationList[position]

        holder.feature.text = currentItem.name

        holder.seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                holder.textStar.text = "$progress ⭐"
                Log.d("progress", "ok, progress $progress")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //TODO("Not yet implemented")
            }

        })
    }
}