package com.example.e_commerce

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

data class Details(
    val edition: String = "",
    val author: String = "Author",
    val rating: Double = 0.0,
    val total_ratings: Int = 0,
    val rank: String = "",
    val returns_policy: String = "",
    val secure_transaction: String = "",
    val description: String = "",
    val reading_age: String = "",
    val print_length: Int = 0,
    val language: String = "",
    val dimensions: String = "",
    val publisher: String = "",
    val image: String = ""
)

data class Book(
    val id: Int = 0,
    val title: String = "",
    val details: Details = Details(),
)

class BookAdapter(private val books: List<Book>) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookImage: ImageView = itemView.findViewById(R.id.book_img)
        val bookTitle: TextView = itemView.findViewById(R.id.book_name)
        val bookAuthor: TextView = itemView.findViewById(R.id.author)
        val bookRating: TextView = itemView.findViewById(R.id.rating)
        val buyButton: Button = itemView.findViewById(R.id.book_buy)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.top_books, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]

        Picasso.get().load(book.details.image).into(holder.bookImage)
//        Picasso.get().load("https://m.media-amazon.com/images/I/51KFyfyK7eL._SY264_BO1,204,203,200_QL40_FMwebp_.jpg").into(holder.bookImage)

        holder.bookTitle.text = book.title
        holder.bookAuthor.text = "By " + book.details.author
        holder.bookRating.text = "Rating: "+book.details.rating.toString()
        holder.buyButton.setOnClickListener {
            val intent = Intent(it.context, DetailActivity::class.java)
            intent.putExtra("bookId", book.id)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount() = books.size
}

class ProductActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.app_bg)
        val yourButton: Button = findViewById(R.id.see_all)
        val buyButton: Button = findViewById(R.id.book)
        yourButton.setOnClickListener {
            moveToshow(it)
        }
        buyButton.setOnClickListener {
            moveToshow(it)
        }


        val database = FirebaseDatabase.getInstance().getReference("books")

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val books = dataSnapshot.children.mapNotNull { it.getValue(Book::class.java) }

                viewManager = LinearLayoutManager(this@ProductActivity)
                viewAdapter = BookAdapter(books)

                recyclerView = findViewById<RecyclerView>(R.id.top_books).apply {
                    setHasFixedSize(true)
                    layoutManager = viewManager
                    adapter = viewAdapter
                }


            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    private fun moveToshow(it: View?) {
        val intent = Intent(this, AllActivity::class.java)
        startActivity(intent)
    }
}
