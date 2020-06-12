package jp.co.spring_boot_test_item.domain.service;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jp.co.spring_boot_test_item.domain.model.Item;
import jp.co.spring_boot_test_item.domain.repository.ItemRepository;

public class ItemServiceTest {

	@Mock // モックオブジェクトとして使用することを宣言
	private ItemRepository itemRepository;

	@InjectMocks // モックオブジェクトの注入
	private ItemService itemService;

	@BeforeEach // テストメソッド（@Testをつけたメソッド）実行前に都度実施
	public void initmocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void test_findByItemId() {

		when(itemRepository.findByItemId(100)).thenReturn(getItemId100()); // itemRepository.findByItemId(100)実行時にgetItemId100()の結果が返ることを定義
		Item item = itemRepository.findByItemId(100);

		// 以下、結果確認
		assertThat(item.getItemId(), is(100));
		assertThat(item.getItemName(), is("超ふしぎなアメ"));
		assertThat(item.getItemExplanation(), is("レベル100アップします。"));
		assertThat(item.getCategoryId(), is(1));
	}

	@Test
	public void test_findByCategoryId() {

		when(itemRepository.findByCategoryId(10)).thenReturn(getItemIdOfCategory10()); // itemRepository.findByCategoryId(10)実行時にgetItemIdOfCategory10()の結果が返ることを定義
		List<Item> items = itemRepository.findByCategoryId(10);

		// 以下、結果確認
		assertThat(items.get(0).getItemId(), is(1));
		assertThat(items.get(0).getItemName(), is("ふしぎなアメ"));
		assertThat(items.get(0).getItemExplanation(), is("レベル1アップします。"));
		assertThat(items.get(0).getCategoryId(), is(1));

		assertThat(items.get(1).getItemId(), is(2));
		assertThat(items.get(1).getItemName(), is("オボンのみ"));
		assertThat(items.get(1).getItemExplanation(), is("HPをすこしだけかいふくする。"));
		assertThat(items.get(1).getCategoryId(), is(1));

	}

	public Item getItemId100() {

		Item item = new Item();
		item.setItemId(100);
		item.setItemName("超ふしぎなアメ");
		item.setItemExplanation("レベル100アップします。");
		item.setCategoryId(1);

		return item;

	}

	public List<Item> getItemIdOfCategory10() {

		List<Item> items = new ArrayList<>();

		Item item1 = new Item();
		item1.setItemId(1);
		item1.setItemName("ふしぎなアメ");
		item1.setItemExplanation("レベル1アップします。");
		item1.setCategoryId(1);

		Item item2 = new Item();
		item2.setItemId(2);
		item2.setItemName("オボンのみ");
		item2.setItemExplanation("HPをすこしだけかいふくする。");
		item2.setCategoryId(1);

		items.add(item1);
		items.add(item2);

		return items;

	}

}
