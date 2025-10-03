package io.github.pavelshe11.messengermicro.api.client.grpc

import io.github.pavelshe11.messengermicro.grpc.ParticipantServiceGrpc
import io.github.pavelshe11.messengermicro.grpc.findByRefIdAndParticipantTypeProto
import org.springframework.stereotype.Service
import java.util.*

@Service
class ParticipantGrpcService(
    private val stub: ParticipantServiceGrpc.ParticipantServiceBlockingStub
) {

    fun existsByRefIdAndParticipantType(refId: UUID, participantType: String): Boolean {
        val request = findByRefIdAndParticipantTypeProto.existsByRefIdAndParticipantTypeRequest.newBuilder()
            .setRefId(refId.toString())
            .setParticipantType(participantType)
            .build()

        val response = stub.findByRefIdAndParticipantType(request)
        return response.existingEntity
    }
}

