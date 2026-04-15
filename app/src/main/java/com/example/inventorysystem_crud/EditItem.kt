package com.example.inventorysystem_crud

import android.content.Intent
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

class EditItem : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_item)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val item = intent.getSerializableExtra("ITEM_TO_EDIT") as? Inventory

        val editItemId = findViewById<EditText>(R.id.editItemIdEditText)
        val editItemName = findViewById<EditText>(R.id.editItemNameEditText)
        val editItemQuantity = findViewById<EditText>(R.id.editItemQuantityEditText)
        val editItemLocation = findViewById<EditText>(R.id.editItemLocationEditText)
        val editItemStatus = findViewById<EditText>(R.id.editItemStatusEditText)
        val updateBtn = findViewById<Button>(R.id.saveButton)

        item?.let {
            editItemId.setText(it.itemId)
            editItemName.setText(it.itemName)
            editItemQuantity.setText(it.quantity.toString())
            editItemLocation.setText(it.location)
            editItemStatus.setText(it.status)

            editItemId.isEnabled = false
        }

        updateBtn.setOnClickListener {
            if (item != null) {
                val updatedInventory = Inventory(
                    itemId = item.itemId,
                    itemName = editItemName.text.toString(),
                    quantity = editItemQuantity.text.toString().toIntOrNull() ?: 0,
                    location = editItemLocation.text.toString(),
                    status = editItemStatus.text.toString()
                )

                lifecycleScope.launch(Dispatchers.IO) {
                    val db = AppDatabase.getDatabase(this@EditItem)
                    db.itemDao().updateItem(updatedInventory)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@EditItem, "Item Updated Successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }

        val editItemToHomePage = findViewById<ImageButton>(R.id.imageButton2)
        editItemToHomePage.setOnClickListener {
            val nextPage = Intent(this, HomePage::class.java)
            nextPage.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(nextPage)
        }
    }
}