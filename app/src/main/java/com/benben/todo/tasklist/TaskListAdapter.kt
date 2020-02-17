import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.benben.todo.R
import kotlinx.android.synthetic.main.item_task.view.*

class TaskListAdapter(public var taskList: List<Task> = emptyList()) :
    RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {


    // Déclaration d'une lambda comme variable:
    var onEditClickListener: ((Task) -> Unit)? = null
    var onDeleteClickListener: ((Task) -> Unit)? = null

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(task: Task) {
            // C'est ici qu'on reliera les données et les listeners une fois l'adapteur implémenté
            itemView.task_title.text = task.title
            itemView.task_description.text = task.description

            val deleteButton = itemView.findViewById<ImageButton>(R.id.deleteButton)
            val editButton = itemView.findViewById<ImageButton>(R.id.editButton)

            editButton?.setOnClickListener { itemView ->
                onEditClickListener?.invoke(task)
            }
            deleteButton?.setOnClickListener { itemView ->
                onDeleteClickListener?.invoke(task)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(taskList[position])
    }
}