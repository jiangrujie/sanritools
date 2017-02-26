package com.sanri.codegenerate.meta;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sanri.utils.PathUtil;
import sanri.utils.StringUtil;
import sanri.utils.Validate;
/**
 * 
 * 作者:sanri</br>
 * 时间:2016-9-26上午11:00:23</br>
 * 功能:数据库元数据管理<br/>
 */
public class MetaManager {
	private static Configuration properties = null;
	private static Log logger = LogFactory.getLog(MetaManager.class);
	
	private static List<Table> tables = new ArrayList<Table>();
	private static Map<String,List<Column>> columnMap = new HashMap<String, List<Column>>();
	
	static{
		String metaPath = PathUtil.pkgPath("com.sanri.codegenerate.meta");
		try {
			properties = new PropertiesConfiguration(metaPath+File.separator+"jdbc.properties");
			String driverClass = properties.getString("driverClass");
			if(driverClass.indexOf("oracle") != -1){
				properties.setProperty("jdbcType", "oracle");
			}else if(driverClass.indexOf("mysql") != -1){
				properties.setProperty("jdbcType", "mysql");
			}else{
				logger.error("不支持的数据库类型"+driverClass);
			}
			init();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * 功能:查找  mysql 表的注释 <br/>
	 * 创建时间:2016-9-29下午9:58:20<br/>
	 * 作者：sanri<br/>
	 * 出参说明：表名=>注释 <br/>
	 * @param conn 
	 * @return<br/>
	 * @throws SQLException 
	 */
	private static Map<String,String> findTableComments(Connection conn,String spellingRule) throws SQLException{
		Map<String,String> commentsMap = new HashMap<String, String>();
		PreparedStatement prepareStatement = conn.prepareStatement("show table status");
		ResultSet resultSet = prepareStatement.executeQuery();
		while(resultSet.next()){
			String tableName = ObjectUtils.toString(resultSet.getString("name")).toLowerCase();
			String comments = resultSet.getString("comment");
			
			if("upper".equals(spellingRule)){
				tableName = tableName.toUpperCase();
			}
			commentsMap.put(tableName, comments);
		}
		resultSet.close();
		prepareStatement.close();
		return commentsMap;
	}
	/**
	 * 
	 * 作者:sanri</br>
	 * 时间:2016-9-26下午2:32:53</br>
	 * 功能:初始化元数据</br>
	 */
	private static void init() throws SQLException {
		String spellingRule = properties.getString("spellingRule");
		String jdbcType = properties.getString("jdbcType");
		Connection conn = conn();
		Map<String, String> commonsMap = null;
		if("mysql".equals(jdbcType)){
			commonsMap = findTableComments(conn,spellingRule);
		}
		DatabaseMetaData metaData = conn.getMetaData();
		
		//schema 
		String schema = properties.getString("schema");
		if(StringUtil.isBlank(schema)){
			schema = properties.getString("user").toUpperCase();
		}
		ResultSet tablesResultSet = metaData.getTables(null, schema, null, new String[] { "TABLE" });
		ResultSet columnsResultSet = metaData.getColumns(null, schema, null, null);
		
		//获取所有列
		while(columnsResultSet.next()){
			String tableName = columnsResultSet.getString("TABLE_NAME");
			String columnName = columnsResultSet.getString("COLUMN_NAME");		//列名
			String columnType = columnsResultSet.getString("TYPE_NAME");		//类型名
			String columnComment = columnsResultSet.getString("REMARKS");		//备注
			int length = columnsResultSet.getInt("COLUMN_SIZE");				//长度
			int precision = columnsResultSet.getInt("DECIMAL_DIGITS");			//精度
			String nullable = columnsResultSet.getString("IS_NULLABLE");		//是否可以空
			boolean nullableBool = "no".equalsIgnoreCase(nullable) ? false : true;
			
			Column column = new Column();
			List<Column> columns = null;
			if("upper".equals(spellingRule)){
				columns = columnMap.get(tableName.toUpperCase());
				if(columns == null){
					columns = new ArrayList<Column>();
					columnMap.put(tableName.toUpperCase(), columns);
				}
			}else{
				columns = columnMap.get(tableName.toLowerCase());
				if(columns == null){
					columns = new ArrayList<Column>();
					columnMap.put(tableName.toLowerCase(), columns);
				}
			}
			columns.add(column);
			column.setLength(length);
			column.setPrecision(precision);
			column.setNullable(nullableBool);
			column.setComments(columnComment);
			if("upper".equals(spellingRule)){
				column.setColumnName(columnName.toUpperCase());
				column.setDataType(columnType.toUpperCase());
				continue;
			}
			column.setColumnName(columnName.toLowerCase());
			column.setDataType(columnType.toLowerCase());
		}
		//获取所有表
		while(tablesResultSet.next()){
			Table table = new Table();
			String tableName = tablesResultSet.getString("TABLE_NAME");		//表名
			if("upper".equals(spellingRule)){
				table.setTableName(tableName.toUpperCase());
			}else{
				table.setTableName(tableName.toLowerCase());
			}
			
			String tableComments = tablesResultSet.getString("REMARKS");		//注释 mysql 这样获取不到注释  
			if("mysql".equals(jdbcType) && commonsMap != null){
				tableComments = commonsMap.get(tableName);
			}
			table.setComments(tableComments);
			tables.add(table);
			getColumns(table);
		}
		
		tablesResultSet.close();
		columnsResultSet.close();
	}
	/**
	 * 
	 * 作者:sanri</br>
	 * 时间:2016-9-26下午2:35:29</br>
	 * 功能:获取表的列</br>
	 * @throws SQLException 
	 */
	public static List<Column> getColumns(Table table) throws SQLException {
		String currentTableName = table.getTableName();
		List<Column> columns = columnMap.get(currentTableName);
		table.setColumns(columns);
		return columns;
	}
	/**
	 * 
	 * 功能: get connection
	 * 创建时间:2016-4-15下午11:38:48
	 * 作者：sanri
	 * 入参说明:
	 *　出参说明:{@link Connection}
	 * @return
	 */
	public static Connection conn(){
		Connection conn = null;
		String driver = properties.getString("driverClass");
		String url = properties.getString("jdbcUrl");
		String user = properties.getString("user");
		String pass = properties.getString("password");
		try {
			Properties props =new Properties();
            props.put("remarksReporting","true");
            props.put("user", user);
            props.put("password", pass);
			Class.forName(driver);
			conn = DriverManager.getConnection(url,props);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return conn;
	}
	
	/**
	 * 
	 * 作者:sanri</br>
	 * 时间:2016-9-26上午10:47:47</br>
	 * 功能:获取所有表名</br>
	 */
	public static List<String> allTableName() {
		List<String> tableNames = new ArrayList<String>();
		if(!Validate.isEmpty(tables)){
			for (Table table : tables) {
				tableNames.add(table.getTableName());
			}
		}
		return tableNames;
	}
	
	/**
	 * 
	 * 功能:获取指定表<br/>
	 * 创建时间:2016-9-25下午2:29:47<br/>
	 * 作者：sanri<br/>
	 * 入参说明:表名<br/>
	 * 出参说明：所有的表,会把列也查找出来<br/>
	 */
	public static List<Table> getTables(String... tableNames){
		List<Table> findTables = new ArrayList<Table>();
		if(Validate.isEmpty(tableNames)){return findTables;}
		
		String tableNamesStr = StringUtil.join(tableNames,",").toLowerCase();
		if(!Validate.isEmpty(tables)){
			for (Table table : tables) {
				String tableName = table.getTableName().toLowerCase();
				if(tableNamesStr.indexOf(tableName) != -1){
					findTables.add(table);
				}
			}
		}
		return findTables;
	}
	/**
	 * 
	 * 作者:sanri</br>
	 * 时间:2016-9-29下午3:14:18</br>
	 * 功能:所有的表</br>
	 */
	public static List<Table> allTables(){
		return tables;
	}
	
	public static void main(String[] args) {
		System.out.println(tables.get(2).getColumns());
	}
}
