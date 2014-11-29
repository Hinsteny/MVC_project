package com.oci.perf;

import com.britesnow.snow.web.rest.annotation.WebGet;
import com.britesnow.snow.web.rest.annotation.WebPost;
import com.oci.dao.DaoHelper;
import com.oci.web.WebResponse;
import com.oci.web.WebResponseBuilder;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * <p>WebRest methods to get and refresh the Perf info</p>
 */
@Singleton
public class PerfWebRest {

	@Inject
	private WebResponseBuilder wrb;

	@Inject
	private PerfManager perfManager;

	@Inject
	DaoHelper daoHelper;

	@WebGet("/perf-get-all")
	public WebResponse getAllPerf(){

		AppPerf appPerf = perfManager.getAppPerf(daoHelper.getPoolInfo());

		return wrb.success(appPerf);
	}

	@WebPost("/perf-clear")
	public WebResponse clearPerf(){
		perfManager.clear();
		return wrb.success(true);
	}

}
