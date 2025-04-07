package com.example.todofy.data.repository

import com.example.todofy.data.local.dao.TodoDao
import com.example.todofy.data.local.entity.TodoEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor(
    private val todoDao: TodoDao
) {
    fun getAllTodos(): Flow<List<TodoEntity>> = todoDao.getAllTodos()

    fun getTodosByCategory(categoryId: Int): Flow<List<TodoEntity>> =
        todoDao.getTodosByCategory(categoryId)

    fun searchTodos(query: String): Flow<List<TodoEntity>> =
        todoDao.searchTodos(query)

    fun getCompletedTodosCount(): Flow<Int> = todoDao.getCompletedTodosCount()

    fun getPendingTodosCount(): Flow<Int> = todoDao.getPendingTodosCount()

    fun getOverdueTodosCount(currentDate: Date = Date()): Flow<Int> =
        todoDao.getOverdueTodosCount(currentDate)

    suspend fun getTodoById(id: Int): TodoEntity? = todoDao.getTodoById(id)

    suspend fun insertTodo(todo: TodoEntity): Long = todoDao.insertTodo(todo)

    suspend fun updateTodo(todo: TodoEntity) = todoDao.updateTodo(todo)

    suspend fun deleteTodo(todo: TodoEntity) = todoDao.deleteTodo(todo)

    suspend fun deleteTodoById(id: Int) = todoDao.deleteTodoById(id)
}