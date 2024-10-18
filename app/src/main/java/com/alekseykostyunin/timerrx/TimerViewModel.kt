package com.alekseykostyunin.timerrx

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.TimeUnit

class TimerViewModel : ViewModel() {
    private val _timer = MutableStateFlow<Long?>(null)
    val timer: StateFlow<Long?> = _timer

    private var disposable: Disposable? = null

    fun startTimer() {
        if (disposable != null && !disposable!!.isDisposed) {
            return
        }

        disposable = Observable.interval(1, TimeUnit.SECONDS)
            .doOnNext { _timer.value = it }
            .doOnComplete { _timer.value = null }
            .subscribe()
    }

    fun cancelTimer() {
        _timer.value = 0
        disposable?.dispose()
    }
}

//class TimerViewModel : ViewModel() {
//    private val _timers = MutableStateFlow<MutableMap<String, Long?>>(mutableMapOf())
//    val timers: StateFlow<Map<String, Long?>> = _timers
//
//    private var disposables: MutableMap<String, Disposable> = mutableMapOf()
//
//    fun startTimer(timerId: String, duration: Long) {
//        if (disposables.containsKey(timerId) && !disposables[timerId]!!.isDisposed) {
//            return
//        }
//
//        disposables[timerId] = Observable.interval(1, TimeUnit.SECONDS)
//            .take(duration)
//            .doOnNext { _timers.value[timerId] = it }
//            .doOnComplete { _timers.value[timerId] = null }
//            .subscribe()
//    }
//
//    fun cancelTimer(timerId: String) {
//        _timers.value[timerId] = 0
//        disposables[timerId]?.dispose()
//        disposables.remove(timerId)
//    }
//}