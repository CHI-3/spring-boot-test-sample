package jp.co.spring_boot_test_item.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jp.co.spring_boot_test_item.app.request.ItemPostRequest;
import jp.co.spring_boot_test_item.app.response.ItemResponse;
import jp.co.spring_boot_test_item.domain.model.Item;
import jp.co.spring_boot_test_item.domain.service.ItemService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ItemController {

	private final ItemService itemService;

	@GetMapping("/item/{itemId}")
	public ResponseEntity<ItemResponse> getItemsByGet(@PathVariable int itemId) {
		Item item = itemService.findByItemId(itemId);
		ItemResponse itemResponse = ItemResponse.builder().item(item).build();
		return new ResponseEntity<>(itemResponse, HttpStatus.OK);
	}

	@PostMapping("/items")
	public ResponseEntity<ItemResponse> getItemsByPost(@Validated @RequestBody ItemPostRequest request) {
		List<Item> items = itemService.findByCategoryId(request.getCategoryId());
		ItemResponse itemResponse = ItemResponse.builder().items(items).build();
		return new ResponseEntity<>(itemResponse, HttpStatus.OK);
	}

}
