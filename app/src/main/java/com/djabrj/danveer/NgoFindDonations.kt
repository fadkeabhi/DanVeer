package com.djabrj.danveer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

data class NgoCardData(
    val foodItem: String,
    val peopleToFeed: String,
    val description: String,
    val cookedAt: String,
    val offereBy: String,
    val takenBy: String,
    val status: String

)
class NgoCardAdapter(private val cardList: List<NgoCardData>) :
    RecyclerView.Adapter<NgoCardAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodItemTextView: TextView = itemView.findViewById(R.id.foodItemTextView)
        val peopleToFeedTextView: TextView = itemView.findViewById(R.id.peopleToFeedTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val cookedAtTextView: TextView = itemView.findViewById(R.id.cookedAtTextView)
        val offerByTextView: TextView = itemView.findViewById(R.id.offerByTextView)
//        val takenByTextView: TextView = itemView.findViewById(R.id.takenByTextView)
//        val statusTextView: TextView = itemView.findViewById(R.id.statusTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.donation_card_item_ngo, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = cardList[position]
        holder.foodItemTextView.text = currentItem.foodItem
        holder.peopleToFeedTextView.text = currentItem.peopleToFeed
        holder.descriptionTextView.text = currentItem.description
        holder.cookedAtTextView.text = currentItem.cookedAt
        holder.offerByTextView.text = currentItem.offereBy
//        holder.takenByTextView.text = currentItem.takenBy
//        holder.statusTextView.text = currentItem.status
    }

    override fun getItemCount() = cardList.size
}


class NgoFindDonations : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var cardAdapter: NgoCardAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ngo_find_donations)


        retrieveCardData()

    }

    private fun retrieveCardData() {
        // Simulate data or fetch from Firestore
        val db = FirebaseFirestore.getInstance()
        val cardCollection = db.collection("donations")
        val cardDataList = mutableListOf<NgoCardData>()
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val email = firebaseAuth.currentUser?.email.toString()

        cardCollection.whereEqualTo("status", "placed")
            .get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                for (document in querySnapshot) {

                    val card = NgoCardData(
                        document.data["foodItem"].toString() ?: "",
                        document.data["peopleToFeed"].toString() ?: "",
                        document.data["description"].toString() ?: "",
                        document.data["cookedAt"].toString() ?: "",
                        document.data["offereBy"].toString() ?: "",
                        document.data["takenBy"].toString() ?: "",
                        document.data["status"].toString() ?: ""
                    )
                    cardDataList.add(card)
                }

                // Initialize the RecyclerView
                recyclerView = findViewById(R.id.recyclerView)
                recyclerView.layoutManager = LinearLayoutManager(this)


                // Set up the CardAdapter
                cardAdapter = NgoCardAdapter(cardDataList)
                recyclerView.adapter = cardAdapter



            }
            .addOnFailureListener { e ->
                println("failed")
            }


    }

}