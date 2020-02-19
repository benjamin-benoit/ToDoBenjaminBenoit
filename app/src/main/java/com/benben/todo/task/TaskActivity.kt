package com.benben.todo.task

import Task
import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.benben.todo.R
import java.io.Serializable
import java.util.*


class TaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        val fab = findViewById<Button>(R.id.buttonAddTask)
        val title = findViewById<EditText>(R.id.editTextTitle)
        val description = findViewById<EditText>(R.id.editTextDescription)

        val task: Task? = intent?.getSerializableExtra("task") as? Task
        var id = UUID.randomUUID().toString()

        if(task != null){
            title.setText(task.title)
            description.setText(task.description)
            id = task.id
        }

        fab?.setOnClickListener {
            val myTask = Task(id = id, title = title.text.toString(), description = description.text.toString())
            intent.putExtra("task", myTask as Serializable)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}