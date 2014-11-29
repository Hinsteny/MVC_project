package com.oci.web;

import java.util.List;

import org.j8ql.query.Condition;
import org.j8ql.query.Query;

import com.britesnow.snow.web.param.annotation.WebUser;
import com.britesnow.snow.web.rest.annotation.WebGet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.oci.dao.IDao;
import com.oci.dao.UserDao;
import com.oci.entity.Row;
import com.oci.entity.User;
import com.oci.perf.annotation.ToMonitor;

@Singleton
@ToMonitor
public class UserWebHandler {

    @Inject
    private UserDao userDao;

	@Inject
	private IDao<User,Long> udao;

	@Inject
	private IDao<Row,Long> ticketDao;

	@Inject
	private WebResponseBuilder webResponseBuilder;


    @WebGet("/das-list-user")
    public WebResponse listUser(@WebUser User user){
        List<User> users = userDao.list(user,Query.one("1", "=1"),0,100,"name");
        return webResponseBuilder.success(users);
    }

}
