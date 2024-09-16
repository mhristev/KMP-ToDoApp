package org.livewall.todoapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform