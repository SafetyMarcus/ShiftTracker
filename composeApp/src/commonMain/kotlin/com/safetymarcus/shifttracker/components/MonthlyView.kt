package com.safetymarcus.shifttracker.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.safetymarcus.shifttracker.daysInMonth
import kotlin.math.absoluteValue

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Month(
    month: Int,
) {
    val dayCount by remember { derivedStateOf { daysInMonth(month) } }
    var selectedDay by remember { mutableStateOf(0) } //TODO pass in driven by model
    val selectedColumn by remember { derivedStateOf { selectedDay % 5  } }
    val selectedRow by remember { derivedStateOf { selectedDay/5 }}

    val size by remember { mutableStateOf(80.dp) } //TODO calculate at runtime
    Box(
        modifier = Modifier.fillMaxWidth().padding(12.dp),
    ){
        repeat(dayCount) {
            val column = (it % 5)
            val xSpace = if (column > 0) 8.dp else 0.dp
            val row = it / 5
            val ySpace = if (row > 0) 8.dp else 0.dp
            Day(
                day = it + 1,
                selected = selectedDay == it,
                x = column * size + xSpace,
                y = row * size + ySpace, //TODO calculate column and row count
                xDistanceFromSelected = -(column - selectedColumn),
                yDistanceFromSelected = -(row - selectedRow),
            ) { selectedDay = it }
        }
    }
}

//TODO change to box layout to allow text and background to animate independently
@Composable
fun Day(
    day: Int,
    selected: Boolean,
    xDistanceFromSelected: Int,
    yDistanceFromSelected: Int,
    x: Dp,
    y: Dp,
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

    val absoluteDistance = maxOf(xDistanceFromSelected.absoluteValue, yDistanceFromSelected.absoluteValue)
    val scale by animateFloatAsState(
        if (selected) 1.5f
        else if (absoluteDistance == 0) 1f
        else if (absoluteDistance > 1) 0.8f
        else 0.9f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessLow,
        )
    )

    val targetXOffset by animateDpAsState(
        if (xDistanceFromSelected == 1 || xDistanceFromSelected == 3) 2.dp/xDistanceFromSelected
        else if (xDistanceFromSelected == 2) 4.dp/xDistanceFromSelected
        else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium,
        )
    )

    val targetYOffset by animateDpAsState(
        if (yDistanceFromSelected == 1 || yDistanceFromSelected == 3) 2.dp/yDistanceFromSelected
        else if (yDistanceFromSelected == 2) 4.dp
        else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium,
        )
    )

    Text(
        modifier = Modifier
            .offset(x, y)
            .size(64.dp)
            .graphicsLayer {
                translationX = targetXOffset.toPx()
                translationY = targetYOffset.toPx()
                scaleX = scale
                scaleY = scale
            }
            .background(color, shape = CircleShape)
            .clickable { onClick() },
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.labelMedium,
        color = textColor,
        text = "$day"
    )
}