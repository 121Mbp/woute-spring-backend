package xyz.heetaeb.Woute.domain.search.dto;

import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@AllArgsConstructor
public class SearchResponseDTO {
	List<User> users;
	List<Tag> tags;
	
	@Builder
	@Getter
	@AllArgsConstructor
	public static class User {
		private Long id;
		private String nickName;
		private String profileImg;
		private String url;
	}
	
	@Builder
	@Getter
	@AllArgsConstructor
	@Data
	public static class Tag {
		private Long id;
		private String words;
		@Override
		public int hashCode() {
			return Objects.hash(words);
		}
		@Override
		public boolean equals(Object obj) {
	        if (this == obj) return true;
	        if (obj == null || getClass() != obj.getClass()) return false;
	        Tag other = (Tag) obj;
	        return Objects.equals(words, other.words);			// TODO Auto-generated method stub
		}
		
	}
}
