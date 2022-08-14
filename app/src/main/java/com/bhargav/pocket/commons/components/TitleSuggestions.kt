package com.bhargav.pocket.commons.components

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.bhargav.pocket.commons.utils.innerPadding
import java.util.*

@ExperimentalMaterialApi
@Composable
fun TitleSuggestions(
    suggestions: Set<String> = setOf("Money In", "Money Out"),
    onClick: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = innerPadding),
        state = rememberLazyListState(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = suggestions.toList()) { suggestion ->
            val text =
                suggestion.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale("en", "IN")) else it.toString()
                }
            Chip(onClick = { onClick(text) }) {
                Text(text = text)
            }
        }
    }
    Text(
        text = "Suggestions are based on your recent activity",
        style = MaterialTheme.typography.overline.copy(letterSpacing = 1.sp),
        modifier = Modifier.padding(horizontal = innerPadding),
        color = LocalContentColor.current.copy(alpha = 0.7f)
    )
}
