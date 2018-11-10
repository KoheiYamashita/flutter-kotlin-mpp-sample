package info.kitproject.flutterkotlin.common

actual class Counter {
    private var count = 0
    actual fun load(): Int {
        return count
    }

    actual fun increment(): Int {
        return ++count
    }
}
