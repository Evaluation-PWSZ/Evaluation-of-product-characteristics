package pl.memstacja.bottomnavigation.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.memstacja.bottomnavigation.R

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val recyclerView: RecyclerView = root.findViewById<RecyclerView>(R.id.recycle_view)

        setToList(recyclerView);

        return root
    }

    private fun generateList(): List<ExampleItem> {
        val list = ArrayList<ExampleItem>()

        for (i in 1..20){
            val item = ExampleItem(
                "Lista ${i.toString()}",
                "Opis listy: ${i.toString()}"
            )

            list += item
        }

        return list
    }

    private fun setToList(recyclerView: RecyclerView) {

        val generatedList: List<ExampleItem> = generateList()
        recyclerView.adapter = ExampleAdapter(generatedList.reversed())
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
    }
}