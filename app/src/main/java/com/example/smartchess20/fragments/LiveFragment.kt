package com.example.smartchess20.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import com.example.smartchess20.R
import com.example.smartchess20.Utils
import com.example.smartchess20.boardManagement.ChessBoardManager
import com.example.smartchess20.databinding.FragmentLiveBinding
import com.example.smartchess20.network.WebSocketCommunication
import com.example.smartchess20.roomDB.GameDB
import com.example.smartchess20.roomDB.entities.Game
import com.example.smartchess20.roomDB.viewModels.GameViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class LiveFragment : Fragment(R.layout.fragment_live) {
    /* logic */
    private val utils = Utils()
    private lateinit var webSocketCommunication: WebSocketCommunication
    private lateinit var viewModel: GameViewModel
    private lateinit var chessBoardManager: ChessBoardManager
    private var pgn: String? = null

    /* UI */
    private lateinit var chessboard: GridLayout
    private lateinit var chessboardTiles: Array<Array<ImageView>>
    private lateinit var connectionTextView: TextView
    private lateinit var gameStateTextView: TextView
    private lateinit var moveStateTextView: TextView
    private lateinit var pieceStateTextView: TextView
    private lateinit var pgnTextView: TextView
    private lateinit var buttonConnextion: TextView
    private lateinit var buttonSave: TextView
    private lateinit var buttonReset: TextView

    /* binding */
    private var _binding: FragmentLiveBinding? = null
    private val binding get() = _binding!!

    /**
     * Overridden methods
     */

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLiveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
        initializeViewModel()
        chessBoardManager = ChessBoardManager(this.requireContext())
        chessBoardManager.configureChessboardTiles(chessboard, chessboardTiles)
        initializeWebSocketCommunication()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Important: Clean up binding to avoid memory leaks
    }

    override fun onStop() {
        super.onStop()
        webSocketCommunication.closeWebsocket()
    }

    /**
     * View initialization methods
     */

    private fun initializeViewModel() {
        val db = GameDB.getInstance(this.requireContext())
        viewModel = GameViewModel(db)
    }

    private fun initializeViews() {
        chessboard = binding.chessboard
        chessboardTiles = Array(8) { Array(8) { ImageView(context) } }
        connectionTextView = binding.connectionTextView
        gameStateTextView = binding.gameStateTextView
        moveStateTextView = binding.moveStateTextView
        pieceStateTextView = binding.pieceStateTextView
        pgnTextView = binding.pgnTextView
        buttonConnextion = binding.buttonTextView
        setupButtonConnection()
        buttonSave = binding.buttonSaveTextView
        setupButtonSave()
        buttonReset = binding.buttonResetTextView
        setupResetButton()
    }

    private fun setupButtonConnection() {
        buttonConnextion.setText("Connect")
        buttonConnextion.setOnClickListener {
            if (buttonConnextion.text == "Connect") {
                webSocketCommunication.connectToWebSocket()
            } else {
                buttonConnextion.setText("Connect")
                buttonConnextion.setBackgroundResource(R.drawable.button_connect)
                webSocketCommunication.closeWebsocket()
            }
        }
    }

    private fun setupButtonSave() {
        buttonSave.setOnClickListener {
            if (buttonConnextion.text == "Connect") {
                utils.showShortToast(this.requireContext(), "You are not connected")
            } else {
                val newGame = Game(
                    pgn = pgn ?: "",
                    white_player = "Player White",
                    black_player = "Player Black"
                )
                viewModel.viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        viewModel.insertGame(newGame)
                    }
                }
                utils.showShortToast(this.requireContext(), "Game was saved")
            }
        }
    }

    private fun setupResetButton() {
        buttonReset.setOnClickListener {
            if (buttonConnextion.text == "Connect") {
                utils.showShortToast(this.requireContext(), "You are not connected")
            } else {
                val dataToSendBack = JSONObject()
                dataToSendBack.put("action", "reset")
                webSocketCommunication.sendDataToServer(dataToSendBack.toString())
            }
        }
    }

    /**
     * WebSocket communication methods
     */

    private fun initializeWebSocketCommunication() {
        webSocketCommunication = WebSocketCommunication(
            onMessage = { message ->
                handleWebSocketMessage(message)
                Log.v("LiveFragment", "Message received: $message")
                activity?.runOnUiThread {
                    buttonConnextion.setText("Disonnect")
                    buttonConnextion.setBackgroundResource(R.drawable.button_disconnect)
                    connectionTextView.setText("Connected")
                }
            },
            onFailure = { t, response ->
                Log.v("LiveFragment", "WebSocket failure: ${t.message}")
                t.printStackTrace()
                activity?.runOnUiThread {
                    connectionTextView.setText("Disconnected")
                    buttonConnextion.setText("Connect")
                    buttonConnextion.setBackgroundResource(R.drawable.button_connect)
                }
            },
            onClosed = { code, reason ->
                Log.v("LiveFragment", "WebSocket closed: $code / $reason")
                webSocketCommunication.closeWebsocket()
                activity?.runOnUiThread {
                    connectionTextView.text = "Disconnected"
                    buttonConnextion.text = "Connect"
                    buttonConnextion.setBackgroundResource(R.drawable.button_connect)
                    chessBoardManager.clearBoard(chessboard, chessboardTiles)
                }
            }
        )
    }

    private fun handleWebSocketMessage(message: String) {
        val json = JSONObject(message)

        pgn = json.getString("pgn")
        val formattedPgn = pgn?.replace(Regex("\\d+\\."), "\n$0") ?: ""
        val gameState = json.getString("game")
        val moveState = json.getString("move")
        val pieceState = json.getString("piece")
        val board = json.getJSONArray("board")

        // Run on the UI thread since WebSocket operates on a background thread
        activity?.runOnUiThread {
            gameStateTextView.setText(gameState)
            moveStateTextView.setText(moveState)
            pieceStateTextView.setText(pieceState)
            pgnTextView.setText(formattedPgn)
            chessBoardManager.updateChessboard(chessboard, chessboardTiles, board)
        }
    }
}