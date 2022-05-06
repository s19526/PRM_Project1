package com.filipjagura.PRM.adapter

import androidx.recyclerview.widget.RecyclerView
import com.filipjagura.PRM.data.TaskDto
import com.filipjagura.PRM.databinding.ItemTaskBinding

class TaskVh(private val view: ItemTaskBinding) : RecyclerView.ViewHolder(view.root){
    fun bind(taskDto: TaskDto){
        with(view){
            nameText.text=taskDto.title
            dateText.text=taskDto.date
            priorityText.text=taskDto.priority
            progressBar.progress=taskDto.progress
            progressPercent.text = taskDto.progress.toString()+"%"
        }
    }
}