package com.djabrj.danveer

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DonorProfile : FragmentActivity(){

    private lateinit var firebaseAuth: FirebaseAuth

    fun updateData(donorName: String, phoneNumber: String, pinCode: String, address: String) {
        firebaseAuth = FirebaseAuth.getInstance()
        val email = firebaseAuth.currentUser?.email.toString()
        val db = FirebaseFirestore.getInstance()
        val donorProfileRef = db.collection("users")

        // Query the collection to find the document with the matching email
        val query = donorProfileRef.whereEqualTo("email", email)

        query.get()
            .addOnSuccessListener { querySnapshot ->
                // Check if a document with the email was found
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0] // Assuming there's only one matching document
                    val documentId = document.id

                    // Create a data map with the updated values
                    val updatedData = hashMapOf(
                        "donorName" to donorName,
                        "phoneNumber" to phoneNumber,
                        "address" to address,
                        "pinCode" to pinCode
                    )

                    // Update the document in Firestore
                    donorProfileRef.document(documentId)
                        .update(updatedData as Map<String, Any>)
                        .addOnSuccessListener {
                            // Update was successful
                            // Handle success, if needed
                            Toast.makeText(
                                this,
                                "Profile Updated.",
                                Toast.LENGTH_SHORT
                            ).show()
                            println("uppdated")
                        }
                        .addOnFailureListener { e ->
                            // Update failed
                            // Handle the failure and show an error message or log the error, if needed
                            println("error")
                            Toast.makeText(
                                this,
                                "An Error Occured.",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                } else {
                    // No document with the email was found
                    // Handle this case, e.g., display an error message
                    println("email not found")

                }
            }
            .addOnFailureListener { e ->
                // Query failed
                // Handle the query failure, e.g., show an error message or log the error
                println("Query failed")

            }
    }

    private fun fetchAndPopulateDonorProfile() {
        firebaseAuth = FirebaseAuth.getInstance()
        val email = firebaseAuth.currentUser?.email.toString()
        val db = FirebaseFirestore.getInstance()
        val donorProfileRef = db.collection("users")

        val editDonorName = findViewById<EditText>(R.id.editDonorName)
        val editPhoneNumber = findViewById<EditText>(R.id.editPhoneNumber)
        val editAddress = findViewById<EditText>(R.id.editAddress)
        val editPincode = findViewById<EditText>(R.id.editPincode)

        // Query the collection to find the document with the matching email
        val query = donorProfileRef.whereEqualTo("email", email)

        query.get()
            .addOnSuccessListener { querySnapshot ->
                // Check if a document with the email was found
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0] // Assuming there's only one matching document
                    val donorName = document.getString("donorName") ?: ""
                    val phoneNumber = document.getString("phoneNumber") ?: ""
                    val address = document.getString("address") ?: ""
                    val pinCode = document.getString("pinCode") ?: ""

                    // Populate the EditText fields with the fetched data
                    editDonorName.setText(donorName)
                    editPhoneNumber.setText(phoneNumber)
                    editAddress.setText(address)
                    editPincode.setText(pinCode)
                } else {
                    // No document with the email was found
                    // Handle this case, e.g., display an error message
                    println("Email not found")
                }
            }
            .addOnFailureListener { e ->
                // Query failed
                // Handle the query failure, e.g., show an error message or log the error
                println("Query failed")
            }
    }


    fun isPhoneNumberValid(phoneNumber: String): Boolean {
        return phoneNumber.matches(Regex("^\\d{10,}\$"))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donor_profile)

        fetchAndPopulateDonorProfile()

        val editDonorName = findViewById<EditText>(R.id.editDonorName)
        val editPhoneNumber = findViewById<EditText>(R.id.editPhoneNumber)
        val editAddress = findViewById<EditText>(R.id.editAddress)
        val editPincode = findViewById<EditText>(R.id.editPincode)
        val saveButton = findViewById<Button>(R.id.saveButton)

        saveButton.setOnClickListener {
            val donorName = editDonorName.text.toString()
            val phoneNumber = editPhoneNumber.text.toString()
            val address = editAddress.text.toString()
            val pinCode = editPincode.text.toString()
            if(isPhoneNumberValid(phoneNumber)){
                //phone is valid
                updateData(donorName, phoneNumber, pinCode, address)

            }else{
                //phone not valid
                Toast.makeText(
                    this,
                    "Invalid Phone number.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            // Save the donor's information and selected location to your data store
            // You can use a database or SharedPreferences for this purpose
        }

    }

}
