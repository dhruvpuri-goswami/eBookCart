package com.example.e_commerce

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {
    lateinit var password: EditText
    lateinit var email: EditText
    lateinit var loginBtn: Button
    lateinit var sharedPreferences: SharedPreferences

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.app_bg)

        password = findViewById(R.id.password)
        email = findViewById(R.id.email)
        loginBtn = findViewById(R.id.login_btn)
        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)

        checkLogin()

        val forgetBtn = findViewById<Button>(R.id.forget_pass)
        forgetBtn.setOnClickListener {
            Toast.makeText(this@LoginActivity, "Service is not available !", Toast.LENGTH_SHORT).show()
        }

        loginBtn.setOnClickListener {
            val mobileNum = email.text.toString()
            val passwordStr = password.text.toString()

            if (mobileNum.isEmpty() || passwordStr.isEmpty()) {
                Toast.makeText(this@LoginActivity, "Please Fill Blank Details", Toast.LENGTH_SHORT).show()
            } else {
                databaseReference.child("users").addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.hasChild(mobileNum)) {
                            val getPassword = snapshot.child(mobileNum).child("Password").value.toString()
                            if (passwordStr == getPassword) {
                                sharedPreferences.edit {
                                    putString("mobile", mobileNum)
                                }
                                val i = Intent(this@LoginActivity, ProductActivity::class.java)
                                startActivity(i)
                                Toast.makeText(this@LoginActivity, "Successfully Login", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@LoginActivity, "Wrong Password", Toast.LENGTH_SHORT).show()
                            }
                        }else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Please Register Your self !",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
            }
        }
    }
    fun onRegister(view: View) {
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(intent)
    }
    fun checkLogin() {
        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
        sharedPreferences.getString("mobile", "")?.let { mobileNumber ->
            if (mobileNumber.isNotEmpty()) {
                val i = Intent(this@LoginActivity, ProductActivity::class.java)
                startActivity(i)
            }
        }
    }
}
