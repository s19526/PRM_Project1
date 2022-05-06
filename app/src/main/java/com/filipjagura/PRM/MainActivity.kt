package com.filipjagura.PRM


import TaskAdapter
import android.app.Activity
import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.filipjagura.PRM.adapter.touchHelper
import com.filipjagura.PRM.data.TaskDB
import com.filipjagura.PRM.data.TaskDto
import com.filipjagura.PRM.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*



private const val REQUEST_RESOLVE_TASK = 4
private const val REQUEST_EDIT_TASK = 3
private const val REQUEST_ADD_TASK = 2

class MainActivity : AppCompatActivity() {

    private val view by lazy { ActivityMainBinding.inflate(layoutInflater)}
    private val taskAdapter by lazy {TaskAdapter(TaskDB.open(this)) { goToDetails(it) } }
    //private val taskAdapter = TaskAdapter(taskDB) { goToDetails(it) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)
        setupTaskList()
        setupAddButton()
        //taskAdapter.setListener { goToDetails(it) }
    }

    private fun setupTaskList() {
        val itemSwipe = ItemTouchHelper(touchHelper(taskAdapter))
        view.taskList.apply{
            adapter=taskAdapter
            layoutManager=LinearLayoutManager(context)
            itemSwipe.attachToRecyclerView(this)
        }
        weekSummaryValue.text = taskAdapter.taskThisWeek().toString()
        println(weekSummaryValue.text)
        taskAdapter.load()
    }


    private fun setupAddButton() = view.addButton.setOnClickListener{
        startActivityForResult(Intent(this,AddTaskActivity::class.java), REQUEST_ADD_TASK)
    }

    private fun goToDetails(taskDto:TaskDto) = startActivityForResult(Intent(taskList.context,OpenDetailsActivity::class.java).putExtra("taskId",taskDto.id),
        REQUEST_EDIT_TASK)


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_ADD_TASK && resultCode== Activity.RESULT_OK){
           recreate()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


}


