package com.corroutines.ioTests.digimon.entrypoint

import com.corroutines.ioTests.digimon.model.Digimon

data class DigimonResponse(
        val executionTime: Long,
        val digimonsDetails: List<List<Digimon>?>,
) {
}