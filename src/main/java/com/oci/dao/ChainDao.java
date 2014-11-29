package com.oci.dao;

import java.util.List;
import java.util.Map;

import org.j8ql.query.Condition;
import org.j8ql.query.Query;
import org.j8ql.query.SelectQuery;

import com.google.inject.Singleton;
import com.oci.entity.Chain;
import com.oci.entity.User;

@Singleton
public class ChainDao extends BaseDao<Chain, Long> {

    // --------- Create --------- //
    @Override
    public Long create(User user, Chain entity) {
        Long chainId = super.create(user, entity);
        return chainId;
    }

	@Override
    public Long create(User user, Map map) {
        Chain chain = new Chain();
        chain.setName(map.get("name").toString());
        chain.setUserId(user.getId());
        Long chainId = super.create(user, chain);
        return chainId;
    }
	// --------- /Create --------- //

	// --------- List --------- //
    @Override
	public List<Chain> list(User user, Condition filter, int pageIdx, int pageSize, String orderType, String... orderBy) {
		// get the basic list select
		SelectQuery<Chain> select = listSelectBuilder(user,filter,pageIdx,pageSize,orderType,orderBy);
		if (user != null){
			Condition userCondition = Query.and("userId",user.getId());
			Condition where = select.getWhere();
			where = (where == null) ? userCondition : where.and(userCondition);
			select = select.where(where);
		}
		return daoHelper.list(select);
	}

    @Override
    public List<Chain> list(User user, int pageIdx, int pageSize, String orderType, Map filter, String... orderBy) {
        Condition userIdCondition = Query.and("userId", user.getId());
        return daoHelper.list(listSelectBuilder(user,userIdCondition,pageIdx,pageSize,orderType,orderBy));
    }
    // --------- /List --------- //

}
