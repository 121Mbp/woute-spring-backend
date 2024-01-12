package xyz.heetaeb.Woute.domain.search.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import xyz.heetaeb.Woute.domain.feed.entity.AttachEntity;
import xyz.heetaeb.Woute.domain.feed.entity.FeedEntity;
import xyz.heetaeb.Woute.domain.feed.entity.TagsEntity;
import xyz.heetaeb.Woute.domain.feed.repository.AttachRepository;
import xyz.heetaeb.Woute.domain.feed.repository.FeedRepository;
import xyz.heetaeb.Woute.domain.feed.repository.TagsRepository;
import xyz.heetaeb.Woute.domain.reply.entity.ReplyEntity;
import xyz.heetaeb.Woute.domain.reply.repository.ReplyRepository;
import xyz.heetaeb.Woute.domain.search.dto.SearchResponseDTO;
import xyz.heetaeb.Woute.domain.search.dto.SearchResultDTO;
import xyz.heetaeb.Woute.domain.user.entity.UserEntity;
import xyz.heetaeb.Woute.domain.user.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class SearchService {
	private final TagsRepository tagsRepository;
	private final UserRepository userRepository;
	private final FeedRepository feedRepository;
	private final AttachRepository attachRepository;
	private final ReplyRepository replyRepository;
	
	
	// 검색
	public SearchResponseDTO search(String keyword) {
		if(keyword.startsWith("#", 0)) {
			System.out.println("태그");
			String tagKeyword = keyword.trim().replace("#", "");
			System.out.println("subKey : " + tagKeyword);
			if(tagKeyword.trim().equals("")) {
				System.out.println("태그 공백");
				List<TagsEntity> tags = tagsRepository.findDistinctByWordsContaining("$");
				return SearchResponseDTO.builder()
						.tags(
								tags.stream().distinct().map(tag -> SearchResponseDTO.Tag.builder()
										.words(tag.getWords())
										.build()
										).toList()
								).build();
			}
			List<TagsEntity> tags = tagsRepository.findDistinctByWordsContaining(tagKeyword);
			return SearchResponseDTO.builder()
					.tags(
							tags.stream().distinct().map(tag -> SearchResponseDTO.Tag.builder()
									.words(tag.getWords())
									.build()
									).toList()
							).build();
		} else if(keyword.trim().equals("")) {
			System.out.println("공백");
			List<UserEntity> users = userRepository.findByNicknameContaining("&");
			return SearchResponseDTO.builder()
					.users(
							users.stream().map(user -> SearchResponseDTO.User.builder()
									.id(user.getId())
									.nickName(user.getNickname())
									.profileImg(user.getProfileImage())
									.url("/users/" + user.getId())
									.build()).toList()
							).build();
			} else {
				System.out.println("유저");
				List<UserEntity> users = userRepository.findByNicknameContaining(keyword);
				return SearchResponseDTO.builder()
						.users(
								users.stream().map(user -> SearchResponseDTO.User.builder()
										.id(user.getId())
										.nickName(user.getNickname())
										.profileImg(user.getProfileImage())
										.url("/users/" + user.getId())
										.build()).toList()
								).build();
			}
	}


	// 검색목록에서 태그 선택시 결과페이지로 이동
	public List<SearchResultDTO> toResultPage(String keyword) {
		List<TagsEntity> tags = tagsRepository.findDistinctByWordsContaining(keyword);
		List<Long> feedIds = tags.stream()
				.map(TagsEntity::getFeedId)
				.collect(Collectors.toList());
		List<FeedEntity> feeds = feedRepository.findAllById(feedIds);
		return dataList(feeds);
	}
	
	 public List<SearchResultDTO> dataList(List<FeedEntity> feeds) {
	        return feeds.stream().map(post -> {
	            Long feedId = post.getId();
	            List<AttachEntity> attaches = attachRepository.findAllByFeedId(feedId);
	            List<ReplyEntity> reply = replyRepository.findByFeedId(feedId);
	            int replyCount = reply.size();
	            return SearchResultDTO.builder()
	            		.id(post.getId())
	            		.type(post.getType())
	                    .heartCount(post.getHeartCount())
	                    .attaches(
	                            attaches.stream().map(attach -> SearchResultDTO.Attach.builder()
	                                    .uuid(attach.getUuid())
	                                    .origin(attach.getOrigin())
	                                    .type(attach.getType())
	                                    .filePath(attach.getFilePath())
	                                    .build()).toList()
	                    )
	                    .replyCount(replyCount)
	                    .build();
	        }).toList();
	    }
	
	
}
