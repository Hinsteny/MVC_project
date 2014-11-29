package com.oci.dao;

import java.util.List;
import java.util.Map;

import org.j8ql.query.Condition;
import org.j8ql.query.Query;

import com.google.inject.Singleton;
import com.oci.entity.Item;
import com.oci.entity.User;

@Singleton
public class ItemDao extends BaseDao<Item, Long> {

	@Override
	public Long create(User user, Map map) {
		Item item = new Item();
		item.setName(map.get("name").toString());
		item.setRowId(Long.valueOf(map.get("rowId").toString()));
		Long itemId = super.create(user, item);
		return itemId;
	}
	
	@Override
	public List<Item> list(User user, int pageIdx, int pageSize, String orderType, Map filter, String... orderBy) {
		Integer rowId = filter.get("rowId") == null ? -1 : Integer.valueOf(filter.get("rowId").toString());
		Condition rowIdCondition = Query.and("rowId",rowId);
		return daoHelper.list(listSelectBuilder(user,rowIdCondition,pageIdx,pageSize,orderType,orderBy));
	}
}
