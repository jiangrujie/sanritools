package com.sanri.codegenerate;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sanri.utils.PathUtil;
import sanri.utils.StringUtil;
import sanri.utils.Validate;

import com.sanri.codegenerate.meta.Column;
import com.sanri.codegenerate.meta.MetaManager;
import com.sanri.codegenerate.meta.Table;

public class BeanFromDB {
	public static Configuration TYPE_MAPPER = null;
	public static Configuration GENERATE_CONFIG = null;
	private static Log logger = LogFactory.getLog(BeanFromDB.class);
	
	static{
		String typePath = PathUtil.pkgPath("com.sanri.codegenerate");
		try {
			TYPE_MAPPER = new PropertiesConfiguration(typePath + File.separator+"mapper_jdbc_java.properties");
			GENERATE_CONFIG = new PropertiesConfiguration(typePath + File.separator + "generate_config.properties");
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * 作者:sanri</br>
	 * 时间:2016-9-26上午10:44:08</br>
	 * 功能:javaBean 生成 </br>
	 * @return 
	 */
	public static List<JavaBean>  generate(String[] findTableNames){
		String basePath = GENERATE_CONFIG.getString("basePath");
		long startTime = System.currentTimeMillis();
		logger.info("开始生成 bean 基路径为:"+basePath);
		//构建 javaBean 结构
		List<JavaBean> buildJavaBeans = buildJavaBeans(findTableNames);
		//文件生成
		File beanDir = new File(basePath+File.separator+"bean");
		beanDir.mkdirs();
		for (JavaBean javaBean : buildJavaBeans) {
			List<String> build = javaBean.build();
			File writerBean = javaBean.writerBean(build, beanDir);
			logger.info("javaBean 生成:"+writerBean);
		}
		
		logger.info("生成完成所用时间为:"+(System.currentTimeMillis() - startTime) + " ms");
		return buildJavaBeans;
	}
	
	public static List<JavaBean> generate(){
		String[] findTableNames = findNeedGenerateTables();
		return generate(findTableNames);
	}

	/**
	 * 
	 * 功能:查找需要生成的表<br/>
	 * 创建时间:2016-9-30下午5:08:14<br/>
	 * 作者：sanri<br/>
	 */
	public static String[] findNeedGenerateTables() {
		List<String> tablePatterns = GENERATE_CONFIG.getList("tables");
		String [] findTableNames = null;
		List<String> allTableName = MetaManager.allTableName();
		String allTableNames = StringUtil.join(allTableName,",");
		
		
		if(!Validate.isEmpty(tablePatterns)){
			for (String pattern : tablePatterns) {
				if("*".equals(pattern)){
					findTableNames = allTableName.toArray(new String []{});
					break;
				}
				findTableNames = Validate.match(allTableNames, Pattern.compile(pattern));
			}
		}
		return findTableNames;
	}
	
	
	/**
	 * 作者:sanri</br>
	 * 时间:2016-9-26下午2:00:27</br>
	 * 功能:根据表名列表,构建 javaBean</br>
	 */
	@SuppressWarnings("unchecked")
	public static List<JavaBean> buildJavaBeans(String [] findTableNames) {
		String beanPackage = GENERATE_CONFIG.getString("beanPackage");
		String beanExtends = GENERATE_CONFIG.getString("beanExtends");
		List<String> excludeColumns = GENERATE_CONFIG.getList("beanExcludeColumns");
		
		List<JavaBean> javaBeans = new ArrayList<JavaBean>();
		if(!Validate.isEmpty(findTableNames)){
			List<Table> tables = MetaManager.getTables(findTableNames);
			if(!Validate.isEmpty(findTableNames)){
				for (Table table : tables) {
					// table <1-1> javaBean
					JavaBean javaBean = new JavaBean();
					javaBeans.add(javaBean);

					javaBean.setTable(table);
					javaBean.setExtendsName(beanExtends);
					javaBean.setExcludeColumns(excludeColumns);
					javaBean.setClassName(renamePolicy.mapperClassName(table.getTableName()));
					String classComment = table.getComments();
					if(StringUtil.isBlank(classComment)){
						classComment = "@ generate from table "+table.getTableName();
					}
					javaBean.setClassComment(classComment);
					javaBean.setPackageName(beanPackage);
					List<Column> columns = table.getColumns();
					if(!Validate.isEmpty(columns)){
						Map<String,String> propertysMap = new HashMap<String, String>();
						Map<String,String> propertysComments = new HashMap<String, String>();
						javaBean.setPropertys(propertysMap);
						javaBean.setPropertysComments(propertysComments);
						
						Map<String,Column> columnsMap = new HashMap<String, Column>();
						javaBean.setColumns(columnsMap);
						for (Column column : columns) {
							String propertyName = renamePolicy.mapperPropertiesName(column.getColumnName());
							String propertyType = TYPE_MAPPER.getString(column.getDataType());
							if(propertyType == null){
								logger.error("列类型找不到映射默认使用 String "+column.getDataType());
								propertyType = "String";
							}
							String comments = column.getComments();
							
							columnsMap.put(propertyName, column);
							propertysMap.put(propertyName, propertyType);
							propertysComments.put(propertyName, comments);
						}
					}
				}
			}
		}
		return javaBeans;
	}
	
	public final static RenamePolicy renamePolicy = new RenamePolicyDefault();
	
	public static void main(String[] args) {
		BeanFromDB.generate();
	}
}
