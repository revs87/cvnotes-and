package pt.rvcoding.cvnotes.domain.util

import android.util.Base64


fun String.sha256(): String = Base64.encodeToString(this.toByteArray(), Base64.NO_WRAP)

