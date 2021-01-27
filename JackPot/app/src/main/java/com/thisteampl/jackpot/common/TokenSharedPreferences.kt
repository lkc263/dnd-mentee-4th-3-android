package com.thisteampl.jackpot.common

import android.content.Context
import android.content.SharedPreferences

// 토큰을 저장해 둘 Preferences

class TokenSharedPreferences(context: Context) {
    val PREFS_FILENAME = "prefs"
    val PREF_TEXT = "token"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)
    /* 파일 이름과 EditText를 저장할 Key 값을 만들고 prefs 인스턴스 초기화 */

    var token: String?
        get() = prefs.getString(PREF_TEXT, "")
        set(value) = prefs.edit().putString(PREF_TEXT, value).apply()
    /* get/set 함수 임의 설정. get 실행 시 저장된 값을 반환하며 default 값은 ""
     * set(value) 실행 시 value로 값을 대체한 후 저장 */
}