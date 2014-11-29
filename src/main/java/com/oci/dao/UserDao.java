package com.oci.dao;

import org.j8ql.query.Query;

import com.google.inject.Singleton;
import com.oci.entity.User;

@Singleton
public class UserDao extends BaseDao<User,Long> {


	// TODO: needs to return Option<User>
	public User getByUsername(String username){
		return daoHelper.first(Query.select(entityClass).where("username", username)).orElse(null);
	}

	/**
	 * Higher level methods to create a user.
	 * @param username
	 * @param password
	 * @return
	 */
	public User createUser(String username, String password){
		 User user = new User();
		 user.setUsername(username);
		 user.setPwd(password);
		 // for User, we can create new ones without an existing User
		 Long id = create(null, user);

		 return get(null,id).get();
	}
		
}
