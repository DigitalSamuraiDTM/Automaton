package com.digitalsamurai.automaton.sample.domain

import android.graphics.Bitmap
import com.digitsamurai.algos.BitmapGenerator
import javax.inject.Inject

class GenerateBilinearImageUseCase @Inject constructor(
    private val bitmapGenerator: BitmapGenerator,
) {
    suspend operator fun invoke(
        size: BitmapGenerator.Size,
        bilinearConfig: BitmapGenerator.BilinearConfig.Matrix,
    ): Bitmap {
        return bitmapGenerator.bilinearBitmap(size, bilinearConfig)
    }
}