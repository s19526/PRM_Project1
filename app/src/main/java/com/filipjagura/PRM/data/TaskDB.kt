package com.filipjagura.PRM.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

private const val FILENAME= "task_db_file"

@Database(
    entities = [
        TaskDto::class
    ],
    version = 1
)

abstract class TaskDB : RoomDatabase() {
    abstract val taskDto: TaskDao
    companion object{
        fun open (context: Context) = Room.databaseBuilder(context,TaskDB::class.java, FILENAME).build()
    }
}