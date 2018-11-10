package info.kitproject.flutterkotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import info.kitproject.flutterkotlin.common.Counter
import io.flutter.facade.Flutter
import io.flutter.plugin.common.MethodChannel

class MainActivity : AppCompatActivity() {
    private val channel = "info.kitproject.flutterkotlin"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val flutterView = Flutter.createView(this, lifecycle, "main")
        val layout = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        setContentView(flutterView, layout)
        val counter = Counter()
        MethodChannel(flutterView, channel).setMethodCallHandler { call, result ->
            when {
                call.method == "load" -> result.success(counter.load())
                call.method == "increment" -> result.success(counter.increment())
                else -> result.notImplemented()
            }
        }
    }
}
