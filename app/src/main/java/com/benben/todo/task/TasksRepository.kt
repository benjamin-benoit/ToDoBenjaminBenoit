package com.benben.todo.task

import Task
import com.benben.todo.network.Api

class TasksRepository {
    private val tasksWebService = Api.taskWebService

    suspend fun refresh(): List<Task>? {
        val tasksResponse = tasksWebService.getTasks()
        if (tasksResponse.isSuccessful) {
            return tasksResponse.body()
        }
        return null
    }
    suspend fun updateTask(task: Task): Task? {
        val tasksResponse = tasksWebService.updateTask(task)
        return if (tasksResponse.isSuccessful) {
            tasksResponse.body()
        } else null
    }
    suspend fun createTask(task: Task): Task? {
        val tasksResponse = tasksWebService.createTask(task)
        return if (tasksResponse.isSuccessful) {
            tasksResponse.body()
        } else null

    }
    suspend fun deleteTask(task: Task): String? {
        val tasksResponse = tasksWebService.deleteTask(task.id)
        return if (tasksResponse.isSuccessful) {
            tasksResponse.body()
        } else null
    }
}