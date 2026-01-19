package com.example.financontrol

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.financontrol.databinding.ActivityMainBinding
import com.example.financontrol.ui.AddTransactionActivity
import com.example.financontrol.ui.HistoryActivity
import com.example.financontrol.viewmodel.TransactionViewModel
import com.example.financontrol.utils.MoneyFormatter


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val vm: TransactionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnHistorial.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        binding.btnAgregar.setOnClickListener {
            startActivity(Intent(this, AddTransactionActivity::class.java))
        }

        vm.all.observe(this) { list ->
            val ingresos = list.filter { it.type == "INGRESO" }.sumOf { it.amount }
            val gastos = list.filter { it.type == "GASTO" }.sumOf { it.amount }
            val balance = ingresos - gastos

            binding.tvResumen.text =
                "Balance: ${MoneyFormatter.formatCLP(balance)}"
        }



        }
    }

