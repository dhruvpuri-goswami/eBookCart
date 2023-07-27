package com.example.e_commerce

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.database.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var fullNameEditText: EditText
    private lateinit var phoneNumEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var login: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.app_bg)

        fullNameEditText = findViewById(R.id.full_name)
        emailEditText = findViewById(R.id.email)
        phoneNumEditText = findViewById(R.id.phone_num)
        passwordEditText = findViewById(R.id.password)
        val registerButton: Button = findViewById(R.id.continue_btn)
        login = findViewById(R.id.login)

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
        editor = sharedPreferences.edit()

        databaseReference = FirebaseDatabase.getInstance().reference


        registerButton.setOnClickListener {
            val emailText = emailEditText.text.toString()
            val fullName = fullNameEditText.text.toString()
            val mobileNum = phoneNumEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (fullName.isEmpty() || emailText.isEmpty() || mobileNum.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please Fill All Blank Details", Toast.LENGTH_SHORT).show()
            } else {
                databaseReference.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.hasChild(mobileNum)) {
                            Toast.makeText(this@RegisterActivity, "Phone is already Registered", Toast.LENGTH_SHORT).show()
                        } else {
                            editor.putString("mobile", mobileNum)
                            editor.apply()
                            databaseReference.child("users").child(mobileNum).child("Fullname").setValue(fullName)
                            databaseReference.child("users").child(mobileNum).child("Email").setValue(emailText)
                            databaseReference.child("users").child(mobileNum).child("Password").setValue(password)
                            Toast.makeText(this@RegisterActivity, "Users Registered", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle potential errors.
                    }
                })
            }
        }
    }

    fun onLogin(view: View) {
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
    }
}
