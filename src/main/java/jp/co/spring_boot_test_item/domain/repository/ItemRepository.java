package jp.co.spring_boot_test_item.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.spring_boot_test_item.domain.model.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

	// 「select * from items where item_id = itemId（引数としてわたってくる変数）;」と同義
	public Item findByItemId(int itemId);

	// 「select * from items where category_id = categoryId（引数としてわたってくる変数）;」と同義
	public List<Item> findByCategoryId(int categoryId);

}
