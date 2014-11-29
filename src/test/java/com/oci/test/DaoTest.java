package com.oci.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.oci.dao.DaoRegistry;
import com.oci.dao.IDao;
import com.oci.entity.Chain;
import com.oci.entity.Item;
import com.oci.entity.Row;
import com.oci.entity.User;

public class DaoTest extends BaseTestSupport {

    @Test
    public void emptyTest() {

    }
    
	@Test
	public void simpleChainCreateTest() {
		DaoRegistry daoRegistry = appInjector.getInstance(DaoRegistry.class);
		IDao<Chain,Long> chainDao = daoRegistry.getDao(Chain.class);
		IDao<Row,Long> rowDao = daoRegistry.getDao(Row.class);
		IDao<Item,Long> itemDao = daoRegistry.getDao(Item.class);

		Chain chain = new Chain();
		chain.setName("test_chainTest");
		Long chainId = chainDao.create(null, chain);

		Row row = new Row();
		row.setChainId(chainId);
		row.setName("test_rowTest");
		Long rowId = rowDao.create(null, row);
		
		Item item = new Item();
		item.setRowId(rowId);
		item.setName("test_itemTest");
		Long itemId = itemDao.create(null, item);
		
		Item itemReloaded = itemDao.get(null, itemId).get();
		Row rowReloaded = rowDao.get(null, itemReloaded.getRowId()).get();
		Chain chainReloaded = chainDao.get(null, rowReloaded.getChainId()).get();

		assertEquals("test_chainTest", chainReloaded.getName());
	}

	@Test
	public void testChainWithUser(){
		User user1 = createTestUser1();
		User user2 = createTestUser2();

		DaoRegistry daoRegistry = appInjector.getInstance(DaoRegistry.class);
		IDao<Chain,Long> chainDao = daoRegistry.getDao(Chain.class);

		// user1 has 2 projects
		Chain chain = new Chain();
		chain.setName("test_chain-01");
		chain.setUserId(user1.getId());
		chainDao.create(user1, chain);

		chain = new Chain();
		chain.setName("test_chain-02");
		chain.setUserId(user1.getId());
		chainDao.create(user1, chain);

		// user2 has one project
		chain = new Chain();
		chain.setName("test_chain-03");
		chain.setUserId(user2.getId());
		chainDao.create(user2, chain);


		List<Chain> projects = chainDao.list(user1, null, 0, 100, "asc", "id");
		assertEquals(2, projects.size());

		projects = chainDao.list(user2, null, 0, 100, "asc", "id");
		assertEquals(1, projects.size());

	}
}
