package com.tixeon.todos.android.syncs.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import com.tixeon.todos.android.data.respository.TaskRepository
import com.tixeon.todos.android.data.sync.Syncable
import com.tixeon.todos.android.util.DispatcherProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

@HiltWorker
internal class SyncWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val dispatcherProvider: DispatcherProvider,
    private val taskRepository: TaskRepository,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(dispatcherProvider.io()) {
        try {
            val syncedSuccessfully = awaitAll(
                async {
                    if (taskRepository is Syncable) taskRepository.syncs() else true
                }
            ).any { it }

            if (syncedSuccessfully) {
                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            // Handle errors appropriately, e.g., retry or log
            e.printStackTrace()
            Result.retry() // Or Result.failure() if you don't want to retry
        }
    }

    companion object {

        const val TAG = "SyncWorker"

        private val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        fun startupSyncWork() = OneTimeWorkRequestBuilder<DelegatingWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(constraints)
            .setInputData(SyncWorker::class.delegatedData())
            .build()
    }
}