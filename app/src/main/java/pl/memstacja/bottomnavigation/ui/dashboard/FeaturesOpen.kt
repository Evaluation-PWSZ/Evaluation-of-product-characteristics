package pl.memstacja.bottomnavigation.ui.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.memstacja.bottomnavigation.R

class FeaturesOpen : AppCompatActivity() {
    var name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        title = "Ocena cech produktu"
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_features)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        name = intent.getStringExtra("productName")

        findViewById<TextView>(R.id.productName).text = name

        Log.d("ACTIVE_APP", "$name")

        val featuresList = findViewById<RecyclerView>(R.id.viewGroup)

        setToList(featuresList)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun generateList(): List<ExampleItem> {
        val list = ArrayList<ExampleItem>()

        for (i in 1..3){
            val item = ExampleItem(
                "Cecha ${i.toString()} produktu $name",
                ""
            )

            list += item
        }

        return list
    }

    private fun setToList(recyclerView: RecyclerView) {

        val generatedList: List<ExampleItem> = generateList()
        recyclerView.adapter = FeaturesAdapter(generatedList.reversed())
        recyclerView.layoutManager = LinearLayoutManager(this@FeaturesOpen)
        recyclerView.setHasFixedSize(true)
    }
}