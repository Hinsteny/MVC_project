package com.oci.entity;

public class Row extends BaseEntity<Long> {

	private Long chainId;
	private String name;

	public Long getChainId() {
		return chainId;
	}

	public void setChainId(Long chainId) {
		this.chainId = chainId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
