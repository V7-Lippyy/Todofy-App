package com.example.todofy.ui.screens.add_edit_todo

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todofy.ui.components.CategoryChip
import com.example.todofy.utils.calculateRemainingTime
import com.example.todofy.utils.formatToDisplayDate
import com.example.todofy.utils.formatToDisplayDateTime
import com.example.todofy.utils.formatToDisplayTime
import kotlinx.coroutines.flow.collect
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTodoScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddEditTodoViewModel = hiltViewModel()
) {
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Menghitung estimasi waktu tersisa
    val remainingTime by remember(viewModel.startDate, viewModel.dueDate) {
        derivedStateOf {
            calculateRemainingTime(viewModel.startDate, viewModel.dueDate)
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AddEditTodoViewModel.UiEvent.SaveTodo -> {
                    onNavigateBack()
                }
                is AddEditTodoViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    // Date Picker setup
    val calendar = Calendar.getInstance()

    val startDatePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            viewModel.onEvent(AddEditTodoEvent.OnStartDateChange(calendar.time))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val dueDatePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            viewModel.onEvent(AddEditTodoEvent.OnDueDateChange(calendar.time))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = if (viewModel.todoTitle.isBlank()) "Tambah Tugas" else "Edit Tugas") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(AddEditTodoEvent.OnSaveTodo) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Simpan Tugas",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Judul Task
            OutlinedTextField(
                value = viewModel.todoTitle,
                onValueChange = { viewModel.onEvent(AddEditTodoEvent.OnTitleChange(it)) },
                label = { Text("Judul") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Deskripsi Task
            OutlinedTextField(
                value = viewModel.todoDescription,
                onValueChange = { viewModel.onEvent(AddEditTodoEvent.OnDescriptionChange(it)) },
                label = { Text("Deskripsi") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Pilihan Prioritas
            Text(
                text = "Prioritas",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (categories.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { category ->
                        CategoryChip(
                            category = category,
                            isSelected = category.id == viewModel.selectedCategoryId,
                            onClick = { viewModel.onEvent(AddEditTodoEvent.OnCategorySelect(category.id)) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Checkbox untuk set waktu (jam & menit)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = viewModel.hasTime,
                    onCheckedChange = {
                        viewModel.onEvent(AddEditTodoEvent.OnHasTimeChange(it))
                    }
                )
                Text(
                    text = "Sertakan waktu (jam & menit)",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tanggal & Waktu Mulai
            Text(
                text = "Tanggal Mulai",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { startDatePickerDialog.show() },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Pilih Tanggal Mulai"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = viewModel.startDate?.formatToDisplayDate() ?: "Pilih Tanggal"
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Tombol Time Picker untuk tanggal mulai (hanya muncul jika hasTime = true)
                if (viewModel.hasTime && viewModel.startDate != null) {
                    IconButton(
                        onClick = {
                            val cal = Calendar.getInstance().apply { time = viewModel.startDate!! }
                            TimePickerDialog(
                                context,
                                { _, hourOfDay, minute ->
                                    viewModel.onEvent(AddEditTodoEvent.OnTimeChange(hourOfDay, minute, true))
                                },
                                cal.get(Calendar.HOUR_OF_DAY),
                                cal.get(Calendar.MINUTE),
                                true
                            ).show()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Set Waktu Mulai"
                        )
                    }

                    Text(
                        text = viewModel.startDate?.formatToDisplayTime() ?: "--:--",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tanggal & Waktu Batas
            Text(
                text = "Batas Waktu",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { dueDatePickerDialog.show() },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Pilih Tanggal Batas"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = viewModel.dueDate?.formatToDisplayDate() ?: "Pilih Tanggal"
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Tombol Time Picker untuk tenggat (hanya muncul jika hasTime = true)
                if (viewModel.hasTime && viewModel.dueDate != null) {
                    IconButton(
                        onClick = {
                            val cal = Calendar.getInstance().apply { time = viewModel.dueDate!! }
                            TimePickerDialog(
                                context,
                                { _, hourOfDay, minute ->
                                    viewModel.onEvent(AddEditTodoEvent.OnTimeChange(hourOfDay, minute, false))
                                },
                                cal.get(Calendar.HOUR_OF_DAY),
                                cal.get(Calendar.MINUTE),
                                true
                            ).show()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Set Waktu Tenggat"
                        )
                    }

                    Text(
                        text = viewModel.dueDate?.formatToDisplayTime() ?: "--:--",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Tampilkan estimasi waktu
            if (viewModel.startDate != null && viewModel.dueDate != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Estimasi waktu: $remainingTime",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}