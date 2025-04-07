package com.example.todofy.ui.screens.add_edit_todo

import java.util.Date

sealed class AddEditTodoEvent {
    data class OnTitleChange(val title: String) : AddEditTodoEvent()
    data class OnDescriptionChange(val description: String) : AddEditTodoEvent()
    data class OnCategorySelect(val categoryId: Int) : AddEditTodoEvent()
    data class OnStartDateChange(val date: Date?) : AddEditTodoEvent()
    data class OnDueDateChange(val date: Date?) : AddEditTodoEvent()
    data class OnTimeChange(val hour: Int, val minute: Int, val forStartDate: Boolean) : AddEditTodoEvent()
    data class OnHasTimeChange(val hasTime: Boolean) : AddEditTodoEvent()
    object OnSaveTodo : AddEditTodoEvent()
}