package ipp.estg.cmu_09_8220169_8220307_8220337.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Suspends until the LiveData has a non-null value.
 * Then returns the value.
 * @return the value from the LiveData
 * @throws InterruptedException
 * @throws CancellationException if the coroutine was cancelled while waiting
 * @throws TimeoutCancellationException if the specified timeout is exceeded
 * @throws IllegalArgumentException if the value is never set
 * @sample samples.lifecycle.AwaitSample.awaitValue
 *
 */
suspend fun <T> LiveData<T>.awaitValue(): T? {
    return withContext(Dispatchers.Main) {
        var data: T? = null
        val latch = java.util.concurrent.CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(value: T) {
                data = value
                latch.countDown()
                this@awaitValue.removeObserver(this)
            }
        }
        this@awaitValue.observeForever(observer)
        latch.await()
        return@withContext data
    }
}