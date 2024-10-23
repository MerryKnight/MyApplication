package com.example.myapplication

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate
class Expense(
    private val amount: Double,
    private val category: String,
    private val date: LocalDate
) {

    fun display() {
        println("Сумма: $amount, Категория: $category, Дата: $date")
    }
    fun getAmount(): Double = amount
    fun getCategory(): String = category
}

class ExpenseManager {
    private val expenses = mutableListOf<Expense>()
    fun addExpense(expense: Expense) {
        expenses.add(expense)
        println("Добавлен новый расход:")
        expense.display()
    }
    fun getAllExpenses(): Boolean {
        if (expenses.isEmpty()) {
            return false;
        } else {
            println("Список всех расходов:")
            for (expense in expenses) {
                expense.display()
            }
        }
        return true
    }

    fun getTotalByCategory(): Boolean{
        if (expenses.isEmpty()) {
            return false
        }
        val categoryTotals = mutableMapOf<String, Double>()
        for (expense in expenses) {
            val category = expense.getCategory()
            val amount = expense.getAmount()
            categoryTotals[category] = categoryTotals.getOrDefault(category, 0.0) + amount
        }
        println("Сумма расходов по категориям:")
        for ((category, total) in categoryTotals) {
            println("Категория: $category, Общая сумма: $total")
        }
        return true
    }
}
class MainActivity : AppCompatActivity() {
    private val expenseManager = ExpenseManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val amountEditText = findViewById<EditText>(R.id.amountEditText)
        val categoryEditText = findViewById<EditText>(R.id.categoryEditText)
        val dateEditText = findViewById<EditText>(R.id.dateEditText)
        val addButton = findViewById<Button>(R.id.addButton)
        val displayAllButton = findViewById<Button>(R.id.displayAllButton)
        val totalByCategoryButton = findViewById<Button>(R.id.totalByCategoryButton)
        val resultTextView = findViewById<TextView>(R.id.resultTextView)
        addButton.setOnClickListener {
            val amount = amountEditText.text.toString().toDoubleOrNull()
            val category = categoryEditText.text.toString()
            val dateString = dateEditText.text.toString()

            if (amount != null && category.isNotEmpty() && dateString.isNotEmpty()) {
                val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalDate.parse(dateString)
                } else {
                    return@setOnClickListener
                }

                val expense = Expense(amount, category, date)
                expenseManager.addExpense(expense)

                amountEditText.text.clear()
                categoryEditText.text.clear()
                dateEditText.text.clear()

                resultTextView.text = "Расход добавлен!"
            } else {
                resultTextView.text = "Пожалуйста, заполните все поля!"
            }
        }

        displayAllButton.setOnClickListener {
            val expenses = expenseManager.getAllExpenses()
            if (expenses == false) {
                resultTextView.text = "Список расходов пуст."
            }
        }

        totalByCategoryButton.setOnClickListener {
            val categoryTotals = expenseManager.getTotalByCategory()
            if (categoryTotals == false) {
                resultTextView.text = "Нет данных для подсчета."
            }
            }
        }
    }



