package com.safetymarcus.shifttracker.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.ExperimentalTransitionApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.safetymarcus.shifttracker.daysInMonth
import kotlin.math.absoluteValue

const val columnCount = 7

@Composable
fun Month(
    month: Int,
    today: Int,
) {
    val dayCount by remember { derivedStateOf { daysInMonth(month) } }
    var selectedDay by remember { mutableStateOf(today - 1) } //-1 to 0 index
    val selectedColumn by remember { derivedStateOf { selectedDay % columnCount } }
    val selectedRow by remember { derivedStateOf { selectedDay / columnCount } }
    var cellSize by remember { mutableStateOf(0.dp) }
    val localDensity = LocalDensity.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned {
                cellSize = with(localDensity) { (it.size.width / 7).toDp() }
            }
    ) {
        repeat(columnCount) {
            DayHeader(
                day = it,
                width = cellSize,
                x = it * cellSize,
                selected = selectedColumn == it,
            )
        }
        repeat(dayCount) {
            val column = (it % columnCount)
            val row = it / columnCount
            Day(
                day = it + 1,
                width = cellSize,
                selected = selectedDay == it,
                x = column * cellSize,
                y = (row + 1) * cellSize,
                xDistanceFromSelected = -(column - selectedColumn),
                yDistanceFromSelected = -(row - selectedRow),
            ) { selectedDay = it }
        }
    }
}

@Composable
fun DayHeader(
    day: Int,
    width: Dp,
    x: Dp,
    selected: Boolean,
) = Box(
    modifier = Modifier.size(width),
    contentAlignment = Alignment.Center,
) {
    val text by rememberDayText(day)
    val textColor by rememberHeaderColor(selected)
    Text(
        modifier = Modifier.offset(x = x),
        style = MaterialTheme.typography.titleMedium,
        text = text,
        color = textColor,
    )
}

@Composable
private fun rememberHeaderColor(selected: Boolean) = animateColorAsState(
    if (selected) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.onSurface
)

@Composable
private fun rememberDayText(day: Int) = remember {
    derivedStateOf {
        when (day) {
            0 -> "S"
            1 -> "M"
            2 -> "T"
            3 -> "W"
            4 -> "T"
            5 -> "F"
            6 -> "S"
            else -> ""
        }
    }
}

@Composable
fun Day(
    day: Int,
    width: Dp,
    selected: Boolean,
    xDistanceFromSelected: Int,
    yDistanceFromSelected: Int,
    x: Dp,
    y: Dp,
    onClick: () -> Unit = {}
) {
    val color by rememberBackgroundColor(selected)
    val textColor by rememberTextColor(selected)

    val scale by rememberScale(
        selected = selected,
        absoluteDistance = maxOf(
            xDistanceFromSelected.absoluteValue,
            yDistanceFromSelected.absoluteValue
        )
    )
    val targetXOffset by rememberXOffset(xDistanceFromSelected)
    val targetYOffset by rememberYOffset(yDistanceFromSelected)
    val alignment = rememberLabelTransition(selected)
    Box(
        modifier = Modifier.size(width)
            .offset(x, y)
            .clip(CircleShape)
            .clickable { onClick() }
            .graphicsLayer {
                translationX = targetXOffset.toPx()
                translationY = targetYOffset.toPx()
                scaleX = scale
                scaleY = scale
            }
            .background(color, shape = CircleShape)
    ) {
        Text(
            modifier = Modifier.align(alignment.targetState),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium,
            color = textColor,
            text = "$day"
        )
    }
}

@Composable
private fun rememberBackgroundColor(selected: Boolean) = animateColorAsState(
    if (selected) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.secondary
)

@Composable
private fun rememberTextColor(selected: Boolean) = animateColorAsState(
    if (selected) MaterialTheme.colorScheme.onPrimary
    else MaterialTheme.colorScheme.onSecondary
)

@Composable
private fun rememberScale(
    selected: Boolean,
    absoluteDistance: Int
) = animateFloatAsState(
    when {
        selected -> 1f
        absoluteDistance == 0 -> 0.6f
        absoluteDistance > 1 -> 0.4f
        else -> 0.5f
    },
    animationSpec = spring(
        dampingRatio =
        if (selected) Spring.DampingRatioMediumBouncy
        else Spring.DampingRatioMediumBouncy,
        stiffness =
        if (selected) Spring.StiffnessMediumLow
        else Spring.StiffnessLow,
    )
)

@Composable
private fun rememberXOffset(xDistanceFromSelected: Int) = animateDpAsState(
    when (xDistanceFromSelected) {
        1, 3 -> 2.dp / xDistanceFromSelected
        2 -> 4.dp / xDistanceFromSelected
        else -> 0.dp
    },
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMediumLow,
    )
)

@Composable
private fun rememberYOffset(yDistanceFromSelected: Int) = animateDpAsState(
    when (yDistanceFromSelected) {
        1, 3 -> 2.dp / yDistanceFromSelected
        2 -> 4.dp
        else -> 0.dp
    },
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMediumLow,
    )
)

@OptIn(ExperimentalTransitionApi::class)
@Composable
private fun rememberLabelTransition(selected: Boolean): Transition<Alignment> {
    val state = remember { MutableTransitionState(Alignment.Center) }
    val transition = rememberTransition(
        transitionState = state
    )
    state.targetState = if (selected) Alignment.TopCenter else Alignment.Center
    return transition
}