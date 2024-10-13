package com.example.smartchess20.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartchess20.R
import com.example.smartchess20.adapters.HistoryAdapter
import com.example.smartchess20.databinding.FragmentHistoryBinding
import com.example.smartchess20.roomDB.GameDB
import com.example.smartchess20.roomDB.viewModels.GameViewModel

class HistoryFragment : Fragment(R.layout.fragment_history) {
    /* logic */
    private lateinit var db: GameDB
    private lateinit var viewModel: GameViewModel

    /* binding */
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupGameViewModel()
        setupRecyclerView()
    }

    private fun setupGameViewModel() {
        db = GameDB.getInstance(this.requireContext())
        viewModel = GameViewModel(db)
    }

    private fun setupRecyclerView() {
        val adapter = HistoryAdapter(viewModel)
        val recyclerView = binding.historyRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView.adapter = adapter
    }
}