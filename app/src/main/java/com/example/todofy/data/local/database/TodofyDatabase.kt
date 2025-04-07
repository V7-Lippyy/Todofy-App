package com.example.todofy.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todofy.data.local.dao.CategoryDao
import com.example.todofy.data.local.dao.TodoDao
import com.example.todofy.data.local.entity.CategoryEntity
import com.example.todofy.data.local.entity.TodoEntity
import com.example.todofy.data.model.defaultCategories
import com.example.todofy.utils.Converters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [TodoEntity::class, CategoryEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TodofyDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var INSTANCE: TodofyDatabase? = null

        fun getDatabase(context: Context): TodofyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodofyDatabase::class.java,
                    "todofy_database"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Menambahkan kategori default saat database pertama kali dibuat
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    insertDefaultCategories(database.categoryDao())
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // Fungsi untuk menambahkan kategori default
        private suspend fun insertDefaultCategories(categoryDao: CategoryDao) {
            defaultCategories.forEach { category ->
                categoryDao.insertCategory(
                    CategoryEntity(
                        id = category.id,
                        name = category.name,
                        colorHex = String.format("#%06X", 0xFFFFFF and category.color.value.toInt())
                    )
                )
            }
        }
    }
}