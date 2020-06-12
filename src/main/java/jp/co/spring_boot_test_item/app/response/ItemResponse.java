package jp.co.spring_boot_test_item.app.response;

import java.util.List;

import jp.co.spring_boot_test_item.domain.model.Item;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemResponse {

	// Item返却用
	private Item item;

	// List<Item>返却用
	private List<Item> items;

}
