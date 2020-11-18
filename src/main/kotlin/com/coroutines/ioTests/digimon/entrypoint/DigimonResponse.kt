package com.coroutines.ioTests.digimon.entrypoint

import com.coroutines.ioTests.digimon.model.Digimon

data class DigimonResponse(
        val executionTime: Long,
        val digimonsDetails: List<List<Digimon>?>,
) {
}