package com.bhargav.pocket.model

import androidx.compose.ui.graphics.Color
import com.bhargav.pocket.R
import com.bhargav.pocket.commons.utils.randomId
import java.util.*

data class CategoryData(
    val title: String = "",
    val total: Float = 0f,
    val transactions: List<String> = listOf()
) {
    override fun toString(): String {
        return "CategoryData(total=$total, transactions=$transactions)"
    }
}

data class Category(
    val icon: Int = R.drawable.ic_transfer,
    val title: String = "Transfer",
    val color: Color = Color(0x4D7299FF),
    val emoji: String = "🔁"
) {
    companion object {
        val MoneyIn = Category(
            icon = R.drawable.ic_money_in,
            title = "Money In",
            color = Color(0x4D0073B4),
            emoji = "↙️"
        )
        val MoneyOut = Category(
            icon = R.drawable.ic_money_out,
            title = "Money Out",
            color = Color(0x4DC32D48),
            emoji = "↗️"
        )
        val Food = Category(
            icon = R.drawable.ic_food,
            title = "Food",
            color = Color(0x4DE49D31),
            emoji = "🍕"
        )
        val Bills = Category(
            icon = R.drawable.ic_electricity,
            title = "Bills",
            color = Color(0x4DFAFF00),
            emoji = "🧾"
        )
        val Health = Category(
            icon = R.drawable.ic_health,
            title = "Health",
            color = Color(0x4DFF7E8E),
            emoji = "💟"
        )
        val Shopping = Category(
            icon = R.drawable.ic_shopping,
            title = "Shopping",
            color = Color(0x4D559504),
            emoji = "🛍"
        )
        val Education = Category(
            icon = R.drawable.ic_books,
            title = "Education",
            color = Color(0x4D70ACCE),
            emoji = "📚"
        )
        val Misc = Category(
            icon = R.drawable.ic_misc,
            title = "Misc",
            color = Color(0x4DFFE600),
            emoji = "✨"
        )
    }

    override fun toString(): String {
        return "Category(icon=$icon, title='$title', color=$color)"
    }
}

enum class Categories {
    MONEY_IN, MONEY_OUT, FOOD, BILLS, HEALTH, SHOPPING, EDUCATION, MISC
}

enum class TransactionType {
    DEBIT, CREDIT
}

val categoryMapping: Map<Categories, Category> = mapOf(
    Categories.MONEY_IN to Category.MoneyIn,
    Categories.MONEY_OUT to Category.MoneyOut,
    Categories.FOOD to Category.Food,
    Categories.BILLS to Category.Bills,
    Categories.HEALTH to Category.Health,
    Categories.SHOPPING to Category.Shopping,
    Categories.EDUCATION to Category.Education,
    Categories.MISC to Category.Misc
)

val stringCategoryMapping: Map<String, Category> = mapOf(
    Categories.MONEY_IN.name to Category.MoneyIn,
    Categories.MONEY_OUT.name to Category.MoneyOut,
    Categories.FOOD.name to Category.Food,
    Categories.BILLS.name to Category.Bills,
    Categories.HEALTH.name to Category.Health,
    Categories.SHOPPING.name to Category.Shopping,
    Categories.EDUCATION.name to Category.Education,
    Categories.MISC.name to Category.Misc
)

data class Transaction(
    val transactionId: String = randomId(),
    val title: String = "",
    val amount: Float = 0f,
    val details: String = "",
    val transactionType: TransactionType = TransactionType.DEBIT,
    val category: Categories = Categories.MONEY_IN,
    val date: Long = Date().time,
    val lastEdited: Long = Date().time
) {
    override fun toString(): String {
        return "Transaction(transactionId='$transactionId', title='$title', amount=$amount, details='$details', transactionType=$transactionType, category=$category, date=$date, lastEdited=$lastEdited)"
    }
}
