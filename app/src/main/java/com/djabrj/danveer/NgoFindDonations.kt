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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

data class NgoCardData(
    val id:String,
    val foodItem: String,
    val peopleToFeed: String,
    val description: String,
    val cookedAt: String,
    val offereBy: String,
    val takenBy: String,
    val status: String,

    //about reastorant
    var donorName: String,
    var phoneNumber: String,
    var address: String,
    var pinCode: String

)
class NgoCardAdapter(private val cardList: List<NgoCardData>) :
    RecyclerView.Adapter<NgoCardAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodItemTextView: TextView = itemView.findViewById(R.id.foodItemTextView)
        val peopleToFeedTextView: TextView = itemView.findViewById(R.id.peopleToFeedTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val cookedAtTextView: TextView = itemView.findViewById(R.id.cookedAtTextView)
        val offerByTextView: TextView = itemView.findViewById(R.id.offerByTextView)
        val contactTextView: TextView = itemView.findViewById(R.id.contactTextView)
        val addressTextView: TextView = itemView.findViewById(R.id.addressTextView)
        val bookButton: Button = itemView.findViewById(R.id.bookButton)
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
        holder.offerByTextView.text = currentItem.donorName
        holder.contactTextView.text = currentItem.phoneNumber
        holder.addressTextView.text = currentItem.address
        holder.bookButton.setOnClickListener {
            bookDonation(currentItem.id, it.context)
        }
//        holder.takenByTextView.text = currentItem.takenBy
//        holder.statusTextView.text = currentItem.status
    }

    override fun getItemCount() = cardList.size

    private fun bookDonation(id : String, context: Context){

        val firebaseAuth = FirebaseAuth.getInstance()
        val email = firebaseAuth.currentUser?.email.toString()
        val db = FirebaseFirestore.getInstance()
        val donationProfileRef = db.collection("donations")

        donationProfileRef.document(id)
            .get()
            .addOnSuccessListener {document ->
                // Check if a document with the email was found
                if(document.data?.get("status").toString() == "placed"){
                    //now modify details
                    val updatedData = hashMapOf(
                        "status" to "booked",
                        "takenBy" to email
                    )
                    donationProfileRef.document(id)
                        .update(updatedData as Map<String, Any>)
                        .addOnSuccessListener {
                            // Update was successful
                            // Handle success, if needed
                            Toast.makeText(
                                context,
                                "Booked the Donation.",
                                Toast.LENGTH_SHORT
                            ).show()
                            println("uppdated")
                        }
                        .addOnFailureListener { e ->
                            // Update failed
                            // Handle the failure and show an error message or log the error, if needed
                            println("error")
                            Toast.makeText(
                                context,
                                "An Error Occured.",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                }

                else{
                    Toast.makeText(
                        context,
                        "Sorry the donation is already booked.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
    }


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
                        document.id.toString() ?: "",
                        document.data["foodItem"].toString() ?: "",
                        document.data["peopleToFeed"].toString() ?: "",
                        document.data["description"].toString() ?: "",
                        document.data["cookedAt"].toString() ?: "",
                        document.data["offereBy"].toString() ?: "",
                        document.data["takenBy"].toString() ?: "",
                        document.data["status"].toString() ?: "",
                        "",
                        "",
                        "",
                        ""
                    )
                    cardDataList.add(card)
                }

                // Initialize the RecyclerView
                recyclerView = findViewById(R.id.recyclerView)
                recyclerView.layoutManager = LinearLayoutManager(this)


                // Set up the CardAdapter
                cardAdapter = NgoCardAdapter(cardDataList)
                recyclerView.adapter = cardAdapter

                //Update data of each Restorant


                for(i in  0..cardDataList.size-1){
                    println(cardDataList[i].offereBy)
                    val usersCollection = db.collection("users")
                    usersCollection.whereEqualTo("email", cardDataList[i].offereBy)
                        .get()
                        .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                            for (document in querySnapshot) {
                                cardDataList[i].donorName = document.data["donorName"].toString() ?: ""
                                cardDataList[i].phoneNumber = document.data["phoneNumber"].toString() ?: ""
                                cardDataList[i].address = document.data["address"].toString() ?: ""

                                //update UI
                                cardAdapter.notifyItemChanged(i)
                                println(cardDataList[i].id)
                            }
                        }
                        .addOnFailureListener { e ->
                            println("failed")
                        }

                }

//
//                cardAdapter.notifyDataSetChanged()


            }
            .addOnFailureListener { e ->
                println("failed")
            }


    }

}