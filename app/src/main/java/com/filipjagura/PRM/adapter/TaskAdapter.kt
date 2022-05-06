import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.core.os.HandlerCompat
import androidx.recyclerview.widget.RecyclerView
import com.filipjagura.PRM.adapter.TaskVh
import com.filipjagura.PRM.data.TaskDB
import com.filipjagura.PRM.data.TaskDto
import com.filipjagura.PRM.databinding.ItemTaskBinding
import kotlin.concurrent.thread

class TaskAdapter (private val db: TaskDB, private val listenerClick:(TaskDto)->Unit/*, private val listenerLongClick:(TaskDto)->Boolean*/) : RecyclerView.Adapter<TaskVh>(){


    private var tasksList = listOf<TaskDto>()
    private val mainHandler = HandlerCompat.createAsync(Looper.getMainLooper())



    override fun onBindViewHolder(holder: TaskVh, position: Int) {
        val item = tasksList[position]
        holder.bind(tasksList[position])
        holder.itemView.setOnClickListener{listenerClick(item)}
        /*holder.itemView.setOnLongClickListener{listenerLongClick(item)}*/

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

    fun delete(position: Int) = thread{
        val task = tasksList[position]
        db.taskDto.delete(task.id)
        load()
    }

    fun taskThisWeek(): Int{
        var tasks: Int
        thread{
            tasks = db.taskDto.getNotDoneTillEndOfWeek().size
        }
        return tasks

    }

    fun load() = thread{
        tasksList=db.taskDto.getNotDoneNotExpired()
        mainHandler.post{
            notifyDataSetChanged()
        }
    }


}