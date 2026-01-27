package com.example.financontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.financontrol.ui.compose.FinanNav
import com.example.financontrol.viewmodel.TransactionViewModel

class MainActivity : ComponentActivity() {


    private val vm: TransactionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface {
                    FinanNav(vm = vm)
                }
            }
        }
    }
}