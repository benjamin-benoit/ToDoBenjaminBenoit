package com.benben.todo.task

import Task
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.benben.todo.network.Api

class TasksRepository {
    private val tasksWebService = Api.taskWebService

    private val _taskList = MutableLiveData<List<Task>>()
    public val taskList: LiveData<List<Task>> = _taskList

    suspend fun refresh() {
        val tasksResponse = tasksWebService.getTasks()
        if (tasksResponse.isSuccessful) {
            val fetchedTasks = tasksResponse.body()
            _taskList.postValue(fetchedTasks)
        }
    }
    suspend fun updateTask(task: Task) {
        val tasksResponse = tasksWebService.updateTask(task)
        if (tasksResponse.isSuccessful) {
            val editableList = _taskList.value.orEmpty().toMutableList()
            val position = editableList.indexOfFirst { task.id == it.id }
            editableList[position] = task
            _taskList.value = editableList
        }
    }
    suspend fun createTask(task: Task) {
        val tasksResponse = tasksWebService.createTask(task)
        if (tasksResponse.isSuccessful) {
            val editableList = _taskList.value.orEmpty().toMutableList()
            editableList.add(task)
            _taskList.value = editableList
        }
    }
    suspend fun deleteTask(task: Task) {
        val tasksResponse = tasksWebService.deleteTask(task.id)
        if (tasksResponse.isSuccessful) {
            val editableList = _taskList.value.orEmpty().toMutableList()
            editableList.remove(task)
            _taskList.value = editableList
        }
    }
}