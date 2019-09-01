package com.maryang.fastrxjava.data.source

import io.reactivex.observers.DisposableSingleObserver

abstract class DefaultSingleObserver<T>: DisposableSingleObserver<T>() {
    override fun onError(e: Throwable) {
        e.printStackTrace()
    }
}