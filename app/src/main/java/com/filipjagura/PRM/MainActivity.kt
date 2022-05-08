package com.filipjagura.PRM


import TaskAdapter
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.filipjagura.PRM.adapter.touchHelper
import com.filipjagura.PRM.data.TaskDB
import com.filipjagura.PRM.data.TaskDto
import com.filipjagura.PRM.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Future


private const val REQUEST_EDIT_TASK = 3
private const val REQUEST_ADD_TASK = 2
private const val REQUEST_POPUP = 5

class MainActivity : AppCompatActivity() {

    private val view by lazy { ActivityMainBinding.inflate(layoutInflater)}
    private val taskAdapter by lazy {TaskAdapter(TaskDB.open(this),{ goToDetails(it)},{setPopUp(it)})}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)
        setupTaskList()
        setupAddButton()
    }

    private fun setupTaskList() {
        println("Main - tasks list set up & weeksummary")
        val itemSwipe = ItemTouchHelper(touchHelper(taskAdapter,weekSummaryValue))
        view.taskList.apply{
            adapter=taskAdapter
            layoutManager=LinearLayoutManager(context)
            itemSwipe.attachToRecyclerView(this)
        }
        reload()
    }

    private fun setupAddButton() = view.addButton.setOnClickListener{
        println("Main - Add button clicked")
        startActivityForResult(Intent(this,AddTaskActivity::class.java), REQUEST_ADD_TASK)
    }

    private fun goToDetails(taskDto:TaskDto) = startActivityForResult(Intent(this,OpenDetailsActivity::class.java).putExtra("taskId",taskDto.id),
        REQUEST_EDIT_TASK)

    private fun setPopUp(taskDto: TaskDto){
        println("Main - Setting PopUp")
        val window = PopupWindow(view.root,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,true)
        window.isOutsideTouchable=true
        val view = layoutInflater.inflate(R.layout.activity_pop_up_window,null)
        window.contentView=view
        view.findViewById<Button>(R.id.button_yes).setOnClickListener{
            println("Main - PopUp - Yes button pressed")
            taskAdapter.resolveTask(taskDto)
            window.dismiss()
            reload()
        }
        view.findViewById<Button>(R.id.button_no).setOnClickListener{
            println("Main - PopUp - No button pressed")
            window.dismiss()
        }
        window.showAtLocation(view,Gravity.CENTER,0,0)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_ADD_TASK && resultCode== Activity.RESULT_OK){
            println("Main - reload & weeksummary")
            reload()
        }else if (requestCode == REQUEST_POPUP && resultCode== Activity.RESULT_OK){
            println("Main - reload & weeksummary")
            true
            reload()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun reload(){
        val future = taskAdapter.load()
        while (future.isAlive){}
        println("Weeksummary to be set : " + taskAdapter.calculateThisWeek())
        weekSummaryValue.text=taskAdapter.calculateThisWeek().toString()


    }

}


