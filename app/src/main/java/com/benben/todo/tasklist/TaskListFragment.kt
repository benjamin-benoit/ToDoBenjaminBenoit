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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.benben.todo.R
import com.benben.todo.network.Api
import com.benben.todo.task.TaskActivity
import com.benben.todo.tasklist.TaskListViewModel
import com.benben.todo.userinfo.UserInfo
import com.benben.todo.userinfo.UserInfoActivity
import com.benben.todo.userinfo.UserInfoViewModel
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_task_list.*
import kotlinx.coroutines.launch
import java.io.Serializable
import java.util.*


class TaskListFragment : Fragment() {

    var adapter = TaskListAdapter()
    private val viewModelTask by lazy {
        ViewModelProvider(this).get(TaskListViewModel::class.java)
    }
    private val viewModelUserInfo by lazy {
        ViewModelProvider(this).get(UserInfoViewModel::class.java)
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
        // val image_viewiew = view.findViewById<ImageView>(R.id.image_view)

        val textViewUser = view.findViewById<TextView>(R.id.textViewUser)
        lifecycleScope.launch {
            val userInfo = Api.userService.getInfo().body()!!
            textViewUser.text = "${userInfo.firstName} ${userInfo.lastName}"
            email.text = "${userInfo.email}"
        }

        viewModelTask.taskList.observe(this, Observer { newList ->
            adapter.taskList = newList.orEmpty()
            adapter.notifyDataSetChanged()
        })

        savedInstanceState?.getParcelableArrayList<Task>("taskList")?.let { savedList ->
            adapter.taskList = savedList
            adapter.notifyDataSetChanged()
        }

        imageView.setOnClickListener {
            val intent = Intent(context, UserInfoActivity::class.java)
            startActivityForResult(intent, EDIT_IMAGE_REQUEST_CODE)
        }
        layoutUser.setOnClickListener {
            val intent = Intent(context, UserInfoActivity::class.java)
            startActivityForResult(intent, EDIT_USER_REQUEST_CODE)
        }
        fab?.setOnClickListener {
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
            viewModelTask.deleteTask(task)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_TASK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val task = data!!.getSerializableExtra("task") as Task
            viewModelTask.addTask(task)
            recyclerView.adapter?.notifyDataSetChanged()
        } else if (requestCode == EDIT_TASK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val task = data!!.getSerializableExtra("task") as Task
            viewModelTask.editTask(task)
        } else if (requestCode == EDIT_USER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val userInfo = data!!.getSerializableExtra("userInfo") as UserInfo
            viewModelUserInfo.editUser(userInfo)
            textViewUser.text = "${userInfo.firstName} ${userInfo.lastName}"
            email.text = "${userInfo.email}"
        }
    }

    override fun onResume() {
        super.onResume()
        val glide = Glide.with(this)
        lifecycleScope.launch {
            val userInfo = Api.userService.getInfo().body()!!
            glide.load(userInfo.avatar).circleCrop().into(imageView)
        }
        viewModelTask.loadTasks()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("taskList", viewModelTask.taskList.value as? ArrayList<Task>)
    }

    companion object {
        const val ADD_TASK_REQUEST_CODE = 777
        const val EDIT_TASK_REQUEST_CODE = 888
        const val EDIT_USER_REQUEST_CODE = 666
        const val EDIT_IMAGE_REQUEST_CODE = 999
    }
}

