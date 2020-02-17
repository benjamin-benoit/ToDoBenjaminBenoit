
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.benben.todo.R
import com.benben.todo.task.TaskActivity
import com.benben.todo.tasklist.TaskListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_task_list.*
import java.io.Serializable
import java.util.*


class TaskListFragment : Fragment() {

    var adapter = TaskListAdapter()
    private val viewModel by lazy {
        ViewModelProvider(this).get(TaskListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val fab = view.findViewById<FloatingActionButton>(R.id.fabAdd)

        val textViewUser = view.findViewById<TextView>(R.id.textViewUser)

        viewModel.taskList.observe(this, Observer { newList ->
            adapter.taskList = newList.orEmpty()
            adapter.notifyDataSetChanged()
        })

        savedInstanceState?.getParcelableArrayList<Task>("taskList")?.let { savedList ->
            viewModel.taskList.observe(this, Observer { task ->
                adapter.taskList = task.orEmpty()
                adapter.taskList = task
                adapter.notifyDataSetChanged()
            })
        }
        fab?.setOnClickListener { view ->
            val intent = Intent(context, TaskActivity::class.java)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }
        adapter.onEditClickListener = { task ->
            val intent = Intent(context, TaskActivity::class.java)
            intent.putExtra("task", task as Serializable)
            startActivityForResult(intent, EDIT_TASK_REQUEST_CODE)
            recyclerView.adapter?.notifyDataSetChanged()
        }
        adapter.onDeleteClickListener = { task ->
            viewModel.deleteTask(task)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_TASK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val task = data!!.getSerializableExtra("task") as Task
            viewModel.addTask(task)
            recyclerView.adapter?.notifyDataSetChanged()
        } else if (requestCode == EDIT_TASK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val task = data!!.getSerializableExtra("task") as Task
            viewModel.editTask(task)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadTasks()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("taskList", viewModel.taskList as ArrayList<Task>)
    }

    companion object {
        const val ADD_TASK_REQUEST_CODE = 777
        const val EDIT_TASK_REQUEST_CODE = 888
    }
}

