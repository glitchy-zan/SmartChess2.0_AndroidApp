package com.example.smartchess20.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.smartchess20.MainActivity
import com.example.smartchess20.R
import com.example.smartchess20.Utils
import com.example.smartchess20.bottomSheets.HistoryItemBottomSheet
import com.example.smartchess20.interfaces.OnHistoryItemActionListener
import com.example.smartchess20.roomDB.entities.Game
import com.example.smartchess20.roomDB.viewModels.GameViewModel
import kotlinx.coroutines.launch

@SuppressLint("NotifyDataSetChanged")
class HistoryAdapter(private val gameViewModel: GameViewModel) :
    RecyclerView.Adapter<HistoryAdapter.MenuViewHolder>() {

    private val utils = Utils()
    private var historyItems: MutableList<Game> = mutableListOf()

    init {
        queryHistoryItems()
    }

    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pgnTextView = itemView.findViewById<TextView>(R.id.textViewPgn)
        val copyTextView = itemView.findViewById<TextView>(R.id.textViewCopy)
        val textViewNameWhite = itemView.findViewById<TextView>(R.id.textViewNameWhite)
        val textViewNameBlack = itemView.findViewById<TextView>(R.id.textViewNameBlack)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        if (position % 2 != 0) holder.itemView.setBackgroundResource(R.color.gray_dark)
        else holder.itemView.setBackgroundResource(R.color.gray)
        val pgnText = historyItems[position].pgn
        holder.pgnTextView.setText(pgnText)
        holder.textViewNameWhite.setText(historyItems[position].white_player)
        holder.textViewNameBlack.setText(historyItems[position].black_player)
        holder.copyTextView.setOnClickListener {
            utils.copyToClipboard(holder.itemView.context, pgnText)
            utils.showShortToast(holder.itemView.context, "Copied to clipboard")
            utils.startBlinkingAnimation(holder.copyTextView)
        }
        holder.pgnTextView.setOnClickListener {
            val bottomSheet = HistoryItemBottomSheet(object : OnHistoryItemActionListener {
                override fun onItemUpdated(item: Game) {
                    updateHistoryItem(item)
                }

                override fun onItemDeleted(item: Game) {
                    deleteHistoryItem(item, holder.adapterPosition)
                }
            }, historyItems[position])
            bottomSheet.show(
                (holder.itemView.context as MainActivity).supportFragmentManager,
                "HistoryItemBottomSheet"
            )
        }
    }

    override fun getItemCount(): Int = historyItems.size

    /**
     * back thread functions
     */

    private fun queryHistoryItems() {
        gameViewModel.viewModelScope.launch {
            historyItems = gameViewModel.getAllGames()
            notifyDataSetChanged()
        }
    }

    fun updateHistoryItem(item: Game) {
        gameViewModel.viewModelScope.launch {
            historyItems = gameViewModel.updateGame(item)
            notifyDataSetChanged()
        }
    }

    fun deleteHistoryItem(item: Game, pos: Int) {
        gameViewModel.viewModelScope.launch {
            historyItems = gameViewModel.deleteGame(item)
            notifyItemRemoved(pos)
            notifyDataSetChanged()
        }
    }
}