package com.oci.web;

import java.util.List;
import java.util.Map;

import com.britesnow.snow.web.param.annotation.PathVar;
import com.britesnow.snow.web.param.annotation.WebParam;
import com.britesnow.snow.web.param.annotation.WebUser;
import com.britesnow.snow.web.rest.annotation.WebGet;
import com.britesnow.snow.web.rest.annotation.WebPost;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.oci.AppException;
import com.oci.dao.DaoRegistry;
import com.oci.dao.IDao;
import com.oci.entity.User;
import com.oci.perf.annotation.ToMonitor;
import com.oci.web.annotation.EntityIdParam;
import com.oci.web.annotation.JsonParam;

/**
 * <p>This is the generic Data Access Service layer from the server.</p>
 *
 * <p><strong>Note:</strong> Non parameterized @WebRest (i.e. @WebGet("/das-get-Project") will take precedence over
 * parameterized ones (i.e. @WebGet("das-get-{entity}"). Therefore, this GenericDasWebRest can been seen as a fall back
 * fall back when no specialized DasWebRest methods are handling specific entity.</p>
 *
 * <p><strong>Best Practice:</strong> Often, when starting a new project, this is a great and and simple way to start connecting
 * your client to those Web[REST] APIs. As the application becomes more sophisticated, you can override those generic bindings with
 * more specifics ones (e.g., for added security and business rules) without ever changing the UI code.</p>
 *
 */
@Singleton
@ToMonitor
public class GenericDasWebRest {

	@Inject
	private DaoRegistry daoRegistry;

	@Inject
	private WebResponseBuilder webResponseBuilder;

	@WebPost("/das-create-{entity}")
	public WebResponse createEntity(@WebUser User user, @PathVar("entity")String entityType, @JsonParam("props") Map props){
		IDao dao = daoRegistry.getDao(entityType);

		// TODO: probably need to have a createWithReturn
		Object id = dao.create(user, props);
		Object entity = dao.get(user, id).orElse(null);
		return webResponseBuilder.success(entity);
	}

	@WebGet("/das-get-{entity}")
	public WebResponse getEntity(@WebUser User user, @PathVar("entity")String entityType, @EntityIdParam("id") Map id){
		IDao dao = daoRegistry.getDao(entityType);
		Object entity = dao.get(user, id).orElse(null);

		return webResponseBuilder.success(entity);
	}

	@WebGet("/das-list-{entity}")
	public WebResponse listEntity(@WebUser User user, @PathVar("entity")String entityType, @WebParam("pageIndex") Integer pageIndex,
			@WebParam("pageSize") Integer pageSize,@JsonParam("filter") Map filter, @WebParam("orderBy") String orderBy,
			@WebParam("orderType") String orderType){
		IDao dao = daoRegistry.getDao(entityType);
		List<Object> list = null;
		if(filter != null){
			list = dao.list(user, pageIndex, pageSize, orderType, filter, orderBy.split(","));
		}else{
			list = dao.list(user, null, pageIndex, pageSize, orderType, orderBy.split(","));
		}
		return webResponseBuilder.success(list);
	}

	@WebPost("/das-update-{entity}")
	public WebResponse updateEntity(@WebUser User user, @PathVar("entity")String entityType, @EntityIdParam("id") Map id, @JsonParam("props") Map props){
		IDao dao = daoRegistry.getDao(entityType);
		int r = dao.update(user, props, id);
		return webResponseBuilder.success(r);
	}

	@WebPost("/das-delete-{entity}")
	public WebResponse deleteEntity(@WebUser User user, @PathVar("entity")String entityType, @EntityIdParam("id") Map idMap){
		IDao dao = daoRegistry.getDao(entityType);

		int numDeleted = dao.delete(user, idMap);

		if (numDeleted > 0){
			return webResponseBuilder.success(idMap);
		}else{
			return webResponseBuilder.fail(new AppException("Cannot delete " + entityType + " with id " + idMap));
		}

	}

}
