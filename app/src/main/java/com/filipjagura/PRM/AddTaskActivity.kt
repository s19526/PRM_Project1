package com.filipjagura.PRM

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.filipjagura.PRM.data.TaskDB
import com.filipjagura.PRM.data.TaskDto
import com.filipjagura.PRM.databinding.ActivityAddTaskBinding
import kotlinx.android.synthetic.main.activity_add_task.*
import java.time.LocalDate
import java.util.concurrent.Executors
import kotlin.properties.Delegates

class AddTaskActivity : AppCompatActivity() {

    private val view by lazy {ActivityAddTaskBinding.inflate(layoutInflater)}
    private val executor by lazy { Executors.newSingleThreadExecutor() }
    private val db by lazy { TaskDB.open(this)}
    private var id by Delegates.notNull<Long>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)
        setupLoad()
        setupSave()
    }

    private fun setupLoad(){
        println("Add task - data loaded")
        this.id = intent.getLongExtra("taskId",0)
        if(id>0){
            executor.submit(){
                    val taskDto=db.taskDto.findById(id)
                    view.nameInput.setText(taskDto.title)
                    view.dateInput.init(LocalDate.parse(taskDto.date).year, LocalDate.parse(taskDto.date).monthValue-1,LocalDate.parse(taskDto.date).dayOfMonth) { _, _, _, _ -> }
                when(taskDto.priority){
                        radioButton1.text->radioGroup.check(radioButton1.id)
                        radioButton2.text->radioGroup.check(radioButton2.id)
                        radioButton3.text->radioGroup.check(radioButton3.id)
                    }
                    view.estimateInput.setText(taskDto.estimate.toString())
                    view.progressInput.progress=taskDto.progress
            }
        }
    }

    private fun setupSave() = view.saveButton.setOnClickListener{
        println("Add task - save button clicked")
        executor.submit{
            var taskDto=TaskDto(
                id=id,
                title = view.nameInput.text.toString().ifEmpty { "" },
                date = LocalDate.of(view.dateInput.year,view.dateInput.month+1,view.dateInput.dayOfMonth).toString(),
                priority = when(view.radioGroup.checkedRadioButtonId){
                    radioButton1.id->radioButton1.text.toString()
                    radioButton2.id->radioButton2.text.toString()
                    radioButton3.id->radioButton3.text.toString()
                    else -> radioButton1.text.toString()
                },
                progress = view.progressInput.progress,
                estimate = view.estimateInput.text.toString().toDouble(),
            )
            println(taskDto.date)
            when(id){
                0L -> db.taskDto.insert(taskDto)
                else -> db.taskDto.update(taskDto)
            }
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    override fun onDestroy() {
        println("Add task - destroy")
        super.onDestroy()
        executor.shutdownNow()
    }
}