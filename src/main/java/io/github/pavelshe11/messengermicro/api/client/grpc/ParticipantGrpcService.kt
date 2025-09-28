package io.github.pavelshe11.messengermicro.api.client.grpc

import io.github.pavelshe11.messengermicro.grpc.ParticipantServiceGrpc
import io.github.pavelshe11.messengermicro.grpc.findByRefIdProto
import org.springframework.stereotype.Service
import java.util.*

@Service
class ParticipantGrpcService(
    private val stub: ParticipantServiceGrpc.ParticipantServiceBlockingStub
) {

    fun existsByRefId(refId: UUID): Boolean {
        val request = findByRefIdProto.existsByRefIdRequest.newBuilder()
            .setRefId(refId.toString())
            .build()

        val response = stub.findByRefId(request)
        return response.existingEntity
    }
}

