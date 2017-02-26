package com.sanri.codegenerate;

import sanri.utils.StringUtil;
/**
 * 
 * 作者:sanri</br> 
 * 时间:2016-9-26下午2:04:07</br> 
 * 功能:默认命名策略<br/>
 */
public class RenamePolicyDefault implements RenamePolicy {

	@Override
	public String mapperClassName(String tableName) {
		if (!StringUtil.isBlank(tableName)) {
			tableName = tableName.toLowerCase();
			String[] part = tableName.split("_");
			String className = "";
			for (int i = 0; i < part.length; i++) {
				className += StringUtil.capitalize(part[i]);
			}
			return className;
		}
		return tableName;
	}

	@Override
	public String mapperPropertiesName(String columnName) {
		if (!StringUtil.isBlank(columnName)) {
			columnName = columnName.toLowerCase();
			String[] part = columnName.split("_");
			String newColumnName = part[0];
			for (int i = 1; i < part.length; i++) {
				newColumnName += StringUtil.capitalize(part[i]);
			}
			return newColumnName;
		}
		return columnName;
	}
}