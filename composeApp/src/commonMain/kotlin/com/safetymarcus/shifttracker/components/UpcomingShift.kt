package com.safetymarcus.shifttracker.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.safetymarcus.shifttracker.dateString
import com.safetymarcus.shifttracker.models.Shift
import com.safetymarcus.shifttracker.timeString

@Composable
fun UpcomingShift(
    shift: Shift?
) = Column(
    modifier = Modifier.fillMaxWidth().padding(top = Medium, bottom = Medium),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    shift?.let {
        Text(
            textAlign = TextAlign.Center,
            text = "Next shift starts on",
            style = MaterialTheme.typography.titleLarge,
        )
        SmallSpacer()
        Text(
            textAlign = TextAlign.Center,
            text = shift.startTime.dateString,
            style = MaterialTheme.typography.displayLarge
        )
        SmallSpacer()
        Text(
            textAlign = TextAlign.Center,
            text = "at",
            style = MaterialTheme.typography.titleLarge
        )
        SmallSpacer()
        Text(
            textAlign = TextAlign.Center,
            text = shift.startTime.timeString,
            style = MaterialTheme.typography.displayLarge
        )
    } ?: Text(
        textAlign = TextAlign.Center,
        text = "No upcoming shifts",
        style = MaterialTheme.typography.titleLarge
    )
}