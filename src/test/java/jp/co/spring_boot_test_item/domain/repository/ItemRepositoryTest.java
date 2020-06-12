package jp.co.spring_boot_test_item.domain.repository;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import jp.co.spring_boot_test_item.domain.model.Item;

@SpringBootTest
@Transactional
public class ItemRepositoryTest {

	@Autowired
	JdbcTemplate jdbctemplate;

	@Autowired
	ItemRepository itemRepository;

	String item_id_str = null;

	Integer item_id_num = null;

	private File file = null;

	private FileOutputStream out;

	private FileInputStream in;

	File tempDir = new File("src/test/java/jp/co/spring_boot_test_item/domain/repository");

	@BeforeTransaction // 各@Transactional実行前に都度実施
	public void initdb() throws Exception {
		Connection connection = jdbctemplate.getDataSource().getConnection();
		IDatabaseConnection dbconnection = new DatabaseConnection(connection);
		try {

			// AUTO_INCREMENTの現在値を取得
			String sql = "SHOW TABLE STATUS where  name = 'items'";
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				item_id_str = result.getString("Auto_increment");
			}

			// itemsテーブルのバックアップを取得
			QueryDataSet partialDataSet = new QueryDataSet(dbconnection);
			partialDataSet.addTable("items");
			file = File.createTempFile("items", ".xml", tempDir);
			out = new FileOutputStream(file);
			FlatXmlDataSet.write(partialDataSet, out);
			out.flush();
			out.close();

			// DBの値を削除し、テストデータを投入する（ExcelファイルのパスはItemRepositoryTest.javaと同階層）
			IDataSet dataset = new XlsDataSet(
					new File("src/test/java/jp/co/spring_boot_test_item/domain/repository/ItemRepositoryTest.xlsx"));
			DatabaseOperation.CLEAN_INSERT.execute(dbconnection, dataset);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			connection.close();
			dbconnection.close();

		}
	}

	@AfterTransaction // 各@Transactional実行後に都度実施
	public void teardown() throws Exception {
		Connection connection = jdbctemplate.getDataSource().getConnection();
		IDatabaseConnection dbconnection = new DatabaseConnection(connection);
		try {

			// テストデータを削除し、バックアップファイルの内容を戻す
			in = new FileInputStream(file);
			IDataSet dataSet = new FlatXmlDataSetBuilder().build(in);
			DatabaseOperation.CLEAN_INSERT.execute(dbconnection, dataSet);

			// AUTO_INCREMENTをテスト実行前の値に戻す
			String sql = new StringBuilder("ALTER TABLE items AUTO_INCREMENT=").append(item_id_str).toString();
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
			dbconnection.close();
			file.deleteOnExit(); // DBバックアップファイルの削除
		}
	}

	@Test
	public void test_findByItemId() {

		Item actualItem = itemRepository.findByItemId(1);

		assertThat(actualItem.getItemId(), is(1));
		assertThat(actualItem.getItemName(), is("ふしぎなアメ"));
		assertThat(actualItem.getItemExplanation(), is("レベル1アップします。"));
		assertThat(actualItem.getCategoryId(), is(1));

	}

	@Test
	public void test_findByCategoryId() {

		Item item1 = new Item();
		item1.setItemId(1);
		item1.setItemName("ふしぎなアメ");
		;
		item1.setItemExplanation("レベル1アップします。");
		item1.setCategoryId(1);

		Item item2 = new Item();
		item2.setItemId(4);
		item2.setItemName("オボンのみ");
		item2.setItemExplanation("HPをすこしだけかいふくする。");
		item2.setCategoryId(1);

		List<Item> expectedItems = new ArrayList<Item>();
		expectedItems.add(item1);
		expectedItems.add(item2);

		List<Item> actualItems = itemRepository.findByCategoryId(1);

		assertThat(actualItems, hasSize(2));

		for (int i = 0; i < actualItems.size(); i++) {
			for (int j = 0; j < expectedItems.size(); j++) {
				if (actualItems.get(i).getItemId() == expectedItems.get(j).getItemId()) {
					assertThat(actualItems.get(i).getItemName(), is(expectedItems.get(j).getItemName()));
					assertThat(actualItems.get(i).getItemExplanation(), is(expectedItems.get(j).getItemExplanation()));
					assertThat(actualItems.get(i).getCategoryId(), is(expectedItems.get(j).getCategoryId()));
					break;
				}
			}

		}
	}

	@Test
	public void test_save() throws Exception {

		// 更新
		Item Item1 = new Item();
		Item1.setItemId(1);
		Item1.setItemName("ふつうのアメ");
		Item1.setItemExplanation("20kcal摂取できます。");
		Item1.setCategoryId(3);

		itemRepository.save(Item1);

		// 追加
		Item item2 = new Item();
		item2.setItemName("ペロペロキャンディー");
		item2.setItemExplanation("子どもにあげると喜ばれます（たぶん）。");
		item2.setCategoryId(3);

		itemRepository.save(item2);

		// DBの値の確認（更新分）
		Item dbItem1 = itemRepository.findByItemId(1);

		assertThat(dbItem1.getItemId(), is(1));
		assertThat(dbItem1.getItemName(), is("ふつうのアメ"));
		assertThat(dbItem1.getItemExplanation(), is("20kcal摂取できます。"));
		assertThat(dbItem1.getCategoryId(), is(3));

		// DBの値の確認（追加分）
		item_id_num = Integer.valueOf(item_id_str);
		Item dbItem2 = itemRepository.findByItemId(item_id_num);

		assertThat(dbItem2.getItemId(), is(item_id_num));
		assertThat(dbItem2.getItemName(), is("ペロペロキャンディー"));
		assertThat(dbItem2.getItemExplanation(), is("子どもにあげると喜ばれます（たぶん）。"));
		assertThat(dbItem2.getCategoryId(), is(3));

	}

}
