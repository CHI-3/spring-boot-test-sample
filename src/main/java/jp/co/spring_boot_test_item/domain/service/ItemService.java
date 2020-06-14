package jp.co.spring_boot_test_item.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.spring_boot_test_item.domain.model.Item;
import jp.co.spring_boot_test_item.domain.repository.ItemRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;

	@Transactional(readOnly = true)
	public Item findByItemId(int itemId) {
		return itemRepository.findByItemId(itemId);
	}

	@Transactional(readOnly = true)
	public List<Item> findByCategoryId(int categoryId) {
		return itemRepository.findByCategoryId(categoryId);
	}

}
