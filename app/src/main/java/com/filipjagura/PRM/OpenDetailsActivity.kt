package com.filipjagura.PRM

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.filipjagura.PRM.data.TaskDB
import com.filipjagura.PRM.data.TaskDto
import com.filipjagura.PRM.databinding.ActivityOpenTaskDetailsBinding
import kotlinx.android.synthetic.main.activity_open_task_details.*
import java.util.concurrent.Executors


class OpenDetailsActivity : AppCompatActivity() {


    private val view by lazy {ActivityOpenTaskDetailsBinding.inflate(layoutInflater) }
    private val db by lazy { TaskDB.open(this)}
    private val executor by lazy { Executors.newSingleThreadExecutor() }
    private lateinit var taskDto: TaskDto

    override fun onCreate(savedInstanceState: Bundle?) {

        setupLoad()
        super.onCreate(savedInstanceState)
        setContentView(view.root)
        editTask()
    }

    private fun setupLoad(){
        executor.submit(){
            this.taskDto=db.taskDto.findById(intent.getLongExtra("taskId",1))
            println(this::taskDto.isInitialized)
            view.nameDetailsValue.text=taskDto.title
            view.dateDetailsValue.text=taskDto.date
            view.priorityDetailsValue.text=taskDto.priority
            view.estimateDetailsLabel.text=taskDto.estimate.toString()
            view.progressDetailsBar.progress=taskDto.progress
            view.progressDetailsValue.text=taskDto.progress.toString()+"%"
        }
    }
    /*
    private fun reload(){
        executor.submit(){
            this.taskDto=db.taskDto.findById(taskDto.id)
            println(this::taskDto.isInitialized)
            view.nameDetailsValue.text=taskDto.title
            view.dateDetailsValue.text=taskDto.date
            view.priorityDetailsValue.text=taskDto.priority
            view.estimateDetailsLabel.text=taskDto.estimate.toString()
            view.progressDetailsBar.progress=taskDto.progress
            view.progressDetailsValue.text=taskDto.progress.toString()+"%"
        }
    }*/


    private fun editTask()= view.editButton.setOnClickListener{
        println("EditButton")
        startActivityForResult(Intent(editButton.context,AddTaskActivity::class.java).putExtra("taskId",taskDto.id),4)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 4 && resultCode== Activity.RESULT_OK){
            setupLoad()
            recreate()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        setResult(Activity.RESULT_OK)
        executor.shutdownNow()
    }





}

