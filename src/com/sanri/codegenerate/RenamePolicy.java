package com.sanri.codegenerate;

public interface RenamePolicy {
	/**
	 * 
	 * 功能:从表名映射到类名<br/>
	 * 创建时间:2016-9-25下午3:15:09<br/>
	 * 作者：sanri<br/>
	 */
	String mapperClassName(String tableName);
	/**
	 * 
	 * 功能:从列名映射到字段名<br/>
	 * 创建时间:2016-9-25下午3:15:09<br/>
	 * 作者：sanri<br/>
	 */
	String mapperPropertiesName(String columnName);
}