package de.hwrberlin.sweii.tablegames.session

import java.nio.ByteBuffer
import java.util.*

class TokenGenerator {

    /**
     * Generates a random Token of fixed length 11.
     */
    fun generateToken(): String {
        val randomTokenData: ByteArray = ByteBuffer.allocate(Long.SIZE_BYTES).apply { putLong(UUID.randomUUID().mostSignificantBits) }.array()
        return Base64.getEncoder().encodeToString(randomTokenData).removeSuffix("=")
    }
}