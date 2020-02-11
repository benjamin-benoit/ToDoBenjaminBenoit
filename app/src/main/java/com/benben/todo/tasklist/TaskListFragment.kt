
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.benben.todo.R
import com.benben.todo.network.Api
import com.benben.todo.task.TaskActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_task_list.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.Serializable
import java.util.*


class TaskListFragment : Fragment() {

    private val taskList = mutableListOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )


    // Création:
    private val coroutineScope = MainScope()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = TaskListAdapter(taskList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val fab = view.findViewById<FloatingActionButton>(R.id.fabAdd)

        val textViewUser = view.findViewById<TextView>(R.id.textViewUser)

        //textViewUser.text = "${userInfo.firstName} ${userInfo.lastName}"
        savedInstanceState?.getParcelableArrayList<Task>("taskList")?.let { savedList ->
            taskList.clear()
            taskList.addAll(savedList)
        }
        fab?.setOnClickListener { view ->
            val intent = Intent(context, TaskActivity::class.java)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
            //intent.getStringExtra("title")
            //intent.getStringExtra("description")
            //taskList.add(Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}"))
            //recyclerView.adapter?.notifyDataSetChanged()
        }
        adapter.onEditClickListener  = { task ->
            val intent = Intent(context, TaskActivity::class.java)
            intent.putExtra("task", task as Serializable)
            startActivityForResult(intent, EDIT_TASK_REQUEST_CODE)
            recyclerView.adapter?.notifyDataSetChanged()
        }
        adapter.onDeleteClickListener  = { task ->
            taskList.remove(task)
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_TASK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val task = data!!.getSerializableExtra("task") as Task
            taskList.add(task)
            recyclerView.adapter?.notifyDataSetChanged()
        } else if (requestCode == EDIT_TASK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val task = data!!.getSerializableExtra("task") as Task
            val index = taskList.indexOfFirst {it.id == task.id}
            taskList[index] = task
            //taskList.set(taskList.indexOfFirst {it.id == task.id}, task)
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        // Utilisation:
        coroutineScope.launch {
            val userInfo = Api.userService.getInfo().body()!!
            textViewUser.text = "${userInfo.firstName} ${userInfo.lastName}"}

    }

    override fun onDestroy() {
        super.onDestroy()
        // Suppression dans onDestroy():
        coroutineScope.cancel()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("taskList", taskList as ArrayList<Task>)
    }

    companion object {
        const val ADD_TASK_REQUEST_CODE = 777
        const val EDIT_TASK_REQUEST_CODE = 888
    }
}