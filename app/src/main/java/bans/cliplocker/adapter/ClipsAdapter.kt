package bans.cliplocker.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import bans.cliplocker.R
import bans.cliplocker.adapter.ClipsAdapter.DataObjectHolder
import bans.cliplocker.databinding.CardClipBinding
import bans.cliplocker.model.Clip
import java.util.*

class ClipsAdapter(private var mDataset: ArrayList<Clip>, private val callback: Callback) :
    RecyclerView.Adapter<DataObjectHolder>() {

    private var layoutInflater: LayoutInflater? = null

    inner class DataObjectHolder(var binding: CardClipBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        init {
            binding.root.setOnClickListener {
                callback.onNoteClick(
                    mDataset[bindingAdapterPosition]
                )
            }
            binding.delete.setOnClickListener {
                callback.onNoteDelete(
                    mDataset[bindingAdapterPosition]
                )
            }
            binding.copy.setOnClickListener {
                callback.onNoteCopy(
                    mDataset[bindingAdapterPosition]
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataObjectHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: CardClipBinding = DataBindingUtil.inflate(
            layoutInflater!!, R.layout.card_clip, parent, false
        )
        return DataObjectHolder(binding)
    }

    override fun onBindViewHolder(holder: DataObjectHolder, position: Int) {
        val feed = mDataset[position]
        holder.binding.clip = feed
        //Log.e("object : ",""+chat.toString());
        //holder.binding.tvTime.setText(B_util.getDateCurrentTimeZone(feed.getTimestamp()));
    }

    fun deleteItem(index: Int) {
        mDataset.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun getItemCount(): Int {
        return mDataset.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refresh_list(dataset: ArrayList<Clip>) {
        mDataset = dataset
        notifyDataSetChanged()
    }

    interface Callback {
        fun onNoteClick(note: Clip)
        fun onNoteDelete(note: Clip)
        fun onNoteCopy(note: Clip)
    }
}