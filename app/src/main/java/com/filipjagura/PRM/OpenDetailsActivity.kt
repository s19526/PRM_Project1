package com.filipjagura.PRM

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import androidx.core.os.HandlerCompat
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
        share()
    }

    private fun setupLoad(){
        executor.submit(){
            println("Open details - data loaded")
            this.taskDto=db.taskDto.findById(intent.getLongExtra("taskId",1))
            view.nameDetailsValue.text=taskDto.title
            view.dateDetailsValue.text=taskDto.date
            view.priorityDetailsValue.text=taskDto.priority
            view.estimateDetailsValue.text=taskDto.estimate.toString()
            view.progressDetailsBar.progress=taskDto.progress
            view.progressDetailsValue.text=taskDto.progress.toString()+"%"
        }
    }

    private fun share() = view.shareButton.setOnClickListener{
        val tempIntent = Intent()
        tempIntent.setAction(Intent.ACTION_SEND).setType("text/plain").putExtra(Intent.EXTRA_TEXT,"Your progress on " + taskDto.title + " is " +  taskDto.progress + "%")
        startActivity(Intent.createChooser(tempIntent,"Share"))
    }



    private fun editTask()= view.editButton.setOnClickListener{
        println("Open details - edit button set up")
        startActivityForResult(Intent(editButton.context,AddTaskActivity::class.java).putExtra("taskId",taskDto.id),4)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 4 && resultCode== Activity.RESULT_OK){
            println("Open details - recreate")
            setupLoad()
            recreate()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        println("Open details - destroy")
        super.onDestroy()
        setResult(Activity.RESULT_OK)
        executor.shutdownNow()
    }





}

