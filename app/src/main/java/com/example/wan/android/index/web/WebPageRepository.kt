package com.example.wan.android.index.web

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.wan.android.data.model.History
import com.example.wan.android.data.model.WebPage
import com.example.wan.android.utils.fromJson
import com.example.wan.android.utils.logd
import com.example.wan.android.utils.loge
import com.example.wan.android.utils.toJson
import kotlinx.coroutines.flow.catch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WebPageRepository(private val dataStore: DataStore<Preferences>) {

    companion object {
        val HISTORY_KEY = stringPreferencesKey("history")
    }

    private val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())

    suspend fun updateWebPage(url: String, title: String) {
        val time = dateFormat.format(Date())
        val webPage = WebPage(url, title, time)
        try {
            dataStore.edit { data ->
//                data.clear()
//                return@edit
                // 读取当前的 WebPage 列表
                val json = data[HISTORY_KEY]
                val history = fromJson<History>(json)
                val currentList = history?.pageList ?: emptyList()
                logd(
                    "WebPageHistory: currentList = ${
                        currentList.map {
                            "${it.time} [${it.title}](${it.url})"
                        }.toJson()
                    }"
                )

                // 过滤掉指定对象并插入到前面
                val updatedList = currentList.filterNot { it.url == webPage.url }
                    .toMutableList()
                    .apply {
                        add(0, webPage)
                    }
                logd(
                    "WebPageHistory: updatedList = ${
                        updatedList.map {
                            "${it.time} [${it.title}](${it.url})"
                        }.toJson()
                    }"
                )

                // 保存新的列表
                val newHistory = History(pageList = updatedList)
                data[HISTORY_KEY] = newHistory.toJson() ?: ""
            }
        } catch (e: Exception) {
            loge(e)
        }
    }

    // 写入数据
    suspend fun removeWebPageByUrl(url: String) {
        dataStore.edit { data ->
            // 读取当前的 WebPage 列表
            val json = data[HISTORY_KEY]
            val history = fromJson<History>(json)
            val currentList = history?.pageList ?: emptyList()
            logd(
                "WebPageHistory: currentList = ${
                    currentList.map {
                        "${it.time} [${it.title}](${it.url})"
                    }.toJson()
                }"
            )

            // 过滤掉指定对象
            val updatedList = currentList.filterNot { it.url == url }
            logd(
                "WebPageHistory: updatedList = ${
                    updatedList.map {
                        "${it.time} [${it.title}](${it.url})"
                    }.toJson()
                }"
            )

            // 保存新的列表
            val newHistory = History(pageList = updatedList)
            data[HISTORY_KEY] = newHistory.toJson() ?: ""
        }
    }

    // 写入数据
    suspend fun removeWebPageByTitle(title: String) {
        dataStore.edit { data ->
            // 读取当前的 WebPage 列表
            val json = data[HISTORY_KEY]
            val history = fromJson<History>(json)
            val currentList = history?.pageList ?: emptyList()
            logd(
                "WebPageHistory: currentList = ${
                    currentList.map {
                        "${it.time} [${it.title}](${it.url})"
                    }.toJson()
                }"
            )

            // 过滤掉指定对象
            val updatedList = currentList.filterNot { it.title == title }
            logd(
                "WebPageHistory: updatedList = ${
                    updatedList.map {
                        "${it.time} [${it.title}](${it.url})"
                    }.toJson()
                }"
            )

            // 保存新的列表
            val newHistory = History(pageList = updatedList)
            data[HISTORY_KEY] = newHistory.toJson() ?: ""
        }
    }

    // 读取数据 订阅模式
    suspend fun observeWebPageList(listener: (List<WebPage>) -> Unit) {
        dataStore.data
            .catch {
                loge(it)
            }
            .collect { data ->
                // 读取当前的 WebPage 列表
                val json = data[HISTORY_KEY]
                val history = fromJson<History>(json)
                val currentList = history?.pageList ?: emptyList()
                logd(
                    "WebPageHistory: currentList = ${
                        currentList.map {
                            "${it.time} [${it.title}](${it.url})"
                        }.toJson()
                    }"
                )

                listener.invoke(currentList)
            }
    }

}