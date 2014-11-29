package com.oci.dao;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.j8ql.query.Condition;
import org.j8ql.query.Query;
import org.j8ql.query.SelectQuery;

import com.google.inject.Inject;
import com.googlecode.gentyref.GenericTypeReflector;
import com.oci.entity.User;
import com.oci.web.CurrentUserHolder;


@SuppressWarnings({"rawtypes", "unchecked"})
public  class BaseDao<E,I> implements IDao<E,I> {

	protected Class<E> entityClass;
	protected Class<I> idClass;

	@Inject
	protected DaoHelper daoHelper;

	// TODO: needs to remove this. Not needed anymore
	@javax.inject.Inject
	protected CurrentUserHolder currentUserHolder;


	public BaseDao() {
		initEntityClass();
	}
	
	public BaseDao(boolean entityClassProvided){
		if (!entityClassProvided){
			initEntityClass();
		}
	}
	
	private void initEntityClass(){
		if (entityClass == null && idClass == null) {
			Type persistentType = GenericTypeReflector.getTypeParameter(getClass(), BaseDao.class.getTypeParameters()[0]);
			Type persistentIdType = GenericTypeReflector.getTypeParameter(getClass(), BaseDao.class.getTypeParameters()[1]);
			if (persistentType instanceof Class && persistentIdType instanceof Class) {
				this.entityClass = (Class<E>) persistentType;
				this.idClass = (Class<I>) persistentIdType;
			} else {
				throw new IllegalStateException("concrete class " + getClass().getName()
										+ " must have a generic Entity and ID types "
										+ BaseDao.class.getName());
			}
		}

	}



	// --------- IDao Interface --------- //
	@Override
	public Optional<E> get(User user, I id) {
		return daoHelper.first(Query.select(entityClass).whereId(id));
	}

	@Override
	public I create(User user, E entity) {
		return daoHelper.execute(Query.insert(entityClass).value(entity).returningIdAs(idClass));
	}

	@Override
	public I create(User user, Map map){
		return daoHelper.execute(Query.insert(entityClass).value(map).returningIdAs(idClass));
	}

	@Override
	public int update(User user, E entity, I id) {
		return daoHelper.execute(Query.update(entityClass).value(entity).whereId(id));
	}

	@Override
	public int delete(User user, I id) {
		return daoHelper.execute(Query.delete(entityClass).whereId(id));
	}

	@Override
	public List<E> list(User user, int pageIdx, int pageSize, String orderType, Map filter, String... orderBy) {
		return daoHelper.list(listSelectBuilder(user,null,pageIdx,pageSize,orderType,orderBy));
	}
	
	@Override
	public List<E> list(User user, Condition filter, int pageIdx, int pageSize, String orderType, String... orderBy) {
		return daoHelper.list(listSelectBuilder(user,filter,pageIdx,pageSize,orderType,orderBy));
	}
	
	@Override
	public Long count(Condition filter) {
		return daoHelper.count(Query.select(entityClass).where(filter));
	}

	@Override
	public Class<E> getPersistentClass() {
		// TODO Auto-generated method stub
		return entityClass;
	}
	// --------- IDao Interface --------- //

	// --------- Protected --------- //

	/**
	 * Base selectBuilder for the list api. Reused by sub class DAOs that need to add joins to the list.
	 */
	protected SelectQuery<E> listSelectBuilder(User user, Condition filter, int pageIdx, int pageSize, String orderType, String... orderBy) {
		int limit = pageSize;
		int offset = pageIdx * pageSize;
		renderOrderType(orderType, orderBy);
		return Query.select(entityClass).where(filter).limit(limit).offset(offset).orderBy(orderBy);
	}

	// --------- /Protected --------- //

	// --------- Private --------- //
	/**
	 * if the orderType is "desc" so add "!" to orderBy string
	 * @param orderType
	 * @param orderBy
	 */
	private void renderOrderType(String orderType, String... orderBy){
		if("desc".equalsIgnoreCase(orderType)){
			for(int i = 0, j =  orderBy.length; i < j; i++){
				orderBy[i] = "!" + orderBy[i].trim();
			}
		}
	}
	// --------- /Private --------- //
}