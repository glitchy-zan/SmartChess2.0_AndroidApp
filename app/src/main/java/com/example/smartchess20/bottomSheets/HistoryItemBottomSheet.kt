package com.example.smartchess20.bottomSheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.example.smartchess20.R
import com.example.smartchess20.Utils
import com.example.smartchess20.interfaces.OnHistoryItemActionListener
import com.example.smartchess20.roomDB.entities.Game
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class HistoryItemBottomSheet(
    private val listener: OnHistoryItemActionListener,
    private val historyItem: Game
) : BottomSheetDialogFragment() {

    val utils = Utils()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_history_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val updateViewWhite = view.findViewById<EditText>(R.id.editTextWhitePawn)
        val updateViewBlack = view.findViewById<EditText>(R.id.editTextBlackPawn)
        val updateIcon = view.findViewById<TextView>(R.id.updateTextView)
        updateIcon.setOnClickListener {
            val updateValueWhite = updateViewWhite.text.toString()
            val updateValueBlack = updateViewBlack.text.toString()
            historyItem.white_player = updateValueWhite
            historyItem.black_player = updateValueBlack
            listener.onItemUpdated(historyItem)
            dismiss()
        }
        val deleteIcon = view.findViewById<TextView>(R.id.deleteTextView)
        deleteIcon.setOnClickListener {
            listener.onItemDeleted(historyItem)
            dismiss()
        }
    }
}
