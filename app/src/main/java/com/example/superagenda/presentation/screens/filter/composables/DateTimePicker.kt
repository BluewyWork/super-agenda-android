package com.example.superagenda.presentation.screens.filter.composables

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
   initialDateTime: LocalDateTime? = null,
   onDateTimeSelected: (LocalDateTime?) -> Unit
) {
   var selectedDateTime by remember { mutableStateOf(initialDateTime) }

   val context = LocalContext.current
   val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
   val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

   val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
      val newDate =
         selectedDateTime?.withYear(year)?.withMonth(month + 1)?.withDayOfMonth(dayOfMonth)
            ?: LocalDateTime.of(year, month + 1, dayOfMonth, 0, 0)
      selectedDateTime = newDate
      onDateTimeSelected(selectedDateTime)
   }

   val timeListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
      val newTime = selectedDateTime?.withHour(hour)?.withMinute(minute)
         ?: LocalDateTime.of(
            LocalDateTime.now().year,
            LocalDateTime.now().month,
            LocalDateTime.now().dayOfMonth,
            hour,
            minute
         )
      selectedDateTime = newTime
      onDateTimeSelected(selectedDateTime)
   }

   Column(modifier = Modifier.padding(16.dp)) {
      Row(
         horizontalArrangement = Arrangement.SpaceBetween,
         modifier = Modifier.fillMaxWidth()
      ) {
         Text(text = "Selected Date: ${selectedDateTime?.format(dateFormatter) ?: "None"}")

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
      }

      Spacer(modifier = Modifier.height(8.dp))

      Row(
         horizontalArrangement = Arrangement.SpaceBetween,
         modifier = Modifier.fillMaxWidth()
      ) {
         Text(text = "Selected Time: ${selectedDateTime?.format(timeFormatter) ?: "None"}")

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
}
