package com.djabrj.danveer

import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class DonateFood : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donate_food)

        // Initialize UI elements
        val editFoodItem = findViewById<EditText>(R.id.editFoodItem)
        val editPeopleToFeed = findViewById<EditText>(R.id.editPeopleToFeed)
        val editDescription = findViewById<EditText>(R.id.editDescription)
        val datePicker = findViewById<DatePicker>(R.id.datePicker)
        val timePicker = findViewById<TimePicker>(R.id.timePicker)
        timePicker.setIs24HourView(true)



        // Save button
        val saveButton = findViewById<Button>(R.id.donateFoodButton)
        saveButton.setOnClickListener {
            // Retrieve user input
            val foodItem = editFoodItem.text.toString()
            val peopleToFeed = editPeopleToFeed.text.toString()
            val description = editDescription.text.toString()

            //manage dates

            val year = datePicker.year.toString()
            val month = if(datePicker.month < 9){
                "0" + (datePicker.month+1).toString()
            }else{
                (datePicker.month+1).toString()
            }
            val day = datePicker.dayOfMonth.toString()


            val hour = if(timePicker.hour < 10){
                "0" + timePicker.hour.toString()
            }else{
                timePicker.hour.toString()
            }
            val minute = if(timePicker.minute < 10){
                "0" + timePicker.minute.toString()
            }else{
                timePicker.minute.toString()
            }

            val cookedAt = "$year/$month/$day $hour:$minute"

//            println(cookedAt)

            // Handle the data as needed for insertion
            // You can pass this data to your insertion logic or Firebase Firestore, for example
            // Example: Call a function to insert the data

            //Check if all inputs are provided
            if(foodItem.isEmpty() || peopleToFeed.isEmpty() || description.isEmpty()){
                Toast.makeText(
                    this,
                    "Please provide all information.",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                insertDataToDatabase(foodItem, peopleToFeed, description, cookedAt)

            }
        }
    }



//    private fun clearInputFields() {
//        editFoodItem.text.clear()
//        editPeopleToFeed.text.clear()
//        editDescription.text.clear()
//        editCookedAt.text.clear()
//    }

    private fun insertDataToDatabase(
        foodItem: String,
        peopleToFeed: String,
        description: String,
        cookedAt: String
    ) {

        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val email = firebaseAuth.currentUser?.email.toString()

        val db = FirebaseFirestore.getInstance()
        val collectionName = "donations" // Replace with your Firestore collection name

        val data = hashMapOf(
            "foodItem" to foodItem,
            "peopleToFeed" to peopleToFeed,
            "description" to description,
            "cookedAt" to cookedAt,
            "offereBy" to email,
            "takenBy" to null,
            "status" to "placed"
        )

        db.collection(collectionName)
            .add(data)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Donation added.",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    Toast.makeText(
                        this,
                        "An error occured.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

}
