package com.example.todofy.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todofy.data.local.entity.TodoEntity
import com.example.todofy.data.repository.CategoryRepository
import com.example.todofy.data.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategoryId = MutableStateFlow<Int?>(null)
    val selectedCategoryId = _selectedCategoryId.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val todos = combine(_searchQuery, _selectedCategoryId) { query, categoryId ->
        Pair(query, categoryId)
    }.flatMapLatest { (query, categoryId) ->
        when {
            query.isNotBlank() -> todoRepository.searchTodos(query)
            categoryId != null -> todoRepository.getTodosByCategory(categoryId)
            else -> todoRepository.getAllTodos()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categories = categoryRepository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Statistik untuk dashboard di HomeScreen
    val completedTodosCount = todoRepository.getCompletedTodosCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val pendingTodosCount = todoRepository.getPendingTodosCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val overdueTodosCount = todoRepository.getOverdueTodosCount(Date())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Mengganti kategori yang dipilih
    fun selectCategory(categoryId: Int?) {
        _selectedCategoryId.value = categoryId
    }

    // Mengubah query pencarian
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Mengubah status complete/incomplete todo
    fun toggleTodoCompletion(todo: TodoEntity) {
        viewModelScope.launch {
            todoRepository.updateTodo(todo.copy(
                isCompleted = !todo.isCompleted,
                updatedAt = Date()
            ))
        }
    }

    // Menghapus todo
    fun deleteTodo(todo: TodoEntity) {
        viewModelScope.launch {
            todoRepository.deleteTodo(todo)
        }
    }
}