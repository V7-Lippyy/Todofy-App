package com.example.todofy.ui.screens.add_edit_todo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todofy.data.local.entity.TodoEntity
import com.example.todofy.data.repository.CategoryRepository
import com.example.todofy.data.repository.TodoRepository
import com.example.todofy.utils.setTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddEditTodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
    private val categoryRepository: CategoryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var todoTitle by mutableStateOf("")
        private set

    var todoDescription by mutableStateOf("")
        private set

    var selectedCategoryId by mutableStateOf(1) // Default ke kategori pertama
        private set

    var startDate by mutableStateOf<Date?>(null)
        private set

    var dueDate by mutableStateOf<Date?>(null)
        private set

    var hasTime by mutableStateOf(false)
        private set

    private val todoId: Int = savedStateHandle["todoId"] ?: -1
    private var existingTodo: TodoEntity? = null

    val categories = categoryRepository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        if (todoId != -1) {
            viewModelScope.launch {
                todoRepository.getTodoById(todoId)?.let { todo ->
                    existingTodo = todo
                    todoTitle = todo.title
                    todoDescription = todo.description
                    selectedCategoryId = todo.categoryId
                    startDate = todo.startDate
                    dueDate = todo.dueDate
                    hasTime = todo.hasTime
                }
            }
        } else {
            // Default untuk tugas baru: tanggal mulai = hari ini
            startDate = Date()
        }
    }

    fun onEvent(event: AddEditTodoEvent) {
        when (event) {
            is AddEditTodoEvent.OnTitleChange -> {
                todoTitle = event.title
            }
            is AddEditTodoEvent.OnDescriptionChange -> {
                todoDescription = event.description
            }
            is AddEditTodoEvent.OnCategorySelect -> {
                selectedCategoryId = event.categoryId
            }
            is AddEditTodoEvent.OnStartDateChange -> {
                // Jika tanggal mulai diubah dan tanggal tenggat ada sebelumnya
                // pastikan tanggal tenggat tidak lebih awal dari tanggal mulai
                startDate = event.date
                if (dueDate != null && startDate != null && dueDate!!.before(startDate)) {
                    // Set tenggat sama dengan tanggal mulai jika tenggat sebelumnya lebih awal
                    dueDate = startDate
                }
            }
            is AddEditTodoEvent.OnDueDateChange -> {
                dueDate = event.date
            }
            is AddEditTodoEvent.OnTimeChange -> {
                if (event.forStartDate && startDate != null) {
                    startDate = startDate!!.setTime(event.hour, event.minute)
                } else if (!event.forStartDate && dueDate != null) {
                    dueDate = dueDate!!.setTime(event.hour, event.minute)
                }
            }
            is AddEditTodoEvent.OnHasTimeChange -> {
                hasTime = event.hasTime
            }
            AddEditTodoEvent.OnSaveTodo -> {
                viewModelScope.launch {
                    if (todoTitle.isBlank()) {
                        _uiEvent.emit(UiEvent.ShowSnackbar("Judul tidak boleh kosong"))
                        return@launch
                    }

                    if (dueDate != null && startDate != null && dueDate!!.before(startDate)) {
                        _uiEvent.emit(UiEvent.ShowSnackbar("Tenggat waktu harus setelah tanggal mulai"))
                        return@launch
                    }

                    val todo = TodoEntity(
                        id = existingTodo?.id ?: 0,
                        title = todoTitle,
                        description = todoDescription,
                        categoryId = selectedCategoryId,
                        startDate = startDate,
                        dueDate = dueDate,
                        hasTime = hasTime,
                        isCompleted = existingTodo?.isCompleted ?: false,
                        createdAt = existingTodo?.createdAt ?: Date(),
                        updatedAt = Date()
                    )

                    if (existingTodo != null) {
                        todoRepository.updateTodo(todo)
                    } else {
                        todoRepository.insertTodo(todo)
                    }

                    _uiEvent.emit(UiEvent.SaveTodo)
                }
            }
        }
    }

    sealed class UiEvent {
        object SaveTodo : UiEvent()
        data class ShowSnackbar(val message: String) : UiEvent()
    }
}