package com.example.inventorysystem_crud

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomePage : AppCompatActivity() {

    private lateinit var itemAdapter: ItemAdapter
    private var selectedItem: Inventory? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val searchItem = findViewById<EditText>(R.id.searchBar)

        searchItem.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                performSearch(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        val recyclerView = findViewById<RecyclerView>(R.id.inventoryTableRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        itemAdapter = ItemAdapter(emptyList()) { item ->
            selectedItem = item
        }
        recyclerView.adapter = itemAdapter

        loadDatabaseData()

        val goToAddItem = findViewById<Button>(R.id.addItemButton)
        goToAddItem.setOnClickListener {
            val nextPage = Intent(this, AddItem::class.java)
            startActivity(nextPage)
        }

        val goToEditItem = findViewById<Button>(R.id.editItemButton)
        goToEditItem.setOnClickListener {
            if (selectedItem != null) {
                val nextPage = Intent(this, EditItem::class.java)
                nextPage.putExtra("ITEM_TO_EDIT", selectedItem)
                startActivity(nextPage)
            }
            else {
                Toast.makeText(this, "Please select an item first", Toast.LENGTH_SHORT).show()
            }
        }

        val deleteItem = findViewById<Button>(R.id.deleteItemButtom)
        deleteItem.setOnClickListener {
            val itemToDelete = selectedItem
            if (itemToDelete != null) {
                AlertDialog.Builder(this)
                    .setTitle("Delete Item")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Yes") { _, _ ->
                        val db = AppDatabase.getDatabase(this)
                        lifecycleScope.launch(Dispatchers.IO) {
                            db.itemDao().deleteItem(itemToDelete)
                            val updatedList = db.itemDao().getAllItems()
                            withContext(Dispatchers.Main) {
                                itemAdapter.updateData(updatedList)
                                selectedItem = null
                                Toast.makeText(this@HomePage, "Item deleted successfully", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    .setNegativeButton("No", null)
                    .show()
            } else {
                Toast.makeText(this, "Please select an item to delete", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadDatabaseData()
    }

    private fun loadDatabaseData() {
        val db = AppDatabase.getDatabase(this)
        lifecycleScope.launch(Dispatchers.IO) {
            val items = db.itemDao().getAllItems()
            withContext(Dispatchers.Main) {
                itemAdapter.updateData(items)
            }
        }
    }

    private fun performSearch(query: String) {
        val db = AppDatabase.getDatabase(this)
        lifecycleScope.launch(Dispatchers.IO) {
            val results = if (query.isEmpty()) {
                db.itemDao().getAllItems()
            } else {
                db.itemDao().searchDatabase("%$query%")
            }
            withContext(Dispatchers.Main) {
                itemAdapter.updateData(results)
            }
        }
    }
}