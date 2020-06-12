package jp.co.spring_boot_test_item.domain.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.spring_boot_test_item.app.request.ItemUpdateRequest;
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

	@Transactional(rollbackFor = Exception.class)
	public Item updateItem(ItemUpdateRequest request) throws SQLException {

		Item item = new Item();
		item.setItemId(request.getItemId());
		item.setItemName(request.getItemName());
		item.setItemExplanation(request.getItemExplanation());
		item.setCategoryId(request.getCategoryId());

		itemRepository.save(item);

		return itemRepository.findByItemId(request.getItemId());

	}

	@Transactional(rollbackFor = Exception.class)
	public void deleteItem(int itemId) throws NullPointerException, SQLException {

		Item item = itemRepository.findByItemId(itemId);
		itemRepository.delete(item);

	}

}
