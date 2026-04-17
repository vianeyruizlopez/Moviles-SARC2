package com.williamsel.sarc.core.hardware.data

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.VibrationEffect
import android.os.Vibrator
import com.williamsel.sarc.core.hardware.domain.model.GestorSonido
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AndroidGestorSonido @Inject constructor(
    @ApplicationContext private val context: Context
) : GestorSonido {

    private val generadorTonos = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100)

    private val vibrador = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    override fun sonarNavegacion() {
        generadorTonos.startTone(ToneGenerator.TONE_PROP_BEEP, 150)
        vibrador.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
    }


}