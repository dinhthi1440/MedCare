package com.example.medcare.extension
import java.util.UUID
import kotlin.random.Random

class RandomUtil {

    companion object {

        // Random số Int trong khoảng
        fun randomInt(min: Int, max: Int): Int {
            return Random.nextInt(min, max + 1)
        }
        fun randomIDInt(): Int {
            return Random.nextInt(0, 10000 + 1)
        }

        // Random số Float trong khoảng
        fun randomFloat(min: Float, max: Float): Float {
            return Random.nextFloat() * (max - min) + min
        }

        // Random số Double trong khoảng
        fun randomDouble(min: Double, max: Double): Double {
            return Random.nextDouble(min, max)
        }

        // Random Boolean
        fun randomBoolean(): Boolean {
            return Random.nextBoolean()
        }

        // Random chuỗi
        fun randomString(length: Int): String {
            val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
            return (1..length)
                .map { allowedChars.random() }
                .joinToString("")
        }

        // Random UUID
        fun randomUUID(): String {
            return UUID.randomUUID().toString()
        }

        // Random item trong List
        fun <T> randomItem(list: List<T>): T? {
            return if (list.isNotEmpty()) list.random() else null
        }

        // Random giờ phút (trả về "HH:mm")
        fun randomTime(): String {
            val hour = randomInt(0, 23).toString().padStart(2, '0')
            val minute = randomInt(0, 59).toString().padStart(2, '0')
            return "$hour:$minute"
        }
    }
}