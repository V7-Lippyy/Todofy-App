package com.example.todofy.ui.screens.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todofy.data.local.entity.CategoryEntity
import com.example.todofy.data.repository.CategoryRepository
import com.example.todofy.data.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.util.Date
import javax.inject.Inject

data class CategoryWithTodoCount(
    val category: CategoryEntity,
    val todoCount: Int
)

data class StatisticsState(
    val categoriesWithTodoCount: List<CategoryWithTodoCount> = emptyList(),
    val completedTodos: Int = 0,
    val pendingTodos: Int = 0,
    val overdueTodos: Int = 0,
    val totalTodos: Int = 0,
    val completionRate: Float = 0f
)

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val categories = categoryRepository.getAllCategories()
    private val completedTodos = todoRepository.getCompletedTodosCount()
    private val pendingTodos = todoRepository.getPendingTodosCount()
    private val overdueTodos = todoRepository.getOverdueTodosCount(Date())

    val statisticsState = combine(
        categories,
        completedTodos,
        pendingTodos,
        overdueTodos
    ) { categoriesList, completed, pending, overdue ->
        val totalTodos = completed + pending
        val completionRate = if (totalTodos > 0) {
            completed.toFloat() / totalTodos
        } else {
            0f
        }

        val categoriesWithCount = categoriesList.map { category ->
            // Untuk simplisitas, kita hanya menggunakan kategori dan tidak query jumlah todo per kategori
            // Dalam implementasi lengkap, akan lebih baik jika mengambil data ini dari repository
            CategoryWithTodoCount(
                category = category,
                todoCount = 0 // Placeholder, seharusnya diambil dari repository
            )
        }

        StatisticsState(
            categoriesWithTodoCount = categoriesWithCount,
            completedTodos = completed,
            pendingTodos = pending,
            overdueTodos = overdue,
            totalTodos = totalTodos,
            completionRate = completionRate
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StatisticsState())
}