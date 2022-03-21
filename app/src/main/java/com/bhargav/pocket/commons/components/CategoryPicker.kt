package com.bhargav.pocket.commons.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bhargav.pocket.model.Categories
import com.bhargav.pocket.model.Category
import com.bhargav.pocket.model.categoryMapping

@ExperimentalFoundationApi
@Composable
fun CategoryPicker(
    modifier: Modifier = Modifier,
    categories: Array<Categories> = Categories.values(),
    value: Int = 0,
    enabled: Boolean = true,
    onSelect: (Categories) -> Unit
) {
    var selected = categories[value]

    VerticalGrid(columns = 4, modifier = modifier) {
        categories.take(8).forEach { category ->
            val categoryObj: Category = categoryMapping[category]!!
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(bottom = 8.dp)) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(shape = CircleShape)
                        .background(
                            if (category == selected) categoryObj.color
                            else Color.Transparent,
                            shape = CircleShape
                        )
                        .clickable {
                            if (enabled) {
                                selected = category
                                onSelect(selected)
                            }
                        },
                    contentAlignment = Alignment.Center,
                    content = {
                        Image(
                            painter = painterResource(id = categoryObj.icon),
                            contentDescription = "icon",
                        )
                    }
                )

                Text(text = categoryObj.title, style = MaterialTheme.typography.caption)
            }
        }
    }
}
