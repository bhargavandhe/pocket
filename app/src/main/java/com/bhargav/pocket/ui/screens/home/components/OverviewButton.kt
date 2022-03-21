package com.bhargav.pocket.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun RowScope.OverviewButton(
    modifier: Modifier = Modifier,
    backgroundColor: Color = (if (isSystemInDarkTheme()) Color.White else Color.Black).copy(alpha = 0.1f),
    title: String,
    subtitle: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit = { },
) {
    ConstraintLayout(
        modifier = modifier
            .weight(1f)
            .height(100.dp)
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = MaterialTheme.shapes.medium
            )
            .clip(shape = MaterialTheme.shapes.medium)
            .clickable { onClick.invoke() }
            .padding(start = 16.dp, end = 18.dp, top = 12.dp, bottom = 16.dp)
    ) {
        val (titleRef, subtitleRef) = createRefs()

        Row(
            modifier = Modifier.constrainAs(titleRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.secondary)
            )
        }

        Text(
            text = subtitle,
            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.constrainAs(subtitleRef) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
            }
        )
    }
}
