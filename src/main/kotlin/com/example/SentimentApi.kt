
package com.example

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import ai.onnxruntime.OnnxTensor

import kotlinx.serialization.Serializable

@Serializable
data class SentimentRequest(val text: String)

@Serializable
data class SentimentResponse(val sentiment: String, val confidence: Double)

fun main() {
    embeddedServer(Netty, port = 8080) {
        routing {
            val environment = OrtEnvironment.getEnvironment()
            val modelBytes = SentimentApi::class.java.getResource("/sentiment.onnx")?.readBytes()
                ?: throw IllegalStateException("ONNX model not found.")
            val session: OrtSession = environment.createSession(modelBytes)

            post("/analyze") {
                val body = call.receive<SentimentRequest>()
                val inputs = prepareFeatures(body.text)

                val inputTensor = OnnxTensor.createTensor(environment, inputs)
                val results = session.run(mapOf("input" to inputTensor))

                val outputArray = results[0].value as Array<FloatArray>
                val (sentiment, confidence) = interpretOutput(outputArray[0])

                call.respond(SentimentResponse(sentiment, confidence))
            }

            post("/feedback") {
                val feedback = call.receive<SentimentRequest>()
                println("Received feedback for future retraining: ${feedback.text}")
                call.respond(HttpStatusCode.OK, "Thanks for your feedback!")
            }

            get("/") {
                call.respondText("Sentiment Analysis API running", ContentType.Text.Plain)
            }
        }
    }.start(wait = true)
}

fun prepareFeatures(text: String): Array<FloatArray> {
    val vector = FloatArray(128) { 0.0f }
    text.take(128).forEachIndexed { i, c -> vector[i] = c.code.toFloat() }
    return arrayOf(vector)
}

fun interpretOutput(output: FloatArray): Pair<String, Double> {
    val classes = listOf("negative", "neutral", "positive")
    val index = output.indices.maxByOrNull { output[it] } ?: 0
    val sentiment = classes[index]
    val confidence = output[index].toDouble()
    return sentiment to confidence
}
