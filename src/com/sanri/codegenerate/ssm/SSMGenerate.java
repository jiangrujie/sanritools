package com.sanri.codegenerate.ssm;

import java.beans.Introspector;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sanri.utils.PathUtil;
import sanri.utils.StringUtil;
import sanri.utils.Validate;
import sanri.utils.VelocityUtil;

import com.sanri.codegenerate.BeanFromDB;
import com.sanri.codegenerate.JavaBean;
import com.sanri.codegenerate.meta.Column;
import com.sanri.codegenerate.meta.Table;

public class SSMGenerate {
	private static Configuration MYBATIS_TYPE_MAPPER = null;
	private static Log logger = LogFactory.getLog(SSMGenerate.class);
	
	static{
		String generateConfigPath = PathUtil.pkgPath("com.sanri.codegenerate");
		try {
			MYBATIS_TYPE_MAPPER = new PropertiesConfiguration(generateConfigPath+File.separator+"mapper_java_mybatis.properties");
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * 功能:ssm 代码模板构建,首先会生成 bean<br/>
	 * 创建时间:2016-9-30下午5:10:22<br/>
	 * 作者：sanri<br/>
	 * @throws IOException 
	 */
	public static void generate() throws IOException{
		//先生成 bean 
		String[] findTableNames = BeanFromDB.findNeedGenerateTables();
		List<JavaBean> javaBeans = BeanFromDB.generate(findTableNames);
		File baseDir = new File(BeanFromDB.GENERATE_CONFIG.getString("basePath"));
		
		long startTime = System.currentTimeMillis();
		String timeFormat = "yyyy-MM-dd HH:mm:ss";
		logger.info("三层组件生成基路径为"+baseDir+":"+DateFormatUtils.format(startTime, timeFormat));
		//获取配置
		String controllerPackage = BeanFromDB.GENERATE_CONFIG.getString("controllerPackage");
		String voPackage = BeanFromDB.GENERATE_CONFIG.getString("voPackage");
		String servicePackage = BeanFromDB.GENERATE_CONFIG.getString("servicePackage");
		String serviceImplPackage = BeanFromDB.GENERATE_CONFIG.getString("serviceImplPackage");
		String daoPackage = BeanFromDB.GENERATE_CONFIG.getString("daoPackage");
		
		//目录生成
		File controllerDir = new File(baseDir,"controller");
		File serviceDir = new File(baseDir,"service");
		File serviceImplDir = new File(serviceDir,"impl");
		File mapperDir = new File(baseDir,"mapper");
		File xmlDir = new File(baseDir,"xml");
		dirCreate(baseDir,controllerDir,serviceDir,serviceImplDir,mapperDir,xmlDir);
		
		if(!Validate.isEmpty(javaBeans)){
			Table generateTable = null;
			Map<String,Object> context = null;
			
			//每个表的属性列表和列名列表
			List<String> propertys = null;
			List<Column> columns = null;
			//resultMap 配置 
			List<ResultMap> resultMaps = null;
			ResultMap resultMap = null;
			
			for (JavaBean javaBean : javaBeans) {
				generateTable = javaBean.getTable();
				Map<String, Column> propertyMap = javaBean.getColumns();
				
				//创建 resultMap
				Map<String, String> propertysMap = javaBean.getPropertys();
				propertys = new ArrayList<String>(propertysMap.keySet());
				columns = generateTable.getColumns();
				resultMaps = new ArrayList<ResultMap>();
				for (int i=0;i<propertys.size();i++) {
					String property = propertys.get(i);
					
					resultMap = new ResultMap();
					resultMap.setColumn(propertyMap.get(property).getColumnName());
					resultMap.setProperty(property);
					resultMap.setJdbcType(MYBATIS_TYPE_MAPPER.getString(propertysMap.get(property)));
					
					resultMaps.add(resultMap);
				}

				
				//创建上下文
				context = new HashMap<String, Object>();
				context.put("controllerPackage", controllerPackage);
				context.put("servicePackage", servicePackage);
				context.put("serviceImplPackage", serviceImplPackage);
				context.put("daoPackage", daoPackage);
				context.put("voPackage", voPackage);
				String className = javaBean.getClassName();
				context.put("entity", className);
				context.put("lowEntity", Introspector.decapitalize(className));
				String tableComments = generateTable.getComments();
				if(!StringUtil.isBlank(tableComments)){
					tableComments = generateTable.getTableName();
				}
				context.put("chineseEntity",tableComments );
				context.put("tableName", generateTable.getTableName());
				context.put("propertys", propertys);
				context.put("columns", columns);
				context.put("resultMaps", resultMaps);
				
				//文件生成
				File serviceFile = new File(serviceDir,"I"+javaBean.getClassName()+"Service.java");
				File serviceImplFile = new File(serviceImplDir,javaBean.getClassName()+"ServiceImpl.java");
				File mapperFile = new File(mapperDir,javaBean.getClassName()+"Mapper.java");
				File controllerFile = new File(controllerDir,javaBean.getClassName()+"Controller.java");
				File xmlFile = new File(xmlDir,javaBean.getClassName()+"Mapper.xml");
				
				VelocityUtil.generateFile("/tpl/service.tpl", context, serviceFile);
				VelocityUtil.generateFile("/tpl/serviceImpl.tpl", context, serviceImplFile);
				VelocityUtil.generateFile("/tpl/mapper.tpl", context, mapperFile);
				VelocityUtil.generateFile("/tpl/controller.tpl", context, controllerFile);
				VelocityUtil.generateFile("/tpl/xml.vm", context, xmlFile);
				
				logFilePath(serviceFile,serviceImplFile,mapperFile,controllerFile,xmlFile);
			}
		}
		long spendTime = System.currentTimeMillis() - startTime;
		logger.info("自动生成花费时间:"+spendTime+" ms");
	}
	
	private static void logFilePath(File... files){
		for (File file : files) {
			logger.info("文件生成:"+file);
		}
	}
	
	/*
	 * 功能:目录循环创建,如果不存在,则创建<br/>
	 * 注:必须父级目录存在</br/>
	 * 创建时间:2016-9-30下午5:15:58<br/>
	 * 作者：sanri<br/>
	 */
	private static void dirCreate(File...files){
		for (int i = 0; i < files.length; i++) {
			File currentFile = files[i];
			if(currentFile != null && !currentFile.exists()){
				logger.info("目录创建"+currentFile);
				currentFile.mkdir();
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			SSMGenerate.generate();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
