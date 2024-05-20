package com.example.superagenda.presentation.screens.taskEdit.composables

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar


@Composable
fun DateTimePicker(
    initialDateTime: LocalDateTime,
    onDateTimeSelected: (LocalDateTime) -> Unit
) {
    var selectedDateTime by remember { mutableStateOf(initialDateTime) }

    val context = LocalContext.current
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
        selectedDateTime =
            selectedDateTime.withYear(year).withMonth(month + 1).withDayOfMonth(dayOfMonth)
        onDateTimeSelected(selectedDateTime)
    }

    val timeListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
        selectedDateTime = selectedDateTime.withHour(hour).withMinute(minute)
        onDateTimeSelected(selectedDateTime)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Selected Date: ${selectedDateTime.format(dateFormatter)}")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Selected Time: ${selectedDateTime.format(timeFormatter)}")
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val now = Calendar.getInstance()
            DatePickerDialog(
                context,
                dateListener,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
            ).show()
        }) {
            Text(text = "Pick Date")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            val now = Calendar.getInstance()
            TimePickerDialog(
                context,
                timeListener,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
            ).show()
        }) {
            Text(text = "Pick Time")
        }
    }
}
