package test;

import java.io.StringReader;
import java.util.List;

import junit.framework.TestCase;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.util.TablesNamesFinder;

import org.junit.Test;

/**
 * 
 * 创建时间:2016-10-3上午8:15:39<br/>
 * 创建者:sanri<br/>
 * 功能:测试 sql 解析器<br/>
 * jsqlparser
 */
public class TestJSqlParser extends TestCase {
	
	@Test
	public void testJsqlParser() throws JSQLParserException{
		String sql = "select * from adminuser;";
		Statement statement = CCJSqlParserUtil.parse(sql);
		Select selectStatement = (Select) statement;
		SelectBody selectBody = selectStatement.getSelectBody();
		System.out.println(selectBody);
		TablesNamesFinder namesFinder = new TablesNamesFinder();
		List<String> tableList = namesFinder.getTableList(selectStatement);
		System.out.println(tableList);
	}
	
	public void testParseSql() throws JSQLParserException {
		String statement = "SELECT LOCATION_D.REGION_NAME, LOCATION_D.AREA_NAME, COUNT(DISTINCT INCIDENT_FACT.TICKET_ID) FROM LOCATION_D, INCIDENT_FACT WHERE ( LOCATION_D.LOCATION_SK=INCIDENT_FACT.LOCATION_SK ) GROUP BY LOCATION_D.REGION_NAME, LOCATION_D.AREA_NAME";
		CCJSqlParserManager parserManager = new CCJSqlParserManager();
		Select select = (Select) parserManager.parse(new StringReader(statement));

		PlainSelect plain = (PlainSelect) select.getSelectBody();
		List selectitems = plain.getSelectItems();
		System.out.println(selectitems.size());
		for (int i = 0; i < selectitems.size(); i++) {
			Expression expression = ((SelectExpressionItem) selectitems.get(i)).getExpression();
			System.out.println("Expression:-" + expression);
			Column col = (Column) expression;
			System.out.println(col.getTable() + "," + col.getColumnName());
		}
	}
}
