package com.kay.pomodoro.viewModels

import android.app.Application
import android.content.Context
import android.os.CountDownTimer
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kay.pomodoro.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PomodoroViewModel(val app:Application) : AndroidViewModel(app){

    private val minute: Long = 60_000L
    private val second: Long = 1_000L
    private val _elapsedTime = MutableLiveData<Long>()
    val elapsedTime: LiveData<Long>
        get() = _elapsedTime

    private var prefs =
        app.getSharedPreferences("com.kay.pomodoro", Context.MODE_PRIVATE)


    private val timerLengthOptions: IntArray = app.resources.getIntArray(R.array.minutes_array)

    private val _timeSelection = MutableLiveData<Int>()
    val timeSelection: LiveData<Int>
        get() = _timeSelection

    private var _alarmOn = MutableLiveData(true)
    val isAlarmOn: LiveData<Boolean>
        get() = _alarmOn

    fun updateTimeSelection(time: Int) {
        _timeSelection.value = time
    }

    fun setAlarm() {
        when (_alarmOn.value) {
            true -> {
                _alarmOn.value = false
                timeSelection.value?.let {
                    startTimer(it)
                    Log.d("start_timer",it.toString())
                }
            }
            false -> {
                _alarmOn.value = true
                //cancelNotification()
            }
        }
    }

    private fun startTimer(timer_length_selection: Int) {
        val selectedInterval = when (timer_length_selection) {
            0 -> second * 10 //For testing only
            else ->timerLengthOptions[timer_length_selection] * minute
        }

        val triggerTime = SystemClock.elapsedRealtime() + selectedInterval
        viewModelScope.launch {
            saveTime(triggerTime)
        }
        createTimer()

    }

    private val TRIGGER_TIME = "TRIGGER_AT"
    private suspend fun saveTime(triggerTime: Long) =
        withContext(Dispatchers.IO) {
            Log.d("trigger_time",triggerTime.toString())
            prefs.edit().putLong(TRIGGER_TIME, triggerTime).apply()
        }

    private suspend fun loadTime(): Long =
        withContext(Dispatchers.IO) {
            prefs.getLong(TRIGGER_TIME, 0)
        }

    /**
     * Resets the timer on screen and sets alarm value false
     */
    private fun resetTimer() {
        timer.cancel()
        _elapsedTime.value = 0
        _alarmOn.value = false
    }


    /**
     * Creates a new timer
     */

    private lateinit var timer: CountDownTimer
    private fun createTimer() {
        viewModelScope.launch {
            val triggerTime = loadTime()
            timer = object : CountDownTimer(triggerTime, second) {
                override fun onTick(millisUntilFinished: Long) {
                    _elapsedTime.value = triggerTime - SystemClock.elapsedRealtime()
                    if (_elapsedTime.value!! <= 0) {
                        resetTimer()
                    }
                }

                override fun onFinish() {
                    resetTimer()
                }
            }
            timer.start()
        }
    }


}