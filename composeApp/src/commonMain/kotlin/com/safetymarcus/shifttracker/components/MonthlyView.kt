package com.safetymarcus.shifttracker.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.safetymarcus.shifttracker.daysInMonth

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Month(
    month: Int,
) {
    val dayCount by remember { derivedStateOf { daysInMonth(month) } }
    var selectedDay by remember { mutableStateOf(0) } //TODO pass in driven by model

    //TODO change from flow row to calculated box layout to allow for animated bounce
    FlowRow(
        modifier = Modifier.fillMaxWidth().padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(dayCount) {
            Day(
                day = it + 1,
                selected = selectedDay == it,
                distanceFromSelected = it - selectedDay,
            ) { selectedDay = it }
        }
    }
}

//TODO change to box layout to allow text and background to animate independently
@Composable
fun Day(
    day: Int,
    selected: Boolean,
    distanceFromSelected: Int,
    onClick: () -> Unit = {}
) {
    val color by animateColorAsState(
        if (selected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.secondary
    )
    val textColor by animateColorAsState(
        if (selected) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onPrimary
    )
    val size by animateDpAsState(if (selected) 88.dp else 60.dp)

    val targetOffset by animateDpAsState(
        if (selected) 0.dp else (14/distanceFromSelected).dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium,
        )
    )

    Text(
        modifier = Modifier
            .offset(x = targetOffset, y = 0.dp)
            .size(size)
            .background(color, shape = CircleShape)
            .clickable { onClick() },
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.labelMedium,
        color = textColor,
        text = "$day"
    )
}