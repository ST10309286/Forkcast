package com.example.forkcast

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.security.MessageDigest

@Database(
    entities = [
        User::class,
        ShoppingItem::class,
        MealPlan::class,
        FavoriteMeal::class
    ],
    version = 4,
    exportSchema = false // Optional, prevents schema export errors
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun shoppingListDao(): ShoppingListDao
    abstract fun mealPlanDao(): MealPlanDao
    abstract fun favoriteMealDao(): FavoriteMealDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "forkcast_db"
                )
                    // FIX: ensures app won't crash when schema version changes
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    // Utility for hashing passwords (safe to keep inside or outside)
    object SecurityUtils {
        fun hashPassword(password: String): String {
            val bytes = password.toByteArray()
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)
            return digest.joinToString("") { "%02x".format(it) }
        }
    }
}
