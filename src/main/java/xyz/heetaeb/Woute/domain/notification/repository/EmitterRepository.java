package xyz.heetaeb.Woute.domain.notification.repository;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EmitterRepository {
	private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
	private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

	public SseEmitter save(String id, SseEmitter sseEmitter) {
		emitters.put(id, sseEmitter);
		return sseEmitter;
	}

	public void saveEventCache(String id, Object event) {
		eventCache.put(id, event);
	}

    public void deleteById(String id) {
        emitters.remove(id);
    }

    public Map<String, Object> findAllEventCacheStartWithByMemberId(String memberId) {
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(memberId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }	
    
	public Map<String, SseEmitter> findAllByStartWithId(String id) {
		return emitters.entrySet().stream()
				.filter(entry -> entry.getKey().startsWith(id))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public void deleteAllStartByWithId(String id) {
		emitters.forEach((key, emitter) -> {
			if (key.startsWith(id)) emitters.remove(key);
		});
	}
}
