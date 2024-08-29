package com.safetymarcus.shifttracker

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform