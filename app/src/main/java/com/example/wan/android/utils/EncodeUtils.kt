package com.example.wan.android.utils

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

fun String.encrypt(key: String): String {
    // 1. 生成密钥
    val secretKey = generateKey(key)
    val cipher = Cipher.getInstance("AES")
    cipher.init(Cipher.ENCRYPT_MODE, secretKey)
    // 2. 把字符串转为字节数组
    val toByteArray = this.toByteArray(Charsets.UTF_8)
    // 3. 使用 AES 加密字节数组
    val encryptedBytes = cipher.doFinal(toByteArray)
    // 4. 使用 Base64 把字节数组编码成字符串，以便存储或传输
    return Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
}

fun String.decrypt(key: String): String {
    // 1. 生成密钥
    val secretKey = generateKey(key)
    val cipher = Cipher.getInstance("AES")
    cipher.init(Cipher.DECRYPT_MODE, secretKey)
    // 2. 使用 Base64 把字符串解码成字节数组
    val decodeBase64 = Base64.decode(this, Base64.NO_WRAP)
    // 3. 使用 AES 解密字节数组
    val decryptedBytes = cipher.doFinal(decodeBase64)
    // 4. 把字节数组还原字符串
    return String(decryptedBytes, Charsets.UTF_8)
}

// 生成一个 16 字节的密钥
private fun generateKey(key: String): SecretKey {
    val keyBytes = key.toByteArray(Charsets.UTF_8).copyOf(16) // AES 要求密钥长度为 16 字节
    return SecretKeySpec(keyBytes, "AES")
}
