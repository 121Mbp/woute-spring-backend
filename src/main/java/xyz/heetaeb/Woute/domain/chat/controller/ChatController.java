package xyz.heetaeb.Woute.domain.chat.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.core.util.Json;

import java.util.List;
import java.util.Map;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import lombok.RequiredArgsConstructor;
import xyz.heetaeb.Woute.domain.chat.dto.ChatListResponseDTO;
import xyz.heetaeb.Woute.domain.chat.dto.ChatLogResponseDTO;
import xyz.heetaeb.Woute.domain.chat.dto.ChatRequestDTO;
import xyz.heetaeb.Woute.domain.chat.dto.FindUserListDTO;
import xyz.heetaeb.Woute.domain.chat.service.ChatService;

@RequiredArgsConstructor
@RestController
public class ChatController {

	private final SimpMessagingTemplate simpMessagingTemplate;
	private final ChatService chatService;
	
	/**
	 * 메뉴 클릭시 첫 디엠 화면
	 * @param id
	 */
//	@GetMapping("/chat") // 로그인한 정보 가져올시
	@GetMapping("/chat/{id}")
	public ChatListResponseDTO getList(@PathVariable("id") Long id) {
		return chatService.getList(id);
	}
	
	/**
	 * 클릭한 디엠내역 조회
	 * @param roomId
	 */
//	@PostMapping("/chat/m/{id}") // 로그인 정보 가져올시
	@PostMapping("/chat/{id}/m/{userId}")
	public ChatLogResponseDTO getRoom(@RequestBody ChatRequestDTO dto) {
		System.out.println("로그 조회성공");
		return chatService.getRoom(dto.getRoomId());
	}
	
	/**
	 * 디엠 발송
	 * @param id
	 * @param dto
	 */
//	@MessageMapping("/chat/m") // 로그인 정보 가져올시
	@MessageMapping("/chat/m")
	public void message(Map<String, ChatRequestDTO> body) {
		System.out.println("body : " + body);
		if(body.get("newMessage") != null) {
			System.out.println("newMessage : " + body.get("message"));
			ChatRequestDTO dto = body.get("newMessage");
			System.out.println("new dto : " + dto);
			chatService.createRoom(dto);
			simpMessagingTemplate.convertAndSend("/sub/chat/" + dto.getMyId() + "/m/" + dto.getRoomId()  , dto);
		} else {
			System.out.println("id : " + body.get("message"));
			ChatRequestDTO dto = body.get("message");
			System.out.println("dto : " + dto);
			chatService.createRoom(dto);
			simpMessagingTemplate.convertAndSend("/sub/chat/m/" + dto.getRoomId()  , dto);
		}
	}                                                                                                                          
	
	/**
	 * 안읽은 채팅방 읽음 처리
	 * @param id
	 * @param dto
	 */
	@PostMapping("/chat/{id}/read")
	public void read(@PathVariable("id") Long id, @RequestBody ChatRequestDTO dto) {
		chatService.isRead(id, dto.getRoomId());
	}
	
	/**
	 * 채팅 유저검색
	 * @param dto
	 * @return
	 */
	@PostMapping("/chat/user")
	public FindUserListDTO search(@RequestBody ChatRequestDTO dto) {
		System.out.println("nick : " + dto.getNickName());
		return chatService.search(dto.getMyId(), dto.getNickName());
	}
}
