package com.benben.todo.network

import Task
import retrofit2.Response
import retrofit2.http.GET

interface TasksWebService {
    @GET("tasks")
    suspend fun getTasks(): Response<List<Task>>
}