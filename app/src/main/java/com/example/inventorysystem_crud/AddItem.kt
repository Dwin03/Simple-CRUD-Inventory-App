package com.example.inventorysystem_crud

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddItem : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_item)
        
        db = AppDatabase.getDatabase(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val addItemToHomePage = findViewById<ImageButton>(R.id.imageButton)
        addItemToHomePage.setOnClickListener {
            finish()
        }

        val addItemId = findViewById<EditText>(R.id.addItemIdEditText)
        val addItemName = findViewById<EditText>(R.id.addItemNameEditText)
        val addItemQuantity = findViewById<EditText>(R.id.addItemQuantityEditText)
        val addItemLocation = findViewById<EditText>(R.id.addItemLocationEditText)
        val addStatus = findViewById<EditText>(R.id.addStatusEditText)

        val addBtn = findViewById<Button>(R.id.addButton)
        addBtn.setOnClickListener {
            val id = addItemId.text.toString().trim()
            val name = addItemName.text.toString().trim()
            val quantityStr = addItemQuantity.text.toString().trim()
            val location = addItemLocation.text.toString().trim()
            val status = addStatus.text.toString().trim()

            if (id.isEmpty() || name.isEmpty() || quantityStr.isEmpty() || location.isEmpty() || status.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val quantity = quantityStr.toIntOrNull()
            if (quantity == null) {
                Toast.makeText(this, "Quantity must be a valid number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newItem = Inventory(id, name, quantity, location, status)

            lifecycleScope.launch(Dispatchers.IO) {
                db.itemDao().insert(newItem)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddItem, "Item added successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}