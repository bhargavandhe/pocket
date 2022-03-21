package com.bhargav.pocket.commons.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bhargav.pocket.R

@Composable
fun ProfileIcon(modifier: Modifier = Modifier.size(32.dp), onClick: () -> Unit = { }) = Image(
    painter = painterResource(id = R.drawable.ic_blank_user),
    contentDescription = "profile",
    modifier = modifier
        .clip(CircleShape)
        .clickable { onClick() },
    contentScale = ContentScale.Crop
)
