package com.sanri.codegenerate.ssm;
/**
 * 
 * 作者:sanri</br>
 * 时间:2016-9-26下午4:16:35</br>
 * 功能:对应mybatis 中的 resultMap<br/>
 */
public class ResultMap {
	private String column;
	private String property;
	private String jdbcType = "VARCHAR";
	
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getJdbcType() {
		return jdbcType;
	}
	public void setJdbcType(String jdbcType) {
		this.jdbcType = jdbcType;
	}
	
}
