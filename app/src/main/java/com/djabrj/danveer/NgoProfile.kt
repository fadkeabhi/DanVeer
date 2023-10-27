package com.djabrj.danveer

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NgoProfile : FragmentActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    private fun updateData(ngoName: String, phoneNumber: String, address: String) {
        firebaseAuth = FirebaseAuth.getInstance()
        val email = firebaseAuth.currentUser?.email.toString()
        val db = FirebaseFirestore.getInstance()
        val ngoProfileRef = db.collection("users")

        // Query the collection to find the document with the matching email
        val query = ngoProfileRef.whereEqualTo("email", email)

        query.get()
            .addOnSuccessListener { querySnapshot ->
                // Check if a document with the email was found
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0] // Assuming there's only one matching document
                    val documentId = document.id

                    // Create a data map with the updated values
                    val updatedData = hashMapOf(
                        "ngoName" to ngoName,
                        "phoneNumber" to phoneNumber,
                        "address" to address
                    )

                    // Update the document in Firestore
                    ngoProfileRef.document(documentId)
                        .update(updatedData as Map<String, Any>)
                        .addOnSuccessListener {
                            // Update was successful
                            // Handle success, if needed
                            Toast.makeText(
                                this,
                                "Profile Updated.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener { e ->
                            // Update failed
                            // Handle the failure and show an error message or log the error, if needed
                            Toast.makeText(
                                this,
                                "An Error Occurred.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    // No document with the email was found
                    // Handle this case, e.g., display an error message
                    Toast.makeText(
                        this,
                        "Email not found.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { e ->
                // Query failed
                // Handle the query failure, e.g., show an error message or log the error
                Toast.makeText(
                    this,
                    "Query failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    fun isPhoneNumberValid(phoneNumber: String): Boolean {
        return phoneNumber.matches(Regex("^\\d{10,}\$"))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ngo_profile)

        fetchAndPopulateNgoProfile()

        val editNGOName = findViewById<EditText>(R.id.editNGOName)
        val editPhoneNumber = findViewById<EditText>(R.id.editPhoneNumber)
        val editAddress = findViewById<EditText>(R.id.editAddress)
        val saveButton = findViewById<Button>(R.id.saveButton)

        saveButton.setOnClickListener {
            val ngoName = editNGOName.text.toString()
            val phoneNumber = editPhoneNumber.text.toString()
            val address = editAddress.text.toString()
            if (isPhoneNumberValid(phoneNumber)) {
                // Phone is valid
                updateData(ngoName, phoneNumber, address)
            } else {
                // Phone is not valid
                Toast.makeText(
                    this,
                    "Invalid Phone number.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun fetchAndPopulateNgoProfile() {
        firebaseAuth = FirebaseAuth.getInstance()
        val email = firebaseAuth.currentUser?.email.toString()
        val db = FirebaseFirestore.getInstance()
        val ngoProfileRef = db.collection("users")

        val editNGOName = findViewById<EditText>(R.id.editNGOName)
        val editPhoneNumber = findViewById<EditText>(R.id.editPhoneNumber)
        val editAddress = findViewById<EditText>(R.id.editAddress)

        // Query the collection to find the document with the matching email
        val query = ngoProfileRef.whereEqualTo("email", email)

        query.get()
            .addOnSuccessListener { querySnapshot ->
                // Check if a document with the email was found
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0] // Assuming there's only one matching document
                    val ngoName = document.getString("ngoName") ?: ""
                    val phoneNumber = document.getString("phoneNumber") ?: ""
                    val address = document.getString("address") ?: ""

                    // Populate the EditText fields with the fetched data
                    editNGOName.setText(ngoName)
                    editPhoneNumber.setText(phoneNumber)
                    editAddress.setText(address)
                } else {
                    // No document with the email was found
                    // Handle this case, e.g., display an error message
                    Toast.makeText(
                        this,
                        "Email not found",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { e ->
                // Query failed
                // Handle the query failure, e.g., show an error message or log the error
                Toast.makeText(
                    this,
                    "Query failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}
