package com.example.todofy.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todofy.data.local.entity.CategoryEntity
import com.example.todofy.data.local.entity.TodoEntity
import com.example.todofy.ui.components.CategoryChip
import com.example.todofy.ui.components.SearchBar
import com.example.todofy.ui.components.SwipeableTodoItem
import com.example.todofy.ui.theme.Blue
import com.example.todofy.ui.theme.Green
import com.example.todofy.ui.theme.Orange
import com.example.todofy.ui.theme.Red
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

    // Gradient background
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    )

    // Animasi masuk item
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = true) {
        isVisible = true
    }

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
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 12.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Tambah Tugas",
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Header dengan kartu statistik
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(24.dp), // Lebih rounded
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Lebih elevated
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp) // Padding lebih besar
                    ) {
                        // Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // App Title dengan styling
                            Text(
                                text = "TodoFy",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = (-1).sp
                                ),
                                color = MaterialTheme.colorScheme.primary
                            )

                            // Tampilkan tanggal dengan efek
                            val today = remember {
                                val dateFormat = SimpleDateFormat("EEEE, d MMM", Locale("id", "ID"))
                                dateFormat.format(Date())
                            }

                            Surface(
                                shape = RoundedCornerShape(24.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                tonalElevation = 2.dp,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarToday,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )

                                    Spacer(modifier = Modifier.width(6.dp))

                                    Text(
                                        text = today,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Medium
                                        ),
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Statistik dalam satu baris dengan animasi
                        AnimatedVisibility(
                            visible = isVisible,
                            enter = fadeIn() + slideInVertically(
                                initialOffsetY = { 50 },
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            )
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                StatItemModern(
                                    count = completedTodosCount,
                                    label = "Selesai",
                                    icon = Icons.Default.CheckCircle,
                                    color = Green
                                )
                                StatItemModern(
                                    count = pendingTodosCount,
                                    label = "Tertunda",
                                    icon = Icons.Default.Pending,
                                    color = Blue
                                )
                                StatItemModern(
                                    count = overdueTodosCount,
                                    label = "Terlambat",
                                    icon = Icons.Default.Error,
                                    color = Red
                                )
                            }
                        }
                    }
                }

                // Filter dan pencarian dengan animasi
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(
                        initialAlpha = 0f,
                        animationSpec = tween(durationMillis = 500, delayMillis = 300)
                    )
                ) {
                    // Search Bar yang lebih modern
                    SearchBar(
                        query = searchQuery,
                        onQueryChange = viewModel::setSearchQuery,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Kategori dalam LazyRow yang lebih kompak dengan animasi
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(
                        initialAlpha = 0f,
                        animationSpec = tween(durationMillis = 500, delayMillis = 400)
                    )
                ) {
                    if (categories.isNotEmpty()) {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Opsi "Semua"
                            item {
                                CategoryChip(
                                    category = CategoryEntity(
                                        id = 0,
                                        name = "Semua",
                                        colorHex = "#757575"
                                    ),
                                    isSelected = selectedCategoryId == null,
                                    onClick = { viewModel.selectCategory(null) }
                                )
                            }

                            // Prioritas
                            items(categories) { category ->
                                CategoryChip(
                                    category = category,
                                    isSelected = selectedCategoryId == category.id,
                                    onClick = { viewModel.selectCategory(category.id) }
                                )
                            }
                        }
                    }
                }

                // Separator dengan label lebih modern
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Indikator visual
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(4.dp)
                                )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Tugas Anda",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }

                    // Tombol statistik yang lebih menarik
                    Surface(
                        onClick = onNavigateToStatistics,
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        tonalElevation = 2.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Assessment,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(16.dp)
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                text = "Statistik",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }

                // Daftar Todo
                if (filteredTodos.isEmpty()) {
                    // Tampilan saat tidak ada todo
                    EmptyTodoListModern(
                        onAddClick = onNavigateToAddTodo
                    )
                } else {
                    // LazyColumn dengan efek animasi
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 80.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp) // Spacing lebih besar
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

                            // Mencari kategori untuk mendapatkan warna dan nama
                            val category = categories.find { it.id == todo.categoryId }
                            val categoryName = category?.name ?: "Rendah" // Default name

                            // Warna prioritas
                            val priorityColor = when (categoryName) {
                                "Sangat Rendah" -> Green
                                "Rendah" -> Blue
                                "Tinggi" -> Orange
                                "Sangat Tinggi" -> Red
                                else -> MaterialTheme.colorScheme.primary
                            }

                            // Animasi item
                            AnimatedVisibility(
                                visible = isVisible,
                                enter = fadeIn(
                                    initialAlpha = 0f,
                                    animationSpec = tween(durationMillis = 300)
                                ) + slideInVertically(
                                    initialOffsetY = { 50 },
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                )
                            ) {
                                SwipeableTodoItem(
                                    todo = todo,
                                    categoryColor = priorityColor,
                                    categoryName = categoryName,
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
    }
}

@Composable
fun StatItemModern(
    count: Int,
    label: String,
    icon: ImageVector,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Ikon dengan efek
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(16.dp)) // Rounded rectangle
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            color.copy(alpha = 0.2f),
                            color.copy(alpha = 0.1f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Jumlah dengan efek
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold
            )
        )

        // Label
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun EmptyTodoListModern(
    onAddClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Card dengan efek blur background
        Card(
            modifier = Modifier.fillMaxWidth(0.85f),
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Ilustrasi animasi
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    // Inner decoration
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        // Icon
                        Icon(
                            imageVector = Icons.Default.AddTask,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Tidak Ada Tugas",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Jadwalkan tugas Anda dan tingkatkan produktivitas",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Tombol Tambah yang lebih modern
                Button(
                    onClick = onAddClick,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Tambah Tugas",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}