package com.example.smartchess20.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.smartchess20.R
import com.example.smartchess20.boardManagement.ChessBoardManager
import com.example.smartchess20.databinding.FragmentDebugBinding
import com.example.smartchess20.network.WebSocketCommunication
import org.json.JSONObject

class DebugFragment : Fragment(R.layout.fragment_debug) {
    /* logic */
    private lateinit var webSocketCommunication: WebSocketCommunication
    private lateinit var chessBoardManager: ChessBoardManager

    /* UI */
    private lateinit var chessboard: GridLayout
    private lateinit var chessboardTiles: Array<Array<ImageView>>

    /* binding */
    private var _binding: FragmentDebugBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDebugBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureChessboard()
        initializeWebSocketCommunication()
        webSocketCommunication.connectToWebSocket()
    }

    override fun onStop() {
        super.onStop()
        webSocketCommunication.closeWebsocket()
    }

    private fun configureChessboard() {
        chessBoardManager = ChessBoardManager(this.requireContext())
        chessboard = binding.chessboard
        chessboardTiles = Array(8) { Array(8) { ImageView(context) } }
        chessBoardManager.configureChessboardTiles(chessboard, chessboardTiles)
    }

    private fun initializeWebSocketCommunication() {
        webSocketCommunication = WebSocketCommunication(
            onMessage = { message ->
                Log.v("LiveFragment", "Message received: $message")
                handleWebSocketMessage(message)
            },
            onFailure = { t, response ->
                t.printStackTrace()
                Log.v("LiveFragment", "WebSocket failure: ${t.message}")
            },
            onClosed = { code, reason ->
                Log.v("LiveFragment", "WebSocket closed: $code / $reason")
            }
        )
    }

    private fun handleWebSocketMessage(message: String) {
        // Parse the incoming message (JSON format expected)
        val json = JSONObject(message)
        val board = json.getJSONArray("sensorsBoard")
        // Run on the UI thread since WebSocket operates on a background thread
        activity?.runOnUiThread {
            chessBoardManager.updateSensorsBoard(chessboard, chessboardTiles, board)
        }
    }
}