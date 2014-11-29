package com.oci;

public class AppException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1197777655236793747L;
	
	private Enum errorEnum;

	public AppException(Enum errorEnum){

		this.errorEnum = errorEnum;
	}

	public AppException(String message) {
		super(message);
	}
	
	public Enum getErrorEnum(){
		return this.errorEnum;
	}
	
	public String getErrorCode(){
		if (errorEnum != null){
			return errorEnum.name();
		}else{
			return null;
		}
	}

	public String getMessage(){
		if (errorEnum != null){
			if (errorEnum instanceof ErrorEnum){
				return ((ErrorEnum)errorEnum).getMessage();
			}else{
				return getErrorCode();
			}
		}else{
			return super.getMessage();
		}
	}
}
