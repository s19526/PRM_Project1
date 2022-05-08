package com.filipjagura.PRM.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {

    @Query("SELECT * FROM TaskDto ORDER BY date ASC")
    fun getAll(): List<TaskDto>

    @Query("SELECT * FROM TaskDto WHERE done=0 AND date>= DATE('NOW') ORDER BY date ASC")
    fun getNotDoneNotExpired(): List<TaskDto>

    @Query("SELECT COUNT(*) FROM TaskDto WHERE done=0 AND date BETWEEN DATE() AND DATE('NOW','weekday 0') ORDER BY date ASC")
    fun getNotDoneTillEndOfWeek(): Long

    @Query("SELECT * FROM TaskDto WHERE id=:id")
    fun findById(id: Long) : TaskDto

    @Insert
    fun insert(taskDto: TaskDto)

    @Update
    fun update(taskDto: TaskDto)

    @Query("DELETE FROM TaskDto WHERE id = :id")
    fun delete(id: Long)



}