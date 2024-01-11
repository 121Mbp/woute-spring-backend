package xyz.heetaeb.Woute.domain.notification.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;
import xyz.heetaeb.Woute.domain.chat.dto.ChatRequestDTO;
import xyz.heetaeb.Woute.domain.notification.dto.NotiRespDTO;
import xyz.heetaeb.Woute.domain.notification.service.NotiService;

@RequiredArgsConstructor
@RestController
public class NotiController {
	
	private final NotiService notiService;
	
	/**
	 * SSE 연결
	 * @param userid
	 * @param lastEventId
	 * @return
	 */
	@GetMapping(value = "/sub/{id}", produces = "text/event-stream")
	public SseEmitter subscribe(@PathVariable("id") Long userid,
			@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
		return notiService.subscribe(userid, lastEventId);
	}
	
	/**
	 * 알림 목록 조회
	 * @param id
	 * @return
	 */
	@GetMapping("/noti/{id}")
	public List<NotiRespDTO> getList(@PathVariable("id") Long id) {
		return notiService.getList(id);
	}
	
	/**
	 * 알림 읽음 처리
	 * @param dto
	 */
	@PostMapping("/noti")
	public void notiIsRead(@RequestBody ChatRequestDTO dto) {
		notiService.notiIsRead(dto.getMyId());
	}
}
