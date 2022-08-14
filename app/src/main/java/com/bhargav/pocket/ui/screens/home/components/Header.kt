package com.bhargav.pocket.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bhargav.pocket.Routes
import com.bhargav.pocket.commons.components.ProfileIcon
import com.bhargav.pocket.commons.utils.toCurrency
import com.bhargav.pocket.model.User
import com.bhargav.pocket.ui.theme.SpherePurpleContainer
import com.bhargav.pocket.ui.theme.SpherePurpleOnContainer

@Composable
fun Header(userData: User, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(bottomEnd = 100f)
            )
            .padding(all = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = buildAnnotatedString {
                    append("Hello, ")
                    withStyle(
                        style = SpanStyle(fontWeight = FontWeight.SemiBold),
                        block = { append(userData.name) }
                    )
                },
                fontSize = 18.sp
            )
            ProfileIcon(
                modifier = Modifier.size(42.dp),
                onClick = { navController.navigate(Routes.Settings.route) }
            )
        }

        Text(text = "Your current balance,", style = MaterialTheme.typography.body2)
        Text(
            text = toCurrency(userData.netBalance),
            style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold)
        )

        Row(
            modifier = Modifier.padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = SpherePurpleContainer,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .clickable { navController.navigate(Routes.AddTransaction.route) }
                    .padding(vertical = 10.dp, horizontal = 20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "add",
                        modifier = Modifier.size(16.dp),
                        tint = SpherePurpleOnContainer
                    )
                    Text(
                        text = "add new transaction".uppercase(),
                        style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Medium),
                        color = SpherePurpleOnContainer
                    )
                }
            }
        }
    }
}
