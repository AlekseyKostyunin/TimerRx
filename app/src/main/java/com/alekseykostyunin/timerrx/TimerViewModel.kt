package com.alekseykostyunin.timerrx

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.TimeUnit

class TimerViewModel : ViewModel() {

    private val initialTimer = mutableListOf(MyTimer(1, 0))

    private val _timers = MutableStateFlow<List<MyTimer>>(initialTimer)
    val timers: StateFlow<List<MyTimer>> = _timers

    private var disposables: MutableMap<Int, Disposable> = mutableMapOf()

    fun createNewTimer() {
        val idLastTimer = _timers.value.last().id
        val newTimer = MyTimer(idLastTimer + 1, 0)
        _timers.value += newTimer
    }

    fun startTimer(timerId: Int) {
        if (disposables.containsKey(timerId) && !disposables[timerId]!!.isDisposed) {
            return
        }

        disposables[timerId] = Observable.interval(1, TimeUnit.SECONDS)
            .doOnNext {
                _timers.value = _timers.value.map { timer ->
                    if(timer.id == timerId) {
                        timer.copy(time = it)
                    } else {
                        timer
                    }
                }
            }
            .doOnComplete {
                _timers.value = _timers.value.map { timer ->
                    timer.copy(time = 0)
                }
            }
            .subscribe()
    }

    fun cancelTimer(timerId: Int) {
        val timer = _timers.value.find { it.id == timerId }
        if (timer != null) {
            _timers.value = _timers.value.map {
                if (it.id == timerId) {
                    it.copy(time = 0)
                } else {
                    it
                }
            }
            disposables[timerId]?.dispose()
            disposables.remove(timerId)
        }
    }
}