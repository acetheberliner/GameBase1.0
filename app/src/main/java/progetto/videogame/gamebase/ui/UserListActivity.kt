package progetto.videogame.gamebase.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import progetto.videogame.gamebase.R
import progetto.videogame.gamebase.data.entity.User
import progetto.videogame.gamebase.ui.adapter.user.UserAdapter
import progetto.videogame.gamebase.ui.viewmodel.ContactsViewModel

class UserListActivity : AppCompatActivity() {

    private val contactsViewModel: ContactsViewModel by viewModels()
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        userAdapter = UserAdapter { user ->
            onUserSelected(user)
        }
        recyclerView.adapter = userAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_CONTACTS), PERMISSION_REQUEST_CODE)
        } else {
            loadContactsFromDevice()
        }
    }

    private fun onUserSelected(user: User) {
        val resultIntent = Intent().apply {
            putExtra("selected_user", user)
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContactsFromDevice()
            } else {
                Toast.makeText(this, "Permission denied to read contacts", Toast.LENGTH_SHORT).show()
                returnToSummaryActivity()
            }
        }
    }

    private fun loadContactsFromDevice() {
        CoroutineScope(Dispatchers.IO).launch {
            val contactsList = mutableListOf<User>()
            val cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null
            )

            cursor?.use {
                val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val phoneNumberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

                if (nameIndex >= 0 && phoneNumberIndex >= 0) {
                    while (it.moveToNext()) {
                        val name = it.getString(nameIndex) ?: "N/A"
                        val phoneNumber = it.getString(phoneNumberIndex) ?: "N/A"
                        val user = User(name = name, phoneNumber = phoneNumber)
                        contactsList.add(user)
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@UserListActivity, "Error retrieving contact information", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            contactsViewModel.insertAll(contactsList) // Call ViewModel to insert contacts
            runOnUiThread {
                userAdapter.setUsers(contactsList)
            }
        }
    }

    private fun returnToSummaryActivity() {
        val intent = Intent(this, SummaryActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }
}