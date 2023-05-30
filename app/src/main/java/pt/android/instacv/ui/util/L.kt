package pt.android.instacv.ui.util

import android.util.Log

class L {
    companion object {
        const val ENABLED = true

        fun v(tag: String, msg: String, tr: Throwable? = null) {
            if (ENABLED) { tr?.let { Log.v(tag, msg, tr) } ?: Log.v(tag, msg) }
        }
        fun d(tag: String, msg: String, tr: Throwable? = null) {
            if (ENABLED) { tr?.let { Log.d(tag, msg, tr) } ?: Log.d(tag, msg) }
        }
        fun i(tag: String, msg: String, tr: Throwable? = null) {
            if (ENABLED) { tr?.let { Log.i(tag, msg, tr) } ?: Log.i(tag, msg) }
        }
        fun w(tag: String, msg: String, tr: Throwable? = null) {
            if (ENABLED) { tr?.let { Log.w(tag, msg, tr) } ?: Log.w(tag, msg) }
        }
        fun e(tag: String, msg: String, tr: Throwable? = null) {
            if (ENABLED) { tr?.let { Log.e(tag, msg, tr) } ?: Log.e(tag, msg) }
        }
    }
}