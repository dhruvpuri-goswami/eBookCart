package com.example.e_commerce

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import android.widget.EditText
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class CheckoutActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var creditCardEditText: EditText
    private lateinit var expiryDateEditText: EditText
    private lateinit var cvvEditText: EditText
    private lateinit var checkOutButton: Button
    private lateinit var subtotalTextView: TextView
    private lateinit var shippingTextView: TextView
    private lateinit var totalTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.app_bg)
        val price = intent.getDoubleExtra("price", 0.0)

        nameEditText = findViewById(R.id.name)
        creditCardEditText = findViewById(R.id.credit_card)
        expiryDateEditText = findViewById(R.id.expiry_date)
        cvvEditText = findViewById(R.id.cvv)
        checkOutButton = findViewById(R.id.check_out_button)
        subtotalTextView = findViewById(R.id.subtotal)
        shippingTextView = findViewById(R.id.shipping)
        totalTextView = findViewById(R.id.total)

        val subtotal = price
        val shipping = 10.0
        val total = subtotal + shipping

        subtotalTextView.text = String.format("$%.2f", subtotal)
        shippingTextView.text = String.format("$%.2f", shipping)
        totalTextView.text = String.format("$%.2f", total)

        checkOutButton.setOnClickListener {
            if (validateInput()) {
                Toast.makeText(this, "Payment successful!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, ProductActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please fill in all the fields.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInput(): Boolean {
        return nameEditText.text.isNotEmpty() &&
                creditCardEditText.text.isNotEmpty() &&
                expiryDateEditText.text.isNotEmpty() &&
                cvvEditText.text.isNotEmpty()
    }
}
