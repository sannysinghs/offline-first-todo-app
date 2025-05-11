package com.tixeon.todos.android.presentation.tasks

import com.tixeon.todos.android.data.local.entity.TaskEntity
import com.tixeon.todos.android.domain.model.Task
import com.tixeon.todos.android.domain.usecases.AddTaskUseCase
import com.tixeon.todos.android.domain.usecases.CompleteTaskUseCase
import com.tixeon.todos.android.domain.usecases.GetAllTasksUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModelTest {

    private val fakeTaskRepository = FakeTaskRepositoryImpl()

    private val getAllTasksUseCase = GetAllTasksUseCase(fakeTaskRepository)
    private val completeTaskUseCase = CompleteTaskUseCase(fakeTaskRepository)
    private val addTaskUseCase = AddTaskUseCase(fakeTaskRepository)
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: TaskViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when fetching tasks succeeds, should emit success state with tasks`() = runTest {
        // Given
        fakeTaskRepository.fakeSuccess(listOf(FAKE_TASK_ENTITY))

        // When
        setupViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.viewStateFlow.value
        assertTrue(state is TaskViewState.Success)
    }

    private fun setupViewModel() {
        viewModel = TaskViewModel(
            getAllTasks = getAllTasksUseCase,
            completeTaskUseCase = completeTaskUseCase,
            addTaskUseCase = addTaskUseCase
        )
    }

    companion object {
        val FAKE_TASK_ENTITY =
            TaskEntity(
                localId = "123",
                remoteId = "local_123",
                title = "title",
                description = "description",
                dueDate = 1234567890,
                creationDate = 1234567890,
                image = "image",
                isCompleted = false,
                dependencies = "1,2,3"
            )

        // Fake Task objects
        private val fakeTask1 = Task(
            id = "123",
            title = "title",
            description = "description",
            isCompleted = false,
            image = "",
            dueDate = "1234567890",
            createdDate = "1234567890",
            isCompletable = false,
            dependencies = listOf(1,2,3)
        )
    }
} 