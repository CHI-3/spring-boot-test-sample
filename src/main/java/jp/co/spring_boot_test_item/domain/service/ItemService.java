package jp.co.spring_boot_test_item.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jp.co.spring_boot_test_item.domain.model.Item;
import jp.co.spring_boot_test_item.domain.repository.ItemRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;

	public Item findByItemId(int itemId) {
		return itemRepository.findByItemId(itemId);
	}

	public List<Item> findByCategoryId(int categoryId) {
		return itemRepository.findByCategoryId(categoryId);
	}

}
