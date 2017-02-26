package com.sanri.app.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sanri.utils.PathUtil;
import sanri.utils.StringUtil;
import sanri.utils.Validate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sanri.codegenerate.meta.MetaManager;
import com.sanri.codegenerate.meta.Table;

/**
 * 
 * 创建时间:2016-10-3上午8:09:35<br/>
 * 创建者:sanri<br/>
 * 功能:sql 客户端,依赖 jar jsqlparser 主要用于 sql 语句的解析 <br/>
 */
public class SqlClientServlet  extends BaseServlet{
	private Log logger = LogFactory.getLog(SqlClientServlet.class);
	private static File sqlBaseDir;
	
	public SqlClientServlet(){
		this.setServletClass(SqlClientServlet.class);
	}
	
	static{
		sqlBaseDir = new File(PathUtil.webAppsPath()+"/sql");
		if(!sqlBaseDir.exists()){
			sqlBaseDir.mkdir();
		}
	}
	/**
	 * 
	 * 作者:sanri</br>
	 * 时间:2016-9-29下午3:16:38</br>
	 * 功能:查询所有的表</br>
	 */
	public void tables(HttpServletRequest request,HttpServletResponse response) throws IOException{
		List<Table> allTables = MetaManager.allTables();
		String jsonString = JSONObject.toJSONString(allTables);
		PrintWriter writer = response.getWriter();
		writer.write(jsonString);
		writer.flush();
		writer.close();
	}
	/**
	 * 
	 * 功能:跨域请求返回<br/>
	 * 创建时间:2016-10-9下午5:27:31<br/>
	 * 作者：sanri<br/>
	 */
	public void tablesp(HttpServletRequest request,HttpServletResponse response){
		List<Table> allTables = MetaManager.allTables();
		String jsonString = JSONObject.toJSONString(allTables);
		String callbackFunName   = request.getParameter("callback");
		writeString(callbackFunName+"("+jsonString+")", response);
	}
	/**
	 * 
	 * 功能:保存 sql<br/>
	 * 创建时间:2016-9-30上午11:44:28<br/>
	 * 作者：sanri<br/>
	 */
	public void saveSql(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String sql = request.getParameter("sql");
		String nowDay = DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd");
		File filePath = new File(sqlBaseDir,nowDay);
		FileUtils.writeStringToFile(filePath, sql);
	}
	
	public void readSql(HttpServletRequest request,HttpServletResponse response) throws IOException{

	}
	/**
	 * 
	 * 功能:执行 sql<br/>
	 * 创建时间:2016-9-30上午11:46:04<br/>
	 * 作者：sanri<br/>
	 * @throws SQLException 
	 * 返回数据结构为{"select * from answer_append":{"body":[{"id":1,"answerid":"xxx"...},{...}],"head":["id","answerid","pid","user_code","user_name","content","time"]}}
	 */
	public void executeSql(HttpServletRequest request,HttpServletResponse response) throws IOException, SQLException{
		JSONObject json = getJson(request);
		String executeSqls = (String) json.get("executeSqls");
		Map<String,String> result = new HashMap<String, String>();
		if(StringUtil.isBlank(executeSqls)){
			result.put("-1", "no result set");
			writeString(JSONObject.toJSONString(result), response);
			return ;
		}
		String[] executorSqlArray = executeSqls.replaceAll("\\n", "").split(";");
		
		//循环执行 sql 并返回结果
		Map<String,Object> resultSets = new HashMap<String, Object>();
		if(!Validate.isEmpty(executorSqlArray)){
			Connection conn = MetaManager.conn();
			PreparedStatement ps = null;ResultSet rs = null;
			String operator = "select";	//select insert update delete alter drop
			
			for (String sql : executorSqlArray) {
				//单条 sql 的异常不要影响全局
				try{
					sql = sql.trim();
					String[] split = sql.split(" ");
					if(!Validate.isEmpty(split)){
						operator = split[0];
						ps = conn.prepareStatement(sql);
						if("select".equalsIgnoreCase(operator)){
							rs = ps.executeQuery();
							ResultSetMetaData metaData = rs.getMetaData();
							int columnCount = metaData.getColumnCount();
							JSONArray head = new JSONArray();
							for (int i = 1; i <= columnCount; i++) {
								head.add(metaData.getColumnName(i));
							}
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("head", head);
							List<Map> data = new ArrayList<Map>();
							while(rs.next()){
								Map dataObj = new HashMap();
								for (int i = 1; i <= columnCount; i++) {
									dataObj.put(metaData.getColumnName(i), rs.getObject(i));
								}
								data.add(dataObj);
							}
							jsonObject.put("body", data);
							
							resultSets.put(sql, jsonObject);
							continue;
						}
						boolean execute = ps.execute();
						if(!execute){
							resultSets.put(sql, "no result set ; sql 执行失败");
						}
					}
				}catch (SQLException e){
					resultSets.put(sql, "no result set ; sql error");
					e.printStackTrace();
				}
			}
		}
		writeString(JSONObject.toJSONString(resultSets), response);
	}
	/**
	 * 
	 * 功能: sql 解析<br/>
	 * 创建时间:2016-10-3上午7:59:09<br/>
	 * 作者：sanri<br/>
	 * @deprecated 不进行 sql 解析
	 */
	private void parseSql(String executeSql) throws IllegalArgumentException{
		
	}
	@Override
	protected String getRootPath() {
		return "/jdbc/";
	}
}