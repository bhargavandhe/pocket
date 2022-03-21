package com.bhargav.pocket.commons.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.bhargav.pocket.ui.theme.typography

@Composable
fun SphereTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    action: @Composable () -> Unit = { },
    navigationIcon: ImageVector,
    onNavigationClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { onNavigationClick() }) {
                Icon(imageVector = navigationIcon, contentDescription = title)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, style = typography.h6)
        }
        action.invoke()
    }
}
