package com.tixeon.todos.android.presentation.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tixeon.todos.android.data.local.entity.TaskEntity
import com.tixeon.todos.android.domain.model.Task
import com.tixeon.todos.android.domain.usecases.CompleteTaskUseCase
import com.tixeon.todos.android.domain.usecases.GetAllTasksUseCase
import com.tixeon.todos.android.util.DispatcherProvider
import com.tixeon.todos.android.util.Resource
import com.tixeon.todos.android.util.toDateString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val getAllTasks: GetAllTasksUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    private val _viewStateFlow: MutableStateFlow<TaskViewState> =
        MutableStateFlow(TaskViewState.Loading)

    private val _actionStateFlow: MutableStateFlow<ToggleTaskCompleteViewState> =
        MutableStateFlow(ToggleTaskCompleteViewState.None)

    val viewStateFlow = _viewStateFlow.asStateFlow()
    val toggleTaskCompleteViewStateStateFlow = _actionStateFlow.asStateFlow()

    init {
        requestTasks()
    }

    private fun requestTasks() {
        viewModelScope.launch {
            fetchRequests()
        }
    }

    fun toggleCompleteTask(task: Task) = viewModelScope.launch {
        completeTaskUseCase(task.copy(isCompleted = !task.isCompleted))
            .flowOn(dispatcherProvider.io())
            .collectLatest { res ->
                when (res) {
                    is Resource.Error ->
                        _actionStateFlow.value = ToggleTaskCompleteViewState.Error(res.message)

                    else -> {
                        // do nothing
                    }
                }
            }
    }

    private suspend fun fetchRequests() = getAllTasks()
        .flowOn(dispatcherProvider.io())
        .collectLatest { res ->
            val taskViewState = when (res) {
                is Resource.Success -> {
                    TaskViewState.Success(mapToViewState(res.data))
                }

                is Resource.Error -> {
                    TaskViewState.Error(
                        res.message
                    )
                }
            }

            _viewStateFlow.value = taskViewState
        }

    private suspend fun mapToViewState(res: List<TaskEntity>) = coroutineScope {
        val map = res.map { task ->
            val dependencies = if (task.dependencies.isNotEmpty()) {
                task.dependencies.split(",").map { it.toLong() }
            } else {
                emptyList()
            }

            val hasDependenciesResolved = true

            Task(
                title = task.title,
                description = task.description,
                dueDate = task.dueDate?.toDateString(),
                createdDate = task.creationDate.toDateString(),
                id = task.id,
                image = task.image,
                isCompletable = hasDependenciesResolved,
                isCompleted = task.isCompleted,
                dependencies = dependencies
            )
        }

        return@coroutineScope with(map) {
            TaskResult(
                allTasks = sortedBy { it.createdDate },
                upcomingTasks = filterNot { it.isCompleted }.sortedBy { it.dependencies.size }
            )
        }
    }
}