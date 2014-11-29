package com.oci.test.web;

import com.britesnow.snow.testsupport.mock.RequestContextMock;
import com.britesnow.snow.util.JsonUtil;
import com.oci.dao.DaoRegistry;
import com.oci.dao.IDao;
import com.oci.entity.Chain;
import com.oci.test.BaseTestSupport;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static com.britesnow.snow.util.MapUtil.getDeepValue;
import static com.britesnow.snow.util.MapUtil.mapIt;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BasicDasWebRestTest extends BaseTestSupport {


	@Test
	public void simpleProjectCreateWebRestTest(){
		createTestUser1();

		String chainName = "test_chain_single";

		Map authCookieMap = doPost("/login",defaultTestUsernamePassword).getCookieMap();

		String chainJsonStr = JsonUtil.toJson(mapIt("name", chainName));
		RequestContextMock rc = doPost("/das-create-chain",mapIt("props",chainJsonStr), authCookieMap);

		// get and check the result
		Map result = rc.getResponseAsJson();

		assertEquals(chainName, getDeepValue(result, "result.name"));
	}

	/**
	 * TODO: once this security layer will be put in place, this unit test will have to change.
	 */
	@Test
	public void simpleChainCreateAndGetWithoutAuth() {
		// create the project directly in the db using DAO
		DaoRegistry daoRegistry = appInjector.getInstance(DaoRegistry.class);
		IDao<Chain, Long> chainDao = daoRegistry.getDao(Chain.class);
		Chain chain = new Chain();
		String chainName = "test_chain-001";
		chain.setName(chainName);
		Long chainId = (Long) chainDao.create(null, chain);

		Map result, params, chainMap;

		// Test getting with a json id param
		// get the first chain via rest
		params = mapIt("id", JsonUtil.toJson(mapIt("id", chainId)));
		result = doGet("/das-get-chain", params).getResponseAsJson();
		chainMap = (Map) result.get("result");
		assertEquals(chainId.intValue(), chainMap.get("id"));
		assertEquals(chainName, chainMap.get("name"));

		// Test getting with a direct id value
		result = doGet("/das-get-chain", mapIt("id", "" + chainId)).getResponseAsJson();
		chainMap = (Map) result.get("result");
		assertEquals(chainId.intValue(), chainMap.get("id"));
		assertEquals(chainName, chainMap.get("name"));
	}


	// FIXME: kind of weird, on mvn clean package, this method hang on getting a connection?
	//        works fine when running this unit test in IntelliJ ?
	//@Test
	public void projectsCrudWebRestTest(){
		Map authCookieMap = doPost("/login",defaultTestUsernamePassword).getCookieMap();

		// create 9 projects
		for (int i = 0 ; i < 10 ; i++){
			// put half with "A" and the other one with "B" (to test like later)
			String projectName =  "test_project" + ((i<5)?"A":"B") + "_" + i;

			String projectJsonStr = JsonUtil.toJson(mapIt("name", projectName));
			RequestContextMock rc = doPost("/das-create-project",mapIt("props",projectJsonStr), authCookieMap);

			// get and check the result
			Map result = rc.getResponseAsJson();
			assertTrue((Boolean) result.get("success"));
			// check that the name match.
			assertEquals(projectName, getDeepValue(result, "result.name"));
			// check that the id is a number (MapUtil.getDeepValue... would return null if not)
			assertNotNull(getDeepValue(result,"result.id",Integer.class));
		}

		// list all the projects
		List list = (List) doGet("/das-list-project",null,authCookieMap).getResponseAsJson().get("result");
		assertEquals(10, list.size());

		// get the first project from the list
		Map firstProjectMap = (Map) list.get(0);
		// get the the id of the first one.
		Object firstProjectId = firstProjectMap.get("id");

		// get the first project via rest
		Map projectMap = (Map) doGet("/das-get-project",mapIt("id","" + firstProjectId),authCookieMap).getResponseAsJson().get("result");
		assertEquals(firstProjectId,projectMap.get("id"));

		// list the project with the "test_projectA" prefix
		Map param = mapIt("filter", JsonUtil.toJson(mapIt("name,like", "test_projectA%")));
		list = (List) doGet("/das-list-project",param,authCookieMap).getResponseAsJson().get("result");
		assertEquals(5,list.size());
	}

}
