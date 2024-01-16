package xyz.heetaeb.Woute.domain.chat.service;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import xyz.heetaeb.Woute.domain.chat.dto.ChatListResponseDTO;
import xyz.heetaeb.Woute.domain.chat.dto.ChatLogResponseDTO;
import xyz.heetaeb.Woute.domain.chat.dto.ChatRequestDTO;
import xyz.heetaeb.Woute.domain.chat.dto.FindUserListDTO;
import xyz.heetaeb.Woute.domain.chat.entity.ChatMsg;
import xyz.heetaeb.Woute.domain.chat.entity.JoinRoom;
import xyz.heetaeb.Woute.domain.chat.repository.ChatMsgRepository;
import xyz.heetaeb.Woute.domain.chat.repository.JoinRoomRepository;
import xyz.heetaeb.Woute.domain.search.dto.SearchResponseDTO;
import xyz.heetaeb.Woute.domain.user.entity.UserEntity;
import xyz.heetaeb.Woute.domain.user.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class ChatService {
	private final JoinRoomRepository joinRoomRepository;
	private final ChatMsgRepository chatMsgRepository;
	private final UserRepository userRepository;
	
	// 채팅방 리스트 조회
	@Transactional
	public ChatListResponseDTO getList(Long id) {
		List<JoinRoom> myRooms = joinRoomRepository.findByMyUserIdOrderByLastMsgTime(id);
		UserEntity user = userRepository.findById(id).orElseThrow();
		return ChatListResponseDTO.builder()
				.myUser(user)
				.rooms(
						myRooms.stream().map(room -> ChatListResponseDTO.Room.builder()
								.toUserId(room.getToUserId())
								.toUserNick(userRepository.findById(room.getToUserId()).map(UserEntity::getNickname).orElse(null))
								.toUserImg(userRepository.findById(room.getToUserId()).map(UserEntity::getProfileImage).orElse(null))
								.roomId(room.getRoomId())
								.isRead(room.getRead())
								.lastMsg(room.getLastMsg())
								.lastMsgTime(room.getLastMsgTime())
								.build()).toList()
						)
				.build();
	}
	
	// 메시지 전송시 데이터 저장
	@Transactional
	public void createRoom(ChatRequestDTO dto) {
		UserEntity toUser = userRepository.findById(dto.getToUserId()).orElseThrow();
		UserEntity fromUser = userRepository.findById(dto.getMyId()).orElseThrow();
		
		// 보낸 메시지 내용
		ChatMsg msg = ChatMsg.builder()
				.user(fromUser)
				.content(dto.getMessage())
				.roomId(dto.getRoomId())
				.createdAt(ZonedDateTime.now())
				.build();
		
		// 받는 유저와 첫 대화일때 
		if(joinRoomRepository.countByRoomId(dto.getRoomId()) != 2) {
			JoinRoom myRoom = JoinRoom.builder()
					.myUserId(dto.getMyId())
					.toUserId(dto.getToUserId())
					.toUserNick(toUser.getNickname())
					.toUserImg(toUser.getProfileImage())
					.roomId(dto.getRoomId())
					.read(true)
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
			// 기존의 대화가 있을때
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
					.read(true)
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

	// 채팅로그 조회
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
		
	// 알림 읽기
	public void isRead(Long userId, Long roomId) {
		JoinRoom room = joinRoomRepository.findByRoomIdAndMyUserId(roomId, userId);
		JoinRoom updateRoom = room.builder()
				.id(room.getId())
				.myUserId(room.getMyUserId())
				.toUserId(room.getToUserId())
				.toUserNick(room.getToUserNick())
				.toUserImg(room.getToUserImg())
				.roomId(room.getRoomId())
				.lastMsg(room.getLastMsg())
				.read(true)
				.lastMsgTime(room.getLastMsgTime())
				.build();
		joinRoomRepository.save(updateRoom);
	}

	// 채팅 유저찾기
	public FindUserListDTO search(Long myId, String nickName) {
		if(nickName.trim().equals("")) {
			return FindUserListDTO.builder().users(null).build();
		} else {
			List<UserEntity> users = userRepository.findByNicknameContaining(nickName.trim());
			return FindUserListDTO.builder()
					.users(
							users.stream().map(user -> FindUserListDTO.User.builder()
									.userId(user.getId())
									.roomId(
											joinRoomRepository.findByMyUserIdAndToUserId(myId, user.getId()) != null ?
													joinRoomRepository.findByMyUserIdAndToUserId(myId, user.getId()).getRoomId() : null 
											)
									.profileImg(user.getProfileImage())
									.nickName(user.getNickname())
									.build()
									).toList()
							).build();
			}
		}
	
}
