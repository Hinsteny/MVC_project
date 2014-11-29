package com.oci.web;

import com.google.inject.Singleton;

/**
 * <p>The WebResponse factory. Any @Web[REST] that needs to return a JSON data to the client should return a WebResponse built by this Injected factory.</p>
 *
 * <p>This Guice managed WebResponse factory pattern allows to add all sort of useful information across all WebResponse.
 * For example, basic performance information could be added here for all WebResponse of the application without changing any @Web[REST] code.</p>
 *
 */
@Singleton
public class WebResponseBuilder {



	// --------- WebResponse Factories --------- //
	public WebResponse success(){
		return newWebResponse().setSuccess(true);
	}

	public WebResponse success(Object result){
		return success().setResult(result);
	}

	public WebResponse fail(){
		return newWebResponse().setSuccess(false);
	}

	public WebResponse fail(Throwable t){
		return fail().setThrowable(t);
	}
	// --------- /WebResponse Factories --------- //


	private WebResponse newWebResponse(){
		WebResponse webResponse = new WebResponse();
		return webResponse;
	}
}
