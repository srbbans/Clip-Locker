package bans.cliplocker

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import bans.cliplocker.adapter.ClipsAdapter
import bans.cliplocker.databinding.ActivityMainBinding
import bans.cliplocker.model.Clip
import bans.cliplocker.repository.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var dataset: ArrayList<Clip>
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        context = this

        initViews()

        getClips()

    }

    private fun initViews() {
        binding.recyclerView.setHasFixedSize(true)


    }

    private fun getClips() {
        lifecycleScope.launch(Dispatchers.IO) {
            val db =
                Room.databaseBuilder(applicationContext, AppDatabase::class.java, "clips").build()
            val dao = db.clipDao()
            dataset = ArrayList(dao.getAll())
            withContext(Dispatchers.Main) {
                binding.recyclerView.adapter =
                    ClipsAdapter(dataset, object : ClipsAdapter.Callback {
                        override fun onNoteClick(note: Clip) {

                        }

                        override fun onNoteDelete(note: Clip) {
                            lifecycleScope.launch(Dispatchers.IO) {
                                dao.deleteClip(note)
                            }
                        }

                        override fun onNoteCopy(note: Clip) {
                            val clipboard: ClipboardManager =
                                context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Clip Locker", note.message)

                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT)
                                .show()
                        }
                    })
            }
        }
    }


}