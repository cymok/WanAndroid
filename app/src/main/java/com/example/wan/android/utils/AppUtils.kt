package com.example.wan.android.utils

import com.blankj.utilcode.util.SPUtils

object AppUtils {

    fun isAcceptAgreement(): Boolean {
        return SPUtils.getInstance().getBoolean("ACCEPT_AGREEMENT", false)
    }

    fun acceptAgreement(accept: Boolean) {
        SPUtils.getInstance().put("ACCEPT_AGREEMENT", accept)
    }

    /**
     * 比较 x.xx.x 这类,
     * 字符串可带非数字, 即 v1.4 是合法的
     * 不限制子版本的数字位数, 即 1.10 和 1.4 是可比较的
     * @return 示例: 1.4 > 1.3, 1.3.9 < 1.4, 1.10 > 1.4, 1.4 == 1.4.0
     */
    fun String.versionNewerThen(another: String): Boolean {

        val list = this.split(Regex("\\D+")).mapNotNull { it.toIntOrNull() }
        val list2 = another.split(Regex("\\D+")).mapNotNull { it.toIntOrNull() }

        // 比较相同长度
        for (i in 0 until minOf(list.size, list2.size)) {
            return if (list[i] > list2[i]) {
                // 大
                true
            } else if (list[i] < list2[i]) {
                // 小
                false
            } else {
                // 相对
                continue
            }
        }

        // 相同长度相对 比较超出长度
        if (list.size > list2.size) {
            for (i in list.subList(list2.size, list.size)) {
                if (i > 0) {
                    // 大
                    return true
                }
            }
        } else {
            for (i in list2.subList(list.size, list2.size)) {
                if (i > 0) {
                    // 小
                    return false
                }
            }
        }

        // 相等
        return false
    }

}