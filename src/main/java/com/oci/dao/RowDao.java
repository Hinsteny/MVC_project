package com.oci.dao;

import java.util.List;
import java.util.Map;

import org.j8ql.query.Condition;
import org.j8ql.query.Query;

import com.google.inject.Singleton;
import com.oci.entity.Row;
import com.oci.entity.User;

@Singleton
public class RowDao extends BaseDao<Row, Long> {

    @Override
    public Long create(User user, Map map) {
        Row row = new Row();
        row.setName(map.get("name").toString());
        row.setChainId(Long.valueOf(map.get("chainId").toString()));
        Long rowId = super.create(user, row);
        return rowId;
    }

    @Override
    public List<Row> list(User user, int pageIdx, int pageSize, String orderType, Map filter, String... orderBy) {
        Integer chainId = filter.get("chainId") == null ? -1 : Integer.valueOf(filter.get("chainId").toString());
        Condition ChainIdCondition = Query.and("chainId",chainId);
        return daoHelper.list(listSelectBuilder(user,ChainIdCondition,pageIdx,pageSize,orderType,orderBy));
    }
}
