package com.oci.dao;
import org.j8ql.query.Condition;

import com.oci.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IDao<E,I> {

	public enum SortOrder {
		ASC, DESC
	}

	public Optional<E> get(User user, I id);

	public I create(User user, E entity);

	public I create(User user, Map entityMap);

	public int update(User user, E entity, I id);

	public int delete(User user, I id);
	
	public List<E> list(User user, int pageIdx, int pageSize, String orderType, Map filter, String... orderBy);
	
	public List<E> list(User user, Condition filter, int pageIdx, int pageSize, String orderType, String... orderBy);
	
	public Long count(Condition filter);

	public Class<E> getPersistentClass();

}