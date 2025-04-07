package com.example.todofy.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todofy.data.local.entity.CategoryEntity
import com.example.todofy.data.local.entity.TodoEntity
import com.example.todofy.ui.components.CategoryChip
import com.example.todofy.ui.components.CompletedTasksCard
import com.example.todofy.ui.components.OverdueTasksCard
import com.example.todofy.ui.components.PendingTasksCard
import com.example.todofy.ui.components.SearchBar
import com.example.todofy.ui.components.SwipeableTodoItem
import com.example.todofy.utils.Converters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToAddTodo: () -> Unit,
    onNavigateToEditTodo: (Int) -> Unit,
    onNavigateToStatistics: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val todos by viewModel.todos.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategoryId by viewModel.selectedCategoryId.collectAsStateWithLifecycle()
    val completedTodosCount by viewModel.completedTodosCount.collectAsStateWithLifecycle()
    val pendingTodosCount by viewModel.pendingTodosCount.collectAsStateWithLifecycle()
    val overdueTodosCount by viewModel.overdueTodosCount.collectAsStateWithLifecycle()

    val converters = remember { Converters() }

    val filteredTodos by remember(todos, selectedCategoryId, searchQuery) {
        derivedStateOf {
            if (searchQuery.isNotBlank()) {
                todos.filter { todo ->
                    todo.title.contains(searchQuery, ignoreCase = true) ||
                            todo.description.contains(searchQuery, ignoreCase = true)
                }
            } else if (selectedCategoryId != null) {
                todos.filter { it.categoryId == selectedCategoryId }
            } else {
                todos
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddTodo,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah Tugas",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Header dengan judul dan statistik
            HomeHeader(
                completedTodosCount = completedTodosCount,
                pendingTodosCount = pendingTodosCount,
                overdueTodosCount = overdueTodosCount,
                onStatisticsClick = onNavigateToStatistics
            )

            // Search Bar
            SearchBar(
                query = searchQuery,
                onQueryChange = viewModel::setSearchQuery
            )

            // Kategori Filter
            if (categories.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Opsi "Semua" untuk menampilkan seluruh todo
                    item {
                        CategoryChip(
                            category = CategoryEntity(
                                id = 0,
                                name = "Semua",
                                colorHex = "#000000"
                            ),
                            isSelected = selectedCategoryId == null,
                            onClick = { viewModel.selectCategory(null) }
                        )
                    }

                    // Menampilkan seluruh kategori
                    items(categories) { category ->
                        CategoryChip(
                            category = category,
                            isSelected = selectedCategoryId == category.id,
                            onClick = { viewModel.selectCategory(category.id) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Daftar Todo
            if (filteredTodos.isEmpty()) {
                // Tampilan saat tidak ada todo
                EmptyTodoList()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = filteredTodos,
                        key = { it.id }
                    ) { todo ->
                        val dismissState = rememberDismissState(
                            confirmValueChange = { value ->
                                if (value == DismissValue.DismissedToStart) {
                                    viewModel.deleteTodo(todo)
                                    true
                                } else if (value == DismissValue.DismissedToEnd) {
                                    viewModel.toggleTodoCompletion(todo)
                                    false
                                } else {
                                    false
                                }
                            }
                        )

                        // Mencari kategori untuk mendapatkan warna
                        val categoryColor = categories.find { it.id == todo.categoryId }?.let {
                            converters.toColor(it.colorHex)
                        } ?: MaterialTheme.colorScheme.primary

                        SwipeableTodoItem(
                            todo = todo,
                            categoryColor = categoryColor,
                            onCheckedChange = { viewModel.toggleTodoCompletion(it) },
                            onEditClick = { onNavigateToEditTodo(it.id) },
                            onDeleteClick = { viewModel.deleteTodo(it) },
                            dismissState = dismissState
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeHeader(
    completedTodosCount: Int,
    pendingTodosCount: Int,
    overdueTodosCount: Int,
    onStatisticsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "TodoFy",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )

            FloatingActionButton(
                onClick = onStatisticsClick,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Assessment,
                    contentDescription = "Statistik",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Kartu statistik
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CompletedTasksCard(
                count = completedTodosCount,
                modifier = Modifier.weight(1f)
            )
            PendingTasksCard(
                count = pendingTodosCount,
                modifier = Modifier.weight(1f)
            )
            OverdueTasksCard(
                count = overdueTodosCount,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun EmptyTodoList() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(0.8f),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Tidak ada tugas",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tekan tombol + untuk menambahkan tugas baru",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}