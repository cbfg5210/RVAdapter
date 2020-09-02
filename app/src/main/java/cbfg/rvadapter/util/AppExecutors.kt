package cbfg.rvadapter.util

import android.os.Handler
import android.os.Looper

import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Global executor pools for the whole application.
 * Grouping tasks like this avoids the effects of task starvation (e.g. disk reads don't wait behind
 * webservice requests).
 */
internal class AppExecutors private constructor() {
    private val diskIO: Executor
    private val mainThread: Executor

    private object SingletonHolder {
        val INSTANCE = AppExecutors()
    }

    init {
        this.diskIO = Executors.newSingleThreadExecutor()
        this.mainThread = MainThreadExecutor()
    }

    fun diskIO(): Executor {
        return diskIO
    }

    fun mainThread(): Executor {
        return mainThread
    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }

    companion object {
        fun get() = SingletonHolder.INSTANCE
    }
}