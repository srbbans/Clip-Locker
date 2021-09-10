package bans.cliplocker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import bans.cliplocker.model.Clip
import bans.cliplocker.repository.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ClipListenerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clip_listener)

        // get Intent
        if (intent != null) {
            val text: String? = intent.getStringExtra(Intent.EXTRA_TEXT)
            if (text != null) {
                Toast.makeText(this, "Got> $text", Toast.LENGTH_SHORT).show()
                clipProcessor(text)
                finish()
            }

            val copiedText = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
            if (copiedText != null) {
                Toast.makeText(this, "Text Stored to ClipLocker> $copiedText", Toast.LENGTH_SHORT)
                    .show()
                clipProcessor(copiedText as String)
                finish()
            }

        }
    }


    private fun clipProcessor(text: String) {
        val clip = Clip(0, text, System.currentTimeMillis(), getTimeIn12AmPm())
        lifecycleScope.launch(Dispatchers.IO) {
            val db =
                Room.databaseBuilder(applicationContext, AppDatabase::class.java, "clips").build()
            val dao = db.clipDao()
            dao.insertClip(clip)
        }
    }

    /**
     * returns the time in 12 hrs format
     */
    private fun getTimeIn12AmPm(): String {
        return try {
            val sdf: DateFormat = SimpleDateFormat("h:mm a dd/MM/yyyy", Locale.getDefault())
            val netDate = Date(System.currentTimeMillis())
            sdf.format(netDate)
        } catch (ex: Exception) {
            "xx"
        }
    }

}