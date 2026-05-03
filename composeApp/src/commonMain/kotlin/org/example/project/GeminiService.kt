package org.example.project

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class GeminiRequest(
    val contents: List<Content>,
    val systemInstruction: Content? = null
)

@Serializable
data class Content(
    val parts: List<Part>
)

@Serializable
data class Part(
    val text: String
)

@Serializable
data class GeminiResponse(
    val candidates: List<Candidate>? = null,
    val error: GeminiError? = null
)

@Serializable
data class Candidate(
    val content: Content
)

@Serializable
data class GeminiError(
    val message: String
)

class GeminiService {
    // API Key Anda tetap sama
    private val apiKey = "AIzaSyCXL4yNrlhhIjiYl1w0ycNcvG_PYyvqOH8"

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
    }

    suspend fun summarize(text: String): Result<String> {
        return try {
            val response: GeminiResponse = client.post("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent") {
                url { parameters.append("key", apiKey) }
                contentType(ContentType.Application.Json)
                setBody(
                    GeminiRequest(
                        contents = listOf(Content(parts = listOf(Part(text = text)))),
                        systemInstruction = Content(
                            parts = listOf(
                                Part(
                                    text = "You are a highly efficient content summarizer. " +
                                            "Provide a concise, clear, and professional summary in Indonesian. " + // Tambahan: Agar ringkasan dalam Bahasa Indonesia
                                            "Maintain key points and use bullet points for long content."
                                )
                            )
                        )
                    )
                )
            }.body()

            // Cek jika ada error dari server Gemini
            if (response.error != null) {
                return Result.failure(Exception(response.error.message))
            }

            val summary = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            if (!summary.isNullOrBlank()) {
                Result.success(summary)
            } else {
                Result.failure(Exception("Gemini returned an empty response."))
            }
        } catch (e: Exception) {
            // Menangkap error koneksi atau parsing
            Result.failure(e)
        }
    }
}