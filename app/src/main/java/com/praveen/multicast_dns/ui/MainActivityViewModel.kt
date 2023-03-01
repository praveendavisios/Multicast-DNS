package com.praveen.multicast_dns.ui

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable

class MainActivityViewModel(): ViewModel()
{


    private lateinit var subscription: Disposable
    private val _registerBtn = MutableLiveData<Boolean>()
    val registerBtn: LiveData<Boolean> = _registerBtn
    private val _scanBtn = MutableLiveData<Boolean>()
    val scanBtn: LiveData<Boolean> = _scanBtn

    init
    {

    }

    fun onRegisterButtonClicked(){
        _registerBtn.postValue(true)
    }

    fun onScanButtonClicked(){
        _scanBtn.postValue(true)
    }

    override fun onCleared() {
        super.onCleared()
        if(this::subscription.isInitialized)
        {
            subscription.dispose()
        }
    }


}