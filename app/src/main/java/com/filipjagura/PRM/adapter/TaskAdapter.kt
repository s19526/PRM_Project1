import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

import androidx.core.os.HandlerCompat
import androidx.recyclerview.widget.RecyclerView
import com.filipjagura.PRM.adapter.TaskVh
import com.filipjagura.PRM.data.TaskDB
import com.filipjagura.PRM.data.TaskDto
import com.filipjagura.PRM.databinding.ItemTaskBinding
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.concurrent.Future
import kotlin.concurrent.thread

class TaskAdapter (private val db: TaskDB, private val listenerClick:(TaskDto)->Unit, private val listenerLongClick:(TaskDto)->Unit) : RecyclerView.Adapter<TaskVh>() {
    private var tasksList = listOf<TaskDto>()
    private val mainHandler = HandlerCompat.createAsync(Looper.getMainLooper())

    override fun onBindViewHolder(holder: TaskVh, position: Int) {
        val item = tasksList[position]
        holder.bind(tasksList[position])
        holder.itemView.setOnClickListener { listenerClick(item) }
        holder.itemView.setOnLongClickListener {
            listenerLongClick(item)
            true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskVh {
        val view = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TaskVh(view)
    }

    override fun getItemCount(): Int = tasksList.size

    fun delete(position: Int) = thread {
        println("Task adapter - task deleted")
        val task = tasksList[position]
        db.taskDto.delete(task.id)
        load()
    }

    fun resolveTask(taskDto: TaskDto) {
        println("Task adapter - task resolved")
        thread {
            taskDto.progress = 100
            taskDto.done = true
            db.taskDto.update(taskDto)
        }
    }

    fun load() = thread {
        tasksList = db.taskDto.getNotDoneNotExpired()
        println("Task adapter - load ")
        mainHandler.post {
            notifyDataSetChanged()
        }
    }


     fun calculateThisWeek():Int{
         val tasks: Int
         val today : LocalDate=  LocalDate.now()
         var eow : LocalDate
         when(today.dayOfWeek){
             DayOfWeek.SUNDAY -> eow=today
             else -> eow=today.plusDays((7-today.dayOfWeek.value).toLong())
         }
         tasks=tasksList.count {(eow.isAfter(LocalDate.parse(it.date))||eow.isEqual(LocalDate.parse(it.date)))}
         println("Task adapter - tasks calculated " + tasks)
         return tasks
    }




}