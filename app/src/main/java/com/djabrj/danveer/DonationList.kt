package com.djabrj.danveer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

data class CardData(
    val foodItem: String,
    val peopleToFeed: String,
    val description: String,
    val cookedAt: String,
    val offereBy: String,
    val takenBy: String,
    val status: String,
    val id : String
)
class CardAdapter(private val cardList: List<CardData>) :
    RecyclerView.Adapter<CardAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodItemTextView: TextView = itemView.findViewById(R.id.foodItemTextView)
        val peopleToFeedTextView: TextView = itemView.findViewById(R.id.peopleToFeedTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val cookedAtTextView: TextView = itemView.findViewById(R.id.cookedAtTextView)
//        val offerByTextView: TextView = itemView.findViewById(R.id.offerByTextView)
        val takenByTextView: TextView = itemView.findViewById(R.id.takenByTextView)
        val statusTextView: TextView = itemView.findViewById(R.id.statusTextView)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.donation_card_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = cardList[position]
        holder.foodItemTextView.text = currentItem.foodItem
        holder.peopleToFeedTextView.text = currentItem.peopleToFeed
        holder.descriptionTextView.text = currentItem.description
        holder.cookedAtTextView.text = currentItem.cookedAt
//        holder.offerByTextView.text = currentItem.offereBy
        holder.takenByTextView.text = currentItem.takenBy
        holder.statusTextView.text = currentItem.status

        if(currentItem.status != "placed"){
            holder.deleteButton.visibility = View.INVISIBLE
        }

        holder.deleteButton.setOnClickListener{
            val context : Context = it.context
            println(currentItem.id)
            val db = FirebaseFirestore.getInstance()
            val documentId = currentItem.id // Replace with the actual ID of the document you want to delete
            val documentReference = db.collection("donations").document(documentId)
            documentReference
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(
                        context,
                        "Deleted successfully.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        context,
                        "An error occured.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    override fun getItemCount() = cardList.size
}


class DonationList : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var cardAdapter: CardAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_list)

        val swipeRefreshLayout : SwipeRefreshLayout = findViewById(R.id.swiperefresh)

        swipeRefreshLayout.setOnRefreshListener {
            // Perform data refresh operations here
            retrieveCardData()
            println("Data refreshed")
            swipeRefreshLayout.isRefreshing = false
        }

        retrieveCardData()

    }

    private fun retrieveCardData() {
        // Simulate data or fetch from Firestore
        val db = FirebaseFirestore.getInstance()
        val cardCollection = db.collection("donations")
        val cardDataList = mutableListOf<CardData>()
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val email = firebaseAuth.currentUser?.email.toString()

        cardCollection
            .whereEqualTo("offereBy", email)
            .orderBy("cookedAt",Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                for (document in querySnapshot) {
                    val card = CardData(
                        document.data["foodItem"].toString() ?: "",
                        document.data["peopleToFeed"].toString() ?: "",
                        document.data["description"].toString() ?: "",
                        document.data["cookedAt"].toString() ?: "",
                        document.data["offereBy"].toString() ?: "",
                        document.data["takenBy"].toString() ?: "",
                        document.data["status"].toString() ?: "",
                        document.id.toString() ?: ""
                    )
                    cardDataList.add(card)
                }

                // Initialize the RecyclerView
                recyclerView = findViewById(R.id.recyclerView)
                recyclerView.layoutManager = LinearLayoutManager(this)

                // Retrieve card data (you can customize this)


                // Set up the CardAdapter
                cardAdapter = CardAdapter(cardDataList)
                recyclerView.adapter = cardAdapter

            }
            .addOnFailureListener { e ->
                println("failed")
            }


    }

}