package com.bhargav.pocket.model

data class User(
    val name: String = "User",
    val email: String = "abc@xyz.com",
    val lastReset: Long = 0L,
    val netBalance: Float = 0f,
    val monthlyIncome: Float = 0f,
    val monthlyExpense: Float = 0f,
    val spendings: Map<String, CategoryData> = emptyMap(),
    val transactionCount: Int = 0,
    val savedTitles: Map<String, Int> = emptyMap()
) {

    override fun toString(): String {
//        val x = savedTitles.toList().sortedBy { it.second }.toMap()
        return "User(name='$name', email='$email', lastReset=$lastReset, netBalance=$netBalance, monthlyIncome=$monthlyIncome, monthlyExpense=$monthlyExpense, spendings=$spendings, transactionCount=$transactionCount, savedTitles=$savedTitles)"
    }
}
