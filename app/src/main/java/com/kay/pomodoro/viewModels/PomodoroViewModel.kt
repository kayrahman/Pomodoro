package com.kay.pomodoro.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kay.pomodoro.R

class PomodoroViewModel(val app:Application) : AndroidViewModel(app){

    private val timerLengthOptions: IntArray = app.resources.getIntArray(R.array.minutes_array)

    private val _timeSelection = MutableLiveData<Int>()
    val timeSelection: LiveData<Int>
        get() = _timeSelection

    private var _alarmOn = MutableLiveData(false)
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
                   // startTimer(it)
                    Log.d("start_timer",it.toString())
                }
            }
            false -> {
                _alarmOn.value = true
                //cancelNotification()
            }
        }
    }



}