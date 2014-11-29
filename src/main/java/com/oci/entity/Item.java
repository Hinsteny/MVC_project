package com.oci.entity;

public class Item extends BaseEntity<Long> {

	private Long rowId;
	private String name;

	public Long getRowId() {
		return rowId;
	}

	public void setRowId(Long rowId) {
		this.rowId = rowId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
