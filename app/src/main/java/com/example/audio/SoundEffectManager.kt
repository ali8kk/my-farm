package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sin

/**
 * Procedural synth audio sound effect manager for playing satisfying, lightweight
 * game audio cues (harvesting, collection, coins, feedback) without requiring bulky asset files.
 */
object SoundEffectManager {
    private val scope = CoroutineScope(Dispatchers.Default)

    /**
     * Plays a pleasant, bright dual-tone chime for crop harvesting.
     */
    fun playHarvestSound() {
        scope.launch {
            try {
                generateAndPlayChime(
                    sampleRate = 44100,
                    notes = listOf(587.33, 880.0, 1174.66), // D5 -> A5 -> D6 harmonic synth chime
                    durationMs = 100
                )
            } catch (_: Throwable) {
                // Graceful fallback if audio device is unavailable on emulator
            }
        }
    }

    /**
     * Synthesizes smooth sine-wave tones in PCM 16-bit format and streams to AudioTrack.
     */
    private suspend fun generateAndPlayChime(sampleRate: Int, notes: List<Double>, durationMs: Int) {
        try {
            val totalSamples = (sampleRate * (durationMs / 1000.0)).toInt() * notes.size
            if (totalSamples <= 0) return
            val audioData = ShortArray(totalSamples)
            val noteSamples = (sampleRate * (durationMs / 1000.0)).toInt()

            var sampleIndex = 0
            for (freq in notes) {
                for (i in 0 until noteSamples) {
                    if (sampleIndex >= audioData.size) break
                    val t = i.toDouble() / sampleRate
                    // Sine wave synthesis with exponential decay envelope
                    val envelope = 1.0 - (i.toDouble() / noteSamples)
                    val sampleValue = (sin(2.0 * Math.PI * freq * t) * envelope * 0.35 * Short.MAX_VALUE).toInt()
                    audioData[sampleIndex++] = sampleValue.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }
            }

            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(audioData.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(audioData, 0, audioData.size)
            audioTrack.play()

            // Release after playback finishes
            delay(notes.size * durationMs.toLong() + 50)
            try {
                audioTrack.stop()
                audioTrack.release()
            } catch (_: Throwable) {
                // ignore
            }
        } catch (_: Throwable) {
            // ignore
        }
    }
}
