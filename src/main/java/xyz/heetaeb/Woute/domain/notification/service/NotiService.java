package xyz.heetaeb.Woute.domain.notification.service;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import xyz.heetaeb.Woute.domain.notification.dto.NotiRespDTO;
import xyz.heetaeb.Woute.domain.notification.entity.Notification;
import xyz.heetaeb.Woute.domain.notification.repository.EmitterRepository;
import xyz.heetaeb.Woute.domain.notification.repository.NotiRepository;

@Log4j2
@RequiredArgsConstructor
@Service
public class NotiService {

	private final EmitterRepository emitterRepository;
	private final NotiRepository notiRepository;
	
	public SseEmitter subscribe(Long id, String lastEventId) {
		String userid = id + "_" +  System.currentTimeMillis();
		log.info(userid);
		
		SseEmitter emitter = emitterRepository.save(userid, new SseEmitter(60*1000L*60));
		log.info("emitter : " + emitter);
		try {
			emitter.send(SseEmitter.event()
					.name("connect")
					.data("connected!!"));
		} catch (IOException e) {
			log.error(e);
			e.printStackTrace();
		}
		
		emitter.onCompletion(() -> emitterRepository.deleteById(userid));
		emitter.onTimeout(() -> emitterRepository.deleteById(userid));
		emitter.onError((e) -> emitterRepository.deleteById(userid));
		
		if (!lastEventId.isEmpty()) { // 클라이언트가 미수신한 Event 유실 예방, 연결이 끊켰거나 미수신된 데이터를 다 찾아서 보내준다.
			Map<String, Object> events = emitterRepository.findAllEventCacheStartWithByMemberId(lastEventId);
			events.entrySet().stream()
					.filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
					.forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
		}
        
		return emitter;
	}

	public List<NotiRespDTO> getList(Long id) {
		return notiRepository.findByUserIdOrderByCreatedAtDesc(id).stream().map(noti -> NotiRespDTO.builder()
				.id(noti.getId())
				.userId(noti.getUserId())
				.nickname(noti.getNickname())
				.profileImg(noti.getProfileImg())
				.content(noti.getContent())
				.senderUrl(noti.getSenderUrl())
				.type(noti.getType())
				.isRead(noti.isRead())
				.CreatedAt(noti.getCreatedAt())
				.build()).toList();
	}
	
	public void notiIsRead(Long id) {
		List<Notification> notis = notiRepository.findByUserIdAndRead(id, false);
		List<Notification> readNotis = notis.stream().map(noti -> Notification.builder()
				.id(noti.getId())
				.userId(noti.getUserId())
				.nickname(noti.getNickname())
				.profileImg(noti.getProfileImg())
				.content(noti.getContent())
				.senderUrl(noti.getSenderUrl())
				.type(noti.getType())
				.read(true)
				.createdAt(noti.getCreatedAt())
				.build()).toList();
		notiRepository.saveAll(readNotis);
	}
	
	
	
	@Transactional
	public void send(Long userid, String nickname, String profileImg, String content, String url, String type) {
		
        Notification notification = creatNoti(userid, nickname, profileImg, content, url, type);
        String id = String.valueOf(userid);
        
        // 로그인 한 유저의 SseEmitter 모두 가져오기
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllByStartWithId(id);
        sseEmitters.forEach(
                (key, emitter) -> {
                	log.info("key : " + key);
                	log.info("emitter : " + emitter);
                    // 데이터 캐시 저장(유실된 데이터 처리하기 위함)
                    emitterRepository.saveEventCache(key, notification);
                    
                    // 데이터 전송
                    sendToClient(emitter, key, NotiRespDTO.toDto(notification));
                }
        );
    }

	private Notification creatNoti(Long id, String nickname, String profileImg,  String content, String url, String type) {
		Notification notification = Notification.builder()
				.userId(id)
				.nickname(nickname)
				.profileImg(profileImg)
				.content(content)
				.senderUrl(url)
				.type(type)
				.read(false)
				.createdAt(ZonedDateTime.now())
				.build();
		return notiRepository.save(notification);
	}
	
	private void sendToClient(SseEmitter emitter, String id, Object data) {
		try {
			emitter.send(SseEmitter.event()
					.id(id)
					.name("sse")
					.data(data));
		} catch (IOException e) {
			log.error(e);
			emitterRepository.deleteById(id);
		}
		
	}
	
}
