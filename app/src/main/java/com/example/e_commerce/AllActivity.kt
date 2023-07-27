package com.example.e_commerce

import android.os.Bundle
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
data class BookClass(
    val id: Int = 0,
    val title: String = "",
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
class BookAdapterClass(private val books: List<BookClass>) : RecyclerView.Adapter<BookAdapterClass.BookViewHolder>() {

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

        Picasso.get().load(book.image).into(holder.bookImage)
        holder.bookTitle.text = book.title
        holder.bookAuthor.text = book.author
        holder.bookRating.text = book.rating.toString() + " Star"

        holder.buyButton.setOnClickListener {
        }
    }

    override fun getItemCount() = books.size

}

class AllActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all)
        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.app_bg)

        val database = FirebaseDatabase.getInstance().getReference("books")

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val books = dataSnapshot.children.mapNotNull { it.getValue(Book::class.java) }

                viewManager = LinearLayoutManager(this@AllActivity)
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
}
