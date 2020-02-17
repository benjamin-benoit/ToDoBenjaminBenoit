package com.benben.todo.task

import Task
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.benben.todo.network.Api
import com.benben.todo.network.Api.taskWebService

class TasksRepository {
    private val tasksWebService = Api.taskWebService

    private val _taskList = MutableLiveData<List<Task>>()
    public val taskList: LiveData<List<Task>> = _taskList

    suspend fun refresh() {
        val tasksResponse = taskWebService.getTasks()
        if (tasksResponse.isSuccessful) {
            val fetchedTasks = tasksResponse.body()
            _taskList.postValue(fetchedTasks)
        }
    }
}