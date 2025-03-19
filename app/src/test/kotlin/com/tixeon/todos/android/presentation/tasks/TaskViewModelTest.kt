package com.tixeon.todos.android.presentation.tasks

import com.tixeon.todos.android.data.local.entity.TaskEntity
import com.tixeon.todos.android.domain.model.Task
import com.tixeon.todos.android.domain.usecases.CompleteTaskUseCase
import com.tixeon.todos.android.domain.usecases.GetAllTasksUseCase
import com.tixeon.todos.android.domain.usecases.GetCompletedTasks
import com.tixeon.todos.android.util.CryptoHelper
import com.tixeon.todos.android.util.DispatcherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.eq
import java.lang.Exception

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModelTest {

    private val fakeTaskRepository = FakeTaskRepositoryImpl()

    private val getAllTasksUseCase: GetAllTasksUseCase = GetAllTasksUseCase(
        repository = fakeTaskRepository
    )
    private val completeTaskUseCase: CompleteTaskUseCase = CompleteTaskUseCase(
        repository = fakeTaskRepository
    )
    private val getCompletedTasksUseCase: GetCompletedTasks = GetCompletedTasks(
        repository = fakeTaskRepository
    )
    private val cryptoHelper: CryptoHelper = FakeCryptoHelper()
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: TaskViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `GIVEN get task use case returns tasks WHEN view model get view state THEN check if view state is updated`() =
        runTest(testDispatcher) {
            fakeTaskRepository.fakeSuccess(
                listOf(
                    FAKE_TASK_ENTITY,
                    FAKE_TASK_ENTITY.copy(id = 2, isCompleted = true)
                )
            )

            setupViewModel()

            val values = viewModel.viewStateFlow.take(2).toList()
            advanceUntilIdle()

            // Assert Success State
            assertEquals(values[0] is TaskViewState.Loading, true)

            // Assert Success State
            assertEquals(values[1] is TaskViewState.Success, true)

            assertEquals(
                2, (values[1] as TaskViewState.Success)
                    .result.allTasks.size
            )

            assertEquals(
                1, (values[1] as TaskViewState.Success)
                    .result.upcomingTasks.size
            )

        }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `GIVEN viewModel init, WHEN get task use case returns failure, THEN view state return error`() =
        runTest(testDispatcher) {
            val fakeErrorState = Exception("Error getting all tasks")
            fakeTaskRepository.fakeRetrievalError(fakeErrorState)

            setupViewModel()

            val values = viewModel.viewStateFlow.take(2).toList()

            advanceUntilIdle()

            // Assert Success State
            assertEquals(values[0], eq(TaskViewState.Loading))

            // Assert Success State
            assertEquals(eq(TaskViewState.Error(fakeErrorState.message!!)), values[1])
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `GIVEN complete task use case success, WHEN call completeTask, THEN view state return success`() =
        runTest(testDispatcher) {
            fakeTaskRepository.fakeSuccess(
                task = listOf(FAKE_TASK_ENTITY)
            )
            setupViewModel()
            viewModel.toggleCompleteTask(FAKE_TASK)
            val values = viewModel.toggleTaskCompleteViewStateStateFlow.take(3).toList()

            advanceUntilIdle()

            assertEquals(ToggleTaskCompleteViewState.None, values[0])
            assertEquals(ToggleTaskCompleteViewState.Loading, values[1])
            assertTrue(values[2] is ToggleTaskCompleteViewState.Success)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `GIVEN complete task use case failed, WHEN call completeTask, THEN view state return failure`() =
        runTest(testDispatcher) {
            val fakeErrorState = Exception("Error complete tasks")

            fakeTaskRepository.fakeRetrievalError(fakeErrorState)

            setupViewModel()
            viewModel.toggleCompleteTask(FAKE_TASK)

            val values = viewModel.toggleTaskCompleteViewStateStateFlow.take(3).toList()

            advanceUntilIdle()
            assertEquals(ToggleTaskCompleteViewState.None, values[0])
            assertEquals(ToggleTaskCompleteViewState.Loading, values[1])
            assertTrue(values[2] is ToggleTaskCompleteViewState.Error)
        }

    private fun setupViewModel() {
        viewModel = TaskViewModel(
            getAllTasks = getAllTasksUseCase,
            completeTaskUseCase = completeTaskUseCase,
            getCompletedTasks = getCompletedTasksUseCase,
            dispatcherProvider = object : DispatcherProvider {
                override fun io() = testDispatcher
                override fun main() = testDispatcher
                override fun default() = testDispatcher
            },
        )
    }


    companion object {
        val FAKE_TASK = Task(
            id = 1,
            title = "title",
            description = "description",
            isCompleted = false,
            dueDate = "1234567890",
            createdDate = "1234567890",
            image = "image",
            dependencies = listOf(1, 2, 3),
            isCompletable = true
        )

        val FAKE_TASK_ENTITY =
            TaskEntity(
                id = 1,
                title = "title",
                description = "description",
                dueDate = 1234567890,
                creationDate = 1234567890,
                image = "image",
                isCompleted = false,
                dependencies = "1,2,3"
            )
    }
}

class FakeCryptoHelper : CryptoHelper {
    override fun encrypt(data: String): String = data

    override fun decrypt(data: String): String = data
}