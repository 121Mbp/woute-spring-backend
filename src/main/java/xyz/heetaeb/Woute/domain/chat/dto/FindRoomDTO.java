package xyz.heetaeb.Woute.domain.chat.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FindRoomDTO {
	private Long roomId;
}
