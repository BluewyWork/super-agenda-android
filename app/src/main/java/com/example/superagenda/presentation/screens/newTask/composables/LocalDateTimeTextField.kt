package com.example.superagenda.presentation.screens.newTask.composables

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

@Composable
fun LocalDateTimePickerTextField(
   value: LocalDateTime,
   onLocalDateTimeChange: (LocalDateTime) -> Unit,
   label: String,
) {
   var showDatePicker by remember(calculation = { mutableStateOf(false) })
   var showTimePicker by remember(calculation = { mutableStateOf(false) })

   val context = LocalContext.current

   val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
      val updatedDateTime = value.withYear(year).withMonth(month + 1).withDayOfMonth(dayOfMonth)
      onLocalDateTimeChange(updatedDateTime)
      showTimePicker = true
   }

   val timeListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
      val updatedDateTime = value.withHour(hour).withMinute(minute)
      onLocalDateTimeChange(updatedDateTime)
   }

   if (showDatePicker) {
      val now = Calendar.getInstance()
      DatePickerDialog(
         context,
         dateListener,
         now.get(Calendar.YEAR),
         now.get(Calendar.MONTH),
         now.get(Calendar.DAY_OF_MONTH)
      ).show()
      showDatePicker = false
   }

   if (showTimePicker) {
      val now = Calendar.getInstance()

      TimePickerDialog(
         context,
         timeListener,
         now.get(Calendar.HOUR_OF_DAY),
         now.get(Calendar.MINUTE),
         true
      ).show()

      showTimePicker = false
   }

   Row(
      content = {
         OutlinedTextField(
            value = value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            onValueChange = {},
            label = { Text(text = label) },
            singleLine = true,
            readOnly = true,
         )

         Button(
            onClick = {
               showDatePicker = true
            },

            content = {
               Icon(
                  imageVector = Icons.Default.Create,
                  contentDescription = null
               )
            }
         )
      }
   )
}