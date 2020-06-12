package jp.co.spring_boot_test_item.app.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RestController;

import jp.co.spring_boot_test_item.domain.model.Item;
import jp.co.spring_boot_test_item.domain.service.ItemService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ItemControllerTest {

	MockMvc mockMvc;

	@Mock // モックオブジェクトとして使用することを宣言
	private ItemService itemService;

	@InjectMocks // モックオブジェクトの注入
	private ItemController itemController;

	@BeforeEach // テストメソッド（@Testをつけたメソッド）実行前に都度実施
	public void initmocks() {
		MockitoAnnotations.initMocks(this); // アノテーションの有効化
		mockMvc = MockMvcBuilders.standaloneSetup(itemController).build(); // MockMvcのセットアップ
	}

	@Test
	public void test_getItemsByGet() throws Exception {

		when(itemService.findByItemId(1)).thenReturn(getItemIdOfItemId1()); // itemService.findByItemId(1)実行時にgetItemIdOfItemId1()の結果が返ることを定義
		this.mockMvc.perform(get("/item/{itemId}", 1)) // @GetMapping("/item/{itemId}")のメソッドの実行と結果確認
				.andExpect(status().isOk()) // 以下、結果確認
				.andExpect(jsonPath("$.item.itemId").value(1))
				.andExpect(jsonPath("$.item.itemName").value("ふしぎなアメ"))
				.andExpect(jsonPath("$.item.itemExplanation").value("レベル1アップします。"))
				.andExpect(jsonPath("$.item.categoryId").value(1));
	}

	@Test
	public void test_getItemsByPost() throws Exception {
		when(itemService.findByCategoryId(1)).thenReturn(getItemIdOfCategory1()); // findByCategoryId(1)実行時にgetItemIdOfCategory1()の結果が返ることを定義
		this.mockMvc.perform(post("/items").contentType(MediaType.APPLICATION_JSON).content("{\"categoryId\":\"1\"}")) // RequestBodyの要素の型と値を設定
				.andExpect(jsonPath("$.items", hasSize(2))) // 以下、結果確認
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.items[0].itemId").value(1))
				.andExpect(jsonPath("$.items[0].itemName").value("ふしぎなアメ"))
				.andExpect(jsonPath("$.items[0].itemExplanation").value("レベル1アップします。"))
				.andExpect(jsonPath("$.items[0].categoryId").value(1))
				.andExpect(jsonPath("$.items[1].itemId").value(2))
				.andExpect(jsonPath("$.items[1].itemName").value("オボンのみ"))
				.andExpect(jsonPath("$.items[1].itemExplanation").value("HPをすこしだけかいふくする。"))
				.andExpect(jsonPath("$.items[1].categoryId").value(1));
	}

	public Item getItemIdOfItemId1() {

		Item item = new Item();

		item.setItemId(1);
		item.setItemName("ふしぎなアメ");
		item.setItemExplanation("レベル1アップします。");
		item.setCategoryId(1);

		return item;

	}

	public List<Item> getItemIdOfCategory1() {

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
