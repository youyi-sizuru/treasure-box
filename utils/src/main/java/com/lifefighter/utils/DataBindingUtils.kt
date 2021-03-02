package com.lifefighter.utils

import androidx.annotation.MainThread
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

/**
 * @author xzp
 * @created on 2020/11/4.
 */
class ExLiveData<T> : MediatorLiveData<T> {
    @MainThread
    constructor(init: T) : super() {
        value = init
    }

    constructor() : super()
}

@MainThread
fun <T, R> ExLiveData<T>.map(mapper: (T?) -> R?): ExLiveData<R> {
    val result = ExLiveData<R>()
    observeForever { x -> result.value = mapper(x) }
    return result
}

@MainThread
fun <T, R> ExLiveData<T>.map(initValue: R, mapper: (T?) -> R?): ExLiveData<R> {
    val result = ExLiveData(initValue)
    observeForever { x -> result.value = mapper(x) }
    return result
}

@MainThread
fun <X, Y> ExLiveData<X>.switchMap(
    switchMapFunction: (X) -> ExLiveData<Y>
): ExLiveData<Y> {
    val result = ExLiveData<Y>()
    observeForever(object : Observer<X> {
        var mSource: ExLiveData<Y>? = null
        val ob: Observer<in Y> = Observer { result.value = it }
        val ob2: Observer<Y> = Observer {
            if (it != mSource?.value) {
                mSource?.value = it
            }
        }

        override fun onChanged(x: X) {
            val newLiveData = switchMapFunction(x)
            if (mSource === newLiveData) {
                return
            }
            if (mSource != null) {
                mSource!!.removeObserver(ob)
                result.removeObserver(ob2)
            }
            mSource = newLiveData
            if (mSource != null) {
                result.value = mSource!!.value
                mSource!!.observeForever(ob)
                result.observeForever(ob2)
            }
        }
    })
    return result
}

@MainThread
fun <T> ExLiveData<T>.distinct(): ExLiveData<T> {
    val result = ExLiveData<T>()
    var newest: T? = null
    observeForever { data: T ->
        if (data != newest) {
            newest = data
            result.value = data
        }
    }
    return result
}

@MainThread
fun <T> ExLiveData<T>.skip(skipCount: Int = 1): ExLiveData<T> {
    val result = ExLiveData<T>()
    var count = 0
    observeForever { v ->
        if (++count > skipCount) {
            result.value = v
        }
    }
    return result
}

@MainThread
fun <T1, T2, R> combine(
    liveData1: ExLiveData<T1>,
    liveData2: ExLiveData<T2>,
    combiner: (T1?, T2?) -> R?
): ExLiveData<R> {
    val result = ExLiveData<R>()
    var latestData1: T1? = liveData1.value
    var latestData2: T2? = liveData2.value
    liveData1.observeForever { data1: T1 ->
        latestData1 = data1
        result.value = combiner(data1, latestData2)
    }
    liveData2.observeForever { data2: T2 ->
        latestData2 = data2
        result.value = combiner(latestData1, data2)
    }
    return result
}

@MainThread
fun <T1, T2, T3, R> combine(
    liveData1: ExLiveData<T1>,
    liveData2: ExLiveData<T2>,
    liveData3: ExLiveData<T3>,
    combiner: (T1?, T2?, T3?) -> R?
): ExLiveData<R> {
    val result = ExLiveData<R>()
    var latestData1: T1? = liveData1.value
    var latestData2: T2? = liveData2.value
    var latestData3: T3? = liveData3.value
    liveData1.observeForever { data1: T1 ->
        latestData1 = data1
        result.value = combiner(data1, latestData2, latestData3)
    }
    liveData2.observeForever { data2: T2 ->
        latestData2 = data2
        result.value = combiner(latestData1, data2, latestData3)
    }
    liveData3.observeForever { data3: T3 ->
        latestData3 = data3
        result.value = combiner(latestData1, latestData2, data3)
    }

    return result
}

@MainThread
fun <T1, T2, T3, T4, R> combine(
    liveData1: ExLiveData<T1>,
    liveData2: ExLiveData<T2>,
    liveData3: ExLiveData<T3>,
    liveData4: ExLiveData<T4>,
    combiner: (T1?, T2?, T3?, T4?) -> R?
): ExLiveData<R> {
    val result = ExLiveData<R>()
    var latestData1: T1? = liveData1.value
    var latestData2: T2? = liveData2.value
    var latestData3: T3? = liveData3.value
    var latestData4: T4? = liveData4.value
    liveData1.observeForever { data1: T1 ->
        latestData1 = data1
        result.value = combiner(data1, latestData2, latestData3, latestData4)
    }
    liveData2.observeForever { data2: T2 ->
        latestData2 = data2
        result.value = combiner(latestData1, data2, latestData3, latestData4)
    }
    liveData3.observeForever { data3: T3 ->
        latestData3 = data3
        result.value = combiner(latestData1, latestData2, data3, latestData4)
    }
    liveData4.observeForever { data4: T4 ->
        latestData4 = data4
        result.value = combiner(latestData1, latestData2, latestData3, data4)
    }
    return result
}

@MainThread
fun <T1, T2, T3, T4, T5, R> combine(
    liveData1: ExLiveData<T1>,
    liveData2: ExLiveData<T2>,
    liveData3: ExLiveData<T3>,
    liveData4: ExLiveData<T4>,
    liveData5: ExLiveData<T5>,
    combiner: (T1?, T2?, T3?, T4?, T5?) -> R?
): ExLiveData<R> {
    val result = ExLiveData<R>()
    var latestData1: T1? = liveData1.value
    var latestData2: T2? = liveData2.value
    var latestData3: T3? = liveData3.value
    var latestData4: T4? = liveData4.value
    var latestData5: T5? = liveData5.value
    liveData1.observeForever { data1: T1 ->
        latestData1 = data1
        result.value = combiner(data1, latestData2, latestData3, latestData4, latestData5)
    }
    liveData2.observeForever { data2: T2 ->
        latestData2 = data2
        result.value = combiner(latestData1, data2, latestData3, latestData4, latestData5)
    }
    liveData3.observeForever { data3: T3 ->
        latestData3 = data3
        result.value = combiner(latestData1, latestData2, data3, latestData4, latestData5)
    }
    liveData4.observeForever { data4: T4 ->
        latestData4 = data4
        result.value = combiner(latestData1, latestData2, latestData3, data4, latestData5)
    }
    liveData5.observeForever { data5: T5 ->
        latestData5 = data5
        result.value = combiner(latestData1, latestData2, latestData3, latestData4, latestData5)
    }
    return result
}

@MainThread
fun <T1, T2, T3, T4, T5, T6, R> combine(
    liveData1: ExLiveData<T1>,
    liveData2: ExLiveData<T2>,
    liveData3: ExLiveData<T3>,
    liveData4: ExLiveData<T4>,
    liveData5: ExLiveData<T5>,
    liveData6: ExLiveData<T6>,
    combiner: (T1?, T2?, T3?, T4?, T5?, T6?) -> R?
): ExLiveData<R> {
    val result = ExLiveData<R>()
    var latestData1: T1? = liveData1.value
    var latestData2: T2? = liveData2.value
    var latestData3: T3? = liveData3.value
    var latestData4: T4? = liveData4.value
    var latestData5: T5? = liveData5.value
    var latestData6: T6? = liveData6.value
    liveData1.observeForever { data1: T1 ->
        latestData1 = data1
        result.value =
            combiner(data1, latestData2, latestData3, latestData4, latestData5, latestData6)
    }
    liveData2.observeForever { data2: T2 ->
        latestData2 = data2
        result.value =
            combiner(latestData1, data2, latestData3, latestData4, latestData5, latestData6)
    }
    liveData3.observeForever { data3: T3 ->
        latestData3 = data3
        result.value =
            combiner(latestData1, latestData2, data3, latestData4, latestData5, latestData6)
    }
    liveData4.observeForever { data4: T4 ->
        latestData4 = data4
        result.value =
            combiner(latestData1, latestData2, latestData3, data4, latestData5, latestData6)
    }
    liveData5.observeForever { data5: T5 ->
        latestData5 = data5
        result.value =
            combiner(latestData1, latestData2, latestData3, latestData4, data5, latestData6)
    }
    liveData6.observeForever { data6: T6 ->
        latestData6 = data6
        result.value =
            combiner(latestData1, latestData2, latestData3, latestData4, latestData5, data6)
    }
    return result
}


@MainThread
fun <T1, T2> combine(
    liveData1: ExLiveData<T1>,
    liveData2: ExLiveData<T2>
): ExLiveData<Pair<T1?, T2?>> {
    return combine(liveData1, liveData2) { d1, d2 -> d1 to d2 }
}

@MainThread
fun <T, R> combine(
    vararg liveDataList: ExLiveData<out T>,
    combiner: (List<T>) -> R
): ExLiveData<R> {
    val result = ExLiveData<R>()
    if (!liveDataList.any { it.value == null }) {
        result.value = combiner(liveDataList.map { it.value!! })
    }
    liveDataList.forEach { liveData ->
        liveData.observeForever {
            if (liveDataList.any { it.value == null })
                return@observeForever
            result.value = combiner(liveDataList.map { it.value!! })
        }
    }
    return result
}

@MainThread
fun <T1, T2, R> merge(
    l1: ExLiveData<T1>,
    m1: (T1) -> R,
    l2: ExLiveData<T2>,
    m2: (T2) -> R
): ExLiveData<R> {
    val result = ExLiveData<R>()
    l1.observeForever { d1 -> result.value = m1(d1) }
    l2.observeForever { d2 -> result.value = m2(d2) }
    return result
}

@MainThread
fun <T, R> merge(
    vararg pairs: Pair<ExLiveData<T>, (T) -> R>
): ExLiveData<R> {
    val result = ExLiveData<R>()
    pairs.forEach { pair ->
        val (l, m) = pair
        l.observeForever { d -> result.value = m(d) }
    }
    return result
}

@MainThread
fun <T> merge(
    vararg ls: ExLiveData<T>
): ExLiveData<T> {
    val result = ExLiveData<T>()
    ls.forEach { l ->
        l.observeForever { d -> result.value = d }
    }
    return result
}