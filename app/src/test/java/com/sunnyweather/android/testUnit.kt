package com.sunnyweather.android

import kotlinx.coroutines.*
import org.junit.Test

class testUnit {
    @Test
    fun hello() {
        println("hello world ")
    }

    @Test
    fun testAsync() {
        //runBlocking会保证协程作用域内的所有代码和子协程都执行完前会阻塞当前线程
        //如果直接在async后面加上await就必须要等待async中的内容全部执行完才会往下执行，负责就是一个普通的协程
        runBlocking {
            launch {
                val start = System.currentTimeMillis()
                var process1 = async {
                    delay(1000)
                    println("p1开始了吗？${System.currentTimeMillis() - start}")
                    delay(1000)
                    5 + 5
                }
                var process2 = async {
                    delay(1500)
                    println("p2开始了吗？${System.currentTimeMillis() - start}")
                    delay(1500)
                    10 + 10
                }
                delay(1000)
//                println(process1.await() + process2.await())
                val a = process1.await()
                println(a)
                val b = process2.await()
                println(b)
                println("总耗时为 ： ${System.currentTimeMillis() - start}ms")
            }
        }
    }
}