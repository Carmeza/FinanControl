package com.example.financontrol.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.financontrol.databinding.ActivityAddTransactionBinding
import com.example.financontrol.viewmodel.TransactionViewModel

class AddTransactionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTransactionBinding
    private val vm: TransactionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val types = listOf("GASTO", "INGRESO")
        binding.spType.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            types
        )

        binding.btnSave.setOnClickListener {
            val type = binding.spType.selectedItem.toString()
            val amountText = binding.etAmount.text.toString().trim()
            val desc = binding.etDesc.text.toString().trim()

            if (amountText.isEmpty()) {
                binding.etAmount.error = "Ingresa un monto"
                return@setOnClickListener
            }

            val amount = amountText.toLongOrNull()?.toDouble()
            if (amount == null || amount <= 0) {
                binding.etAmount.error = "Monto inválido"
                return@setOnClickListener
            }

            if (desc.isEmpty()) {
                binding.etDesc.error = "Ingresa una descripción"
                return@setOnClickListener
            }

            vm.add(type, amount, desc)
            Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
