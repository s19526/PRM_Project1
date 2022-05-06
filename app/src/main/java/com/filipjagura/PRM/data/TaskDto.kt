package com.filipjagura.PRM.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskDto (

    @PrimaryKey(autoGenerate = true)
    val id: Long=0,

    val title: String,
    var priority: String,
    var progress: Int,
    val date:  String,
    val estimate: Double,
    var done: Boolean=false
){
    fun resolveTask(){
        done = true
        progress = 100
    }
}



