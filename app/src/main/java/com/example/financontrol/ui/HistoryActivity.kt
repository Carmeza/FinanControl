package com.example.financontrol.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financontrol.databinding.ActivityHistoryBinding
import com.example.financontrol.ui.adapter.TransactionAdapter
import com.example.financontrol.viewmodel.TransactionViewModel

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private val vm: TransactionViewModel by viewModels()
    private val adapter = TransactionAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = adapter

        vm.all.observe(this) { list ->
            adapter.submit(list)
        }
    }
}
