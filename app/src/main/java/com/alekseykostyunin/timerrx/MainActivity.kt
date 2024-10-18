package com.alekseykostyunin.timerrx

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alekseykostyunin.timerrx.ui.theme.TimerRxTheme

class MainActivity : ComponentActivity() {

    private var viewModel: TimerViewModel = TimerViewModel()

    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TimerRxTheme {
                val timer by viewModel.timer.collectAsState()
                val showSeconds = remember { mutableStateOf(true) }
                var checked by remember { mutableStateOf(false) }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Таймер", fontWeight = FontWeight.Bold, fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(50.dp))
                    Text(
                        text = if (showSeconds.value) {
                            timer?.let { String.format("%02d:%02d", it / 60, it % 60) } ?: "00:00"
                        } else {
                            timer?.let { String.format("%02d мин.", it / 60) } ?: "00 мин"
                        },
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Log.d("TEST_MainActivity", timer.toString())

                    Spacer(modifier = Modifier.height(46.dp))

                    Row {
                        Button(
                            onClick = { viewModel.startTimer() },
                            Modifier.padding(end = 25.dp)
                        ) {
                            Text(text = "Старт")
                        }
                        Button(
                            onClick = { viewModel.cancelTimer() },
                            Modifier.padding(start = 25.dp)
                        ) {
                            Text(text = "Сброс")
                        }
                    }

                    Spacer(modifier = Modifier.height(26.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Показывать только минуты", Modifier.padding(end = 25.dp))
                        Switch(
                            checked = checked,
                            onCheckedChange = {
                                checked = it
                                showSeconds.value = !checked
                            },
                            thumbContent = {
                                if (checked) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(SwitchDefaults.IconSize),
                                    )
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(26.dp))

//                    Button(
//                        onClick = {
//
//                        },
//                        Modifier.padding(end = 25.dp)
//                    ) {
//                        Text(text = "Запустить новый таймер")
//                    }


                }
            }
        }
    }
}