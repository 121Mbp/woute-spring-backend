package xyz.heetaeb.Woute.domain.search.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import xyz.heetaeb.Woute.domain.search.dto.KeywordRequest;
import xyz.heetaeb.Woute.domain.search.dto.SearchResponseDTO;
import xyz.heetaeb.Woute.domain.search.dto.SearchResultDTO;
import xyz.heetaeb.Woute.domain.search.service.SearchService;

@RequiredArgsConstructor
@RestController
public class SearchController {
	private final SearchService searchService;
	
	@PostMapping("/search")
	public SearchResponseDTO search(@RequestBody KeywordRequest dto) {
		System.out.println("keyword : " + dto.getKeyword());
		System.out.println("keyword sub : " + dto.getKeyword().startsWith("#", 1));
		return searchService.search(dto.getKeyword());
	}
	
	@GetMapping("/search/tags/{keyword}")
	public List<SearchResultDTO> getResult(@PathVariable("keyword") String keyword) {
		return searchService.toResultPage(keyword);
	}
	
}
