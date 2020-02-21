package com.benben.todo.tasklist

import Task
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benben.todo.task.TasksRepository
import kotlinx.coroutines.launch

class TaskListViewModel : ViewModel() {
    private val taskRepository = TasksRepository()
    private val _taskList = MutableLiveData<List<Task>>()
    public val taskList: LiveData<List<Task>> = _taskList

    private fun getMutableList() = _taskList.value.orEmpty().toMutableList()

    fun loadTasks() {
        viewModelScope.launch {
            val newList = taskRepository.refresh()
            _taskList.value = getMutableList().apply {
                clear()
                newList?.let { addAll(it) }
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            if (!taskRepository.deleteTask(task)) return@launch
            _taskList.value = getMutableList().apply {
                remove(task)
            }
        }
    }

    fun addTask(task: Task) {

        viewModelScope.launch {
            val editedTask = taskRepository.createTask(task) ?: return@launch
            _taskList.value = getMutableList().apply {
                add(editedTask)
            }
        }
    }

    fun editTask(task: Task) {
        viewModelScope.launch {
            val editedTask = taskRepository.updateTask(task) ?: return@launch
            _taskList.value = getMutableList().apply {
                val position = indexOfFirst { editedTask.id == it.id }
                set(position, editedTask)
            }
        }
    }
}