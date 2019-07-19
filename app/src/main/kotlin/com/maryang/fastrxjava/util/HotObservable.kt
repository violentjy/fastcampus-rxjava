package com.maryang.fastrxjava.util

import android.annotation.SuppressLint
import android.util.Log
import com.maryang.fastrxjava.base.BaseApplication.Companion.TAG
import io.reactivex.Observable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.AsyncSubject
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject


object HotObservable {

    fun coldHotObservable() {
        // Observable은 subscribe을 한다.
        // subscribe에는 observer를 등록한다.
        // subscribe를 할 때 이벤트를 발행한다.
        // 이벤트를 발행하면 observer의 onNext (onSuccess) 를 실행한다.
        Observable.just(true)
            .subscribe(object : DisposableObserver<Boolean>() {
                override fun onComplete() {

                }

                override fun onNext(t: Boolean) {

                }

                override fun onError(e: Throwable) {

                }
            })

        // subject는 내부에 list<Observer>가 있다.
        // subscribe를 하면 list.add(observer)
        // onNext를 하면 list.forEach{ observer.onNext() }
        val subject = PublishSubject.create<Int>()
        subject.subscribe() // observable
        subject.onNext(1) // observer
    }

    @SuppressLint("CheckResult")
    fun logConnectableObservable() {
        var count = 0
        val observable = Observable
            .range(0, 3)
            .timestamp()
            .map { timestamped ->
                Log.d(
                    TAG,
                    "_____________연산__________ ${Thread.currentThread()}"
                )
                String.format("[%d] %d", timestamped.value(), timestamped.time())
            }
            .doOnNext { value -> count++ }
            .publish()

        observable.subscribe { value ->
            try {
                Thread.sleep(700)
                Log.d(TAG, "subscriber1 : $value ${Thread.currentThread()}")
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        observable.subscribe { value ->
            try {
                Thread.sleep(10)
                Log.d(TAG, "subscriber2 : $value ${Thread.currentThread()}")
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        observable.connect()

        Thread.sleep(100)

        observable.subscribe { value ->
            try {
                Thread.sleep(10)
                Log.d(TAG, "subscriber3 : $value")
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    @SuppressLint("CheckResult")
    fun logAsyncSubject() {
        val subject = AsyncSubject.create<Int>()
        subject.observeOn(Schedulers.io()).subscribe {
            Log.d(TAG, "AsyncSubject subscriber1 value $it")
        }
        subject.onNext(1)
        subject.observeOn(Schedulers.io()).subscribe {
            Log.d(TAG, "AsyncSubject subscriber2 value $it")
        }
        subject.onNext(2)
        subject.onNext(3)
        subject.onComplete()
    }

    @SuppressLint("CheckResult")
    fun logPublishSubject() {
        val subject = PublishSubject.create<Int>()
        subject.observeOn(Schedulers.io()).subscribe {
            Log.d(TAG, "PublishSubject subscriber1 value $it")
        }
        subject.onNext(1)
        subject.observeOn(Schedulers.io()).subscribe {
            Log.d(TAG, "PublishSubject subscriber2 value $it")
        }
        subject.onNext(2)
        subject.onNext(3)

        // subscriber1 1
        // subscriber1 2
        // subscriber2 2
        // subscriber1 3
        // subscriber2 3
    }

    @SuppressLint("CheckResult")
    fun logBehaviorSubject() {
        val subject = BehaviorSubject.create<Int>()
        subject.onNext(0)
        subject.subscribe {
            Log.d(TAG, "BehaviorSubject subscriber1 value $it")
        }
        subject.onNext(1)
        subject.subscribe {
            Log.d(TAG, "BehaviorSubject subscriber2 value $it")
        }
        subject.onNext(3)
        subject.onNext(4)

        // subscriber1 0
        // subscriber1 1
        // subscriber2 1
        // subscriber1 3
        // subscriber2 3
        // subscriber1 4
        // subscriber2 4
    }

    @SuppressLint("CheckResult")
    fun logReplaySubject() {
        val subject = ReplaySubject.create<Int>()
        subject.onNext(0)
        subject.subscribe {
            Log.d(TAG, "ReplaySubject subscriber1 value $it")
        }
        subject.onNext(1)
        subject.subscribe {
            Log.d(TAG, "ReplaySubject subscriber2 value $it")
        }
        subject.onNext(3)
        subject.onNext(4)

        // subscriber1 0
        // subscriber1 1
        // subscriber2 0
        // subscriber2 1
        // subscriber1 3
        // subscriber2 3
        // subscriber1 4
        // subscriber2 4
    }
}
