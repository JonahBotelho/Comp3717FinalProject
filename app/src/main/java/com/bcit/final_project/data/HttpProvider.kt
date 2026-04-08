package com.bcit.final_project.data

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.gson.gson

object HttpProvider {
    val client: HttpClient = HttpClient {
        install(ContentNegotiation) {
            gson()
        }
    }
}
