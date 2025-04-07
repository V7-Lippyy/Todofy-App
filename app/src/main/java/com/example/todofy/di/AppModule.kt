package com.example.todofy.di

import android.content.Context
import com.example.todofy.data.local.dao.CategoryDao
import com.example.todofy.data.local.dao.TodoDao
import com.example.todofy.data.local.database.TodofyDatabase
import com.example.todofy.data.repository.CategoryRepository
import com.example.todofy.data.repository.TodoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTodofyDatabase(@ApplicationContext context: Context): TodofyDatabase {
        return TodofyDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideTodoDao(database: TodofyDatabase): TodoDao {
        return database.todoDao()
    }

    @Provides
    @Singleton
    fun provideCategoryDao(database: TodofyDatabase): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    @Singleton
    fun provideTodoRepository(todoDao: TodoDao): TodoRepository {
        return TodoRepository(todoDao)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(categoryDao: CategoryDao): CategoryRepository {
        return CategoryRepository(categoryDao)
    }
}