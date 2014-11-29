package com.oci.test;

import static com.britesnow.snow.util.MapUtil.mapIt;
import static org.j8ql.query.Query.insert;

import java.util.Map;

import org.j8ql.query.InsertQuery;
import org.junit.Before;
import org.junit.BeforeClass;

import com.britesnow.snow.testsupport.SnowTestSupport;
import com.oci.dao.DaoHelper;
import com.oci.entity.User;

public class BaseTestSupport  extends SnowTestSupport{
	static protected Map defaultTestUsernamePassword = mapIt("username","test_user-01","pwd","welcome");
	static protected Object[][] testUsers = {{"test_user-01","welcome"},{"test_user-02","welcome"}};
	static private InsertQuery<User> testUserInsert = insert("user").columns("username", "pwd").returning(User.class);

    @BeforeClass
    public static void initTestClass() throws Exception {
        SnowTestSupport.initWebApplication("src/main/webapp");
    }

	@Before
	public void before(){
		cleanTables();
	}

	protected void cleanTables(){
		DaoHelper daoHelper = appInjector.getInstance(DaoHelper.class);


		daoHelper.executeUpdate("delete from chain where name like 'test_%'");
		daoHelper.executeUpdate("delete from row where name like 'test_%'");
		daoHelper.executeUpdate("delete from item where name like 'test_%'");
		daoHelper.executeUpdate("delete from \"user\" where username like 'test_%'");

		//da.create("user", defaultTestUsernamePassword);
	}

	protected User createTestUser1(){
		DaoHelper daoHelper = appInjector.getInstance(DaoHelper.class);
		return daoHelper.execute(testUserInsert.values(testUsers[0]));
	}

	protected User createTestUser2(){
		DaoHelper daoHelper = appInjector.getInstance(DaoHelper.class);
		return daoHelper.execute(testUserInsert.values(testUsers[1]));
	}
    
}
