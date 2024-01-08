package xyz.heetaeb.Woute.domain.chat.service;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import xyz.heetaeb.Woute.domain.chat.dto.ChatListResponseDTO;
import xyz.heetaeb.Woute.domain.chat.dto.ChatLogResponseDTO;
import xyz.heetaeb.Woute.domain.chat.dto.ChatRequestDTO;
import xyz.heetaeb.Woute.domain.chat.entity.ChatMsg;
import xyz.heetaeb.Woute.domain.chat.entity.JoinRoom;
import xyz.heetaeb.Woute.domain.chat.repository.ChatMsgRepository;
import xyz.heetaeb.Woute.domain.chat.repository.JoinRoomRepository;
import xyz.heetaeb.Woute.domain.user.entity.UserEntity;
import xyz.heetaeb.Woute.domain.user.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class ChatService {
	private final JoinRoomRepository joinRoomRepository;
	private final ChatMsgRepository chatMsgRepository;
	private final UserRepository userRepository;
	
	@Transactional
	public ChatListResponseDTO getList(Long id) {
		List<JoinRoom> myRooms = joinRoomRepository.findByMyUserId(id);
		UserEntity user = userRepository.findById(id).orElseThrow();

		return ChatListResponseDTO.builder()
				.myUser(user)
				.rooms(
						myRooms.stream().map(room -> ChatListResponseDTO.Room.builder()
								.toUserId(room.getToUserId())
								.toUserNick(room.getToUserNick())
								.toUserImg(room.getToUserImg())
								.roomId(room.getRoomId())
								.isRead(room.getRead())
								.lastMsg(room.getLastMsg())
								.lastMsgTime(room.getLastMsgTime())
								.build()).toList()
						)
				.build();
	}
	
	@Transactional
	public void createRoom(ChatRequestDTO dto) {
		UserEntity toUser = userRepository.findById(dto.getToUserId()).orElseThrow();
		UserEntity fromUser = userRepository.findById(dto.getMyId()).orElseThrow();
		
		ChatMsg msg = ChatMsg.builder()
				.user(fromUser)
				.content(dto.getMessage())
				.roomId(dto.getRoomId())
				.createdAt(ZonedDateTime.now())
//				.createdAt(dto.getLastMsgTime())
				.build();
		
		if(joinRoomRepository.countByRoomId(dto.getRoomId()) != 2) {
			JoinRoom myRoom = JoinRoom.builder()
					.myUserId(dto.getMyId())
					.toUserId(dto.getToUserId())
					.toUserNick(toUser.getNickname())
					.toUserImg(toUser.getProfileImage())
					.roomId(dto.getRoomId())
					.read(false)
					.lastMsg(dto.getMessage())
					.lastMsgTime(msg.getCreatedAt())
					.build();
			joinRoomRepository.save(myRoom);
			
			JoinRoom toUserRoom = JoinRoom.builder()
					.myUserId(dto.getToUserId())
					.toUserId(dto.getMyId())
					.toUserNick(fromUser.getNickname())
					.toUserImg(fromUser.getProfileImage())
					.roomId(dto.getRoomId())
					.read(false)
					.lastMsg(dto.getMessage())
					.lastMsgTime(msg.getCreatedAt())
					.build();
			joinRoomRepository.save(toUserRoom);
			chatMsgRepository.save(msg);
		} else {
			JoinRoom myRoom = joinRoomRepository.findByRoomIdAndMyUserId(dto.getRoomId(), dto.getMyId());
			JoinRoom toUserRoom = joinRoomRepository.findByRoomIdAndMyUserId(dto.getRoomId(), dto.getToUserId());
			
			JoinRoom updateMyRoom = JoinRoom.builder()
					.id(myRoom.getId())
					.myUserId(myRoom.getMyUserId())
					.toUserId(myRoom.getToUserId())
					.toUserNick(myRoom.getToUserNick())
					.toUserImg(myRoom.getToUserImg())
					.roomId(myRoom.getRoomId())
					.read(false)
					.lastMsg(dto.getMessage())
					.lastMsgTime(msg.getCreatedAt())
					.build();
			joinRoomRepository.save(updateMyRoom);
			
			JoinRoom updateUserRoom = JoinRoom.builder()
					.id(toUserRoom.getId())
					.myUserId(toUserRoom.getMyUserId())
					.toUserId(toUserRoom.getToUserId())
					.toUserNick(toUserRoom.getToUserNick())
					.toUserImg(toUserRoom.getToUserImg())
					.roomId(toUserRoom.getRoomId())
					.read(false)
					.lastMsg(dto.getMessage())
					.lastMsgTime(msg.getCreatedAt())
					.build();
			joinRoomRepository.save(updateUserRoom);
			
			chatMsgRepository.save(msg);
		}
		
		
	}

	public ChatLogResponseDTO getRoom(Long roomId) {
		List<ChatMsg> msgs = chatMsgRepository.findByRoomIdOrderByCreatedAtDesc(roomId);
		ChatLogResponseDTO messages = ChatLogResponseDTO.builder()
				.Messages(
						msgs.stream().map(msg -> ChatLogResponseDTO.Message.builder()
								.id(msg.getId())
								.userid(msg.getUser().getId())
								.profileImg(msg.getUser().getProfileImage())
								.roomId(msg.getRoomId())
								.content(msg.getContent())
								.createdAt(msg.getCreatedAt())
								.build()).toList()
						).build();
		return messages;
	}
		
}
