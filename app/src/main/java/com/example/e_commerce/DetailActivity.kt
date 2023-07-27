package com.example.e_commerce

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

data class BookDetails(
    val edition: String = "",
    val author: String = "",
    val rating: Double = 0.0,
    val total_ratings: Int = 0,
    val returns_policy: String = "",
    val secure_transaction: String = "",
    val description: String = "",
    val reading_age: String = "",
    val print_length: Int = 0,
    val language: String = "",
    val dimensions: String = "",
    val publisher: String = "",
    val image: String = "",
    val price: Double = 0.0  // Added price field
)

class DetailActivity : AppCompatActivity() {

    private lateinit var bookImage: ImageView
    private lateinit var bookName: TextView
    private lateinit var bookAuthor: TextView
    private lateinit var bookEdition: TextView
    private lateinit var bookRating: TextView
    private lateinit var bookTotalRatings: TextView
    private lateinit var bookDescription: TextView
    private lateinit var buyNowButton: Button
    private lateinit var bookPrice: TextView
    private lateinit var bookReturn: TextView
    private lateinit var bookAge: TextView
    private lateinit var bookLanguage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.app_bg)

        bookImage = findViewById(R.id.detail_book_img)
        bookName = findViewById(R.id.detail_book_name)
        bookAuthor = findViewById(R.id.detail_book_author)
        bookEdition = findViewById(R.id.detail_book_edition)
        bookRating = findViewById(R.id.detail_book_rating)
        bookTotalRatings = findViewById(R.id.detail_book_total_ratings)
        bookDescription = findViewById(R.id.detail_book_description)
        buyNowButton = findViewById(R.id.buy_now_button)
        bookPrice = findViewById(R.id.detail_book_price)
        bookReturn = findViewById(R.id.detail_book_return)
        bookAge = findViewById(R.id.detail_book_reading_age)
        bookLanguage = findViewById(R.id.detail_book_language)

        val bookId = intent.getIntExtra("bookId", 0)

        val database = FirebaseDatabase.getInstance().getReference("books")

        database.child(bookId.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val bookDetails = dataSnapshot.child("details").getValue(BookDetails::class.java)
                    val title = dataSnapshot.child("title").getValue(String::class.java)

                    if (bookDetails != null && title != null) {
                        updateUI(title, bookDetails)
                    } else {
                        showErrorAndFinish()
                    }
                } else {
                    showErrorAndFinish()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                showErrorAndFinish()
            }
        })
    }

    private fun updateUI(title: String, book: BookDetails) {
        Picasso.get().load(book.image).into(bookImage)
//        Picasso.get().load("https://m.media-amazon.com/images/I/51KFyfyK7eL._SY264_BO1,204,203,200_QL40_FMwebp_.jpg").into(bookImage)
        bookName.text = title
        bookAuthor.text = "By " + book.author
        bookEdition.text = book.edition
        bookRating.text = "${book.rating} out of 5"
        bookTotalRatings.text = book.total_ratings.toString()
        bookDescription.text = book.description
        bookLanguage.text = book.language
        bookAge.text = book.reading_age
        bookReturn.text = book.returns_policy
        bookPrice.text = "$ "+ book.price.toString()

        buyNowButton.setOnClickListener {
            val intent = Intent(this, CheckoutActivity::class.java)
            intent.putExtra("price", book.price)
            startActivity(intent)
        }
    }

    private fun showErrorAndFinish() {
        Toast.makeText(this, "Error loading data. Please try again later.", Toast.LENGTH_SHORT).show()
        finish()
    }
}
