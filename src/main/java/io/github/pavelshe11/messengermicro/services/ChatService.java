package io.github.pavelshe11.messengermicro.services;

import io.github.pavelshe11.messengermicro.api.dto.request.ChatCreationRequestDto;
import io.github.pavelshe11.messengermicro.api.dto.request.ChatDeletingRequestDto;
import io.github.pavelshe11.messengermicro.store.entities.ChatRoomEntity;
import io.github.pavelshe11.messengermicro.store.entities.ChatSendersEntity;
import io.github.pavelshe11.messengermicro.store.entities.ParticipantEntity;
import io.github.pavelshe11.messengermicro.store.entities.ParticipantTypeEntity;
import io.github.pavelshe11.messengermicro.store.repositories.ChatRoomRepository;
import io.github.pavelshe11.messengermicro.store.repositories.ChatSendersRepository;
import io.github.pavelshe11.messengermicro.store.repositories.ParticipantRepository;
import io.github.pavelshe11.messengermicro.store.repositories.ParticipantTypeRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ChatService {
    private static Logger log = LoggerFactory.getLogger(ChatService.class);
    private final ChatRoomRepository chatRoomRepository;
    private final ParticipantRepository participantRepository;
    private final ParticipantTypeRepository participantTypeRepository;
    private final ChatSendersRepository chatSendersRepository;


    @Transactional
    public void createChat(ChatCreationRequestDto request) {
        ChatRoomEntity chatRoom = chatRoomRepository.save(new ChatRoomEntity());

        for (ChatCreationRequestDto.ParticipantDto participantDto : request.getParticipants()) {
            Optional<ParticipantTypeEntity> participantType = participantTypeRepository
                    .findByTypeName(participantDto.getType());
            if (participantType.isEmpty()) {
                participantTypeRepository.save(
                        ParticipantTypeEntity.builder().typeName(participantDto.getType()).build()
                );
            }

            ParticipantEntity participantEntity = ParticipantEntity.builder()
                    .id(participantDto.getParticipant().getId())
                    .participantType(participantType.get())
                    .build();

            ParticipantEntity participant = findOrCreateParticipant(participantEntity);

            ChatSendersEntity chatSenders = new ChatSendersEntity();
            chatSenders.setChatRoom(chatRoom);
            chatSenders.setParticipant(participant);

            chatSendersRepository.save(chatSenders);

        }
    }

    private ParticipantEntity findOrCreateParticipant(ParticipantEntity participantForFinding) {
        Optional<ParticipantEntity> participantOpt =
                participantRepository.findById(participantForFinding.getId());

        if (participantOpt.isEmpty()) {
//            UUID refIdFromNetworking = networking.grpc.requestFindAndGetId();
            ParticipantEntity participant = new ParticipantEntity();

//            participant.setRefId(refIdFromNetworking);
            participant.setId(participantForFinding.getId());
            participant.setParticipantType(participantForFinding.getParticipantType());
            participantRepository.save(participant);
            return participant;
        } else {
            return participantOpt.get();
        }
    }

    @Transactional
    public void deleteChats(ChatDeletingRequestDto request) {

    }
}
