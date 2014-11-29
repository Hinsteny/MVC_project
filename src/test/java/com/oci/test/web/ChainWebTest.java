package com.oci.test.web;

import static com.britesnow.snow.util.MapUtil.mapIt;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.britesnow.snow.testsupport.mock.RequestContextMock;
import com.britesnow.snow.util.JsonUtil;
import com.oci.dao.UserDao;
import com.oci.entity.User;
import com.oci.test.BaseTestSupport;

public class ChainWebTest extends BaseTestSupport {

	@Test
	public void chainAndUser(){
		List<Map> list;

		UserDao userDao = appInjector.getInstance(UserDao.class);

		User user1 = userDao.createUser("test_demouser_1","welcome");
		User user2 = userDao.createUser("test_demouser_2","welcome");

		Map user1CookieMap = doPost("/login", mapIt("username", user1.getUsername(), "pwd", user1.getPwd())).getCookieMap();
		Map user2CookieMap = doPost("/login", mapIt("username", user2.getUsername(), "pwd", user1.getPwd())).getCookieMap();

		String chainJsonStr;
		RequestContextMock rc;

		chainJsonStr = JsonUtil.toJson(mapIt("name", "test_chain_1_user1"));
		rc = doPost("/das-create-chain",mapIt("props",chainJsonStr), user1CookieMap);
		chainJsonStr = JsonUtil.toJson(mapIt("name", "test_chain_2_user1"));
		rc = doPost("/das-create-chain",mapIt("props",chainJsonStr), user1CookieMap);

		chainJsonStr = JsonUtil.toJson(mapIt("name", "test_chain_1_user2"));
		rc = doPost("/das-create-chain",mapIt("props",chainJsonStr), user2CookieMap);

		Map data = new HashMap();
		data.put("pageIndex", "0");
		data.put("pageSize", "15");
		data.put("orderBy", "id");
		data.put("orderType", "asc");
		rc = doGet("/das-list-chain", data, user1CookieMap);
		list = (List<Map>) rc.getResponseAsJson().get("result");
		assertEquals(2,list.size());

		rc = doGet("/das-list-chain",data, user2CookieMap);
		list = (List<Map>) rc.getResponseAsJson().get("result");
		assertEquals(1,list.size());

	}
}
