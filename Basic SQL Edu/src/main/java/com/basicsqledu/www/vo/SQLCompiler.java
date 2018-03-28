package com.basicsqledu.www.vo;

import java.util.ArrayList;
import java.util.HashMap;

public class SQLCompiler
{
	// data 관련 변수
	private String[][] table;
	private String table_name; // table name
	private HashMap<String, Integer> columns; // String : columns_name / Integer : realdata_index
	
	
	
	private boolean grammer_error=false;
	private String text;
	private String texts[];
	private String[] COMMAND = {"CREATE", "DROP", "ALTER", "SELECT", "INSERT", "DELETE", "UPDATE"};
	private String[] keywords = 
		{
			"create", "drop", "alter", "select", "insert", "update", "delete"
			, "from", "table", "view", "schema", "sequence", "index", "column"
			, "join", "inner", "outer", "as"
			, "null", "not null", "primary key", "foreign key", "unique", "default"
			, "clustered", "nonclustered", "and", "or", "on", "set", "values"
			, "asc", "desc", "number", "varchar", "varchar2", "date", "char"
			, "(", ")", "add", "modify", "count", "sum", "max", "min", "avg"
			, "group by", "having", ">", "<", "=", ">=", "<="
			// 안 쓰지만 keyword이기 때문에 봉인한 키워드들
			, "tinytext", "text", "mediumtext", "longtext", "tinyint", "smallint", "mediumint"
			, "int", "bigint", "float", "decimal", "double", "time", "datetime", "timestamp"
			, "year", "binary", "byte", "varbinary", "tinyblob", "blob", "mediumblob", "longblob"
			// 안쓰지만 keyword 이기 때문에 적어놓음
		};
	
	public SQLCompiler()
	{
		// TODO Auto-generated constructor stub
	}
	
	public String getText()
	{
		return text;
	}
	public void setText(String text)
	{
		this.text = text;
		// 구문 분석기에 넣어서 입력
		texts = text.split("\\s|\r|\t");
		System.out.println("length="+texts.length);
		for (String s : texts) {
			if (s.equals("")) continue;
			System.out.println("["+s+"]");
		}
	}
	
	public void getTable() {
		// Mybatis로 arrayList를 꺼내온다.
		// table_name 은 구별자(gp_name)에서 
		// column_name 은 ver_name에서 갖고 온다. -> HashMap에 입력
		// table의 데이터는 ver_name을 맞춰서 hashmap에 ver_data를 입력
		
		
		
	}
	
	
	
	public void getResult() {
		for (int i = 0 ; i < texts.length; i++) {
			// 공백이면 무시하기
			if (texts[i].equals("")) {
				continue;
			}
			// select 인지 검사하기
			switch (texts[i].toLowerCase()) {
			case "create":
				break;
			case "drop":
				break;
			case "alter":
				break;
			case "insert":
				break;
			case "update":
				break;
			case "delete":
				break;
			case "select":
				i = getSelect(i);
				break;
			default:
				break;
			}
		}
	}
	/*
	 * keyword 분류
	 * 문법이 맞는지를 체크하는 것
	 * ; 를 적절한 위치에 선언했는지 체크
	 * order by가 마지막에 있는지를 체크(순서)
	 * 우리가 문제를 내는 범위에서 체크하기(insert / delete / select)
	 * ex) create 를 할 때, 컬럼 순서는 상관없음.(모든 속성이 있으면 된것)
	 * alter table_name ADD / MODIFY
	 * 
	 * 1. select를 배우기
	 *  SELECT * FROM table_name
	 * 2. where 조건절을 배우기
	 * 	SELECT * FROM table_name WHERE 조건
	 * 2-1. where 심화 - or / and 
	 * 2-2. where 심화2 - is null / not is null / 
	 * 2-3. where 심화3 - like '%' // 요거는 조금 고민해봅시다.
	 * 3. order by 정렬 기능 
	 *  SELECT * FROM table_name WHERE 조건 ORDER BY column_name ASC;
	 * 4. CREATE TABLE table_name
	 * 	( attr_name data_type |default value| | null / not null | |UNIQUE|);
	 * 5. DROP TABLE table_name;
	 * 
	 * 0. COMMIT / ROLLBACK
	 * 6. INSERT INTO table_name
	 * 	VALUES (value, ... );
	 * 6-2.
	 * 7. INSERT INTO table_name
	 * (attr_name1, attr_name2, ...)
	 * 	VALUES (value1, value2, ... );
	 * 8. DELETE table_name WHERE 조건;
	 * 9. UPDATE table_name SET attr_name = value WHERE 조건;
	 * 10. ALTER TABLE table_name ADD column_name data_type...;
	 * 10-2. ALTER TABLE table_name DROP column_name;
	 * 10-3. ALTER TABLE table_name MODIFY (COLUMN) / ALTER COLUMN column_name;
	 *  - alter table column 변경은 조금 고민해봅시다.
	 * 11. PRIMARY KEY / FOREIGN KEY
	 * 12. SUBQUERY
	 *  - SELECT * FROM table_name (SELECT * FROM table_name2);
	 * 13. GROUP FUNCTION - COUNT ( 문제는 count(*)만 내고, 나머지는 그냥 있다는 설명만 붙여준다)
	 * 13-2. GROUP BY HAVING
	 * 14. INNER JOIN / OUTER JOIN
	 * etc. 제약조건( constraint)
	 */

	private int getSelect(int index)
	{	// return 값은 i를 이용한 뒤에 +1 한 값
		int i=0;
		int stage = 1;
		for (i = index; i < texts.length; i++ ) {
			String current = texts[i];
			if (stage == 1) {// 1. keyword가 나오면 안되는 순서
				for (String s : COMMAND) {
					if (s.equals(current)) {
						return i++;
					}
				}
				// column이 나오는지 체크한 뒤
				
				// 여기는 column 이름 쓰는 곳
				// 1-2. FROM이 나오면 이 단계는 종료
				if (current.equals("FROM")) {
					stage++;
				}
			}
			else if (stage == 2) {
				// 2. FROM이 나오면 table_name 등록
				// 2-1. select인지 체크
				// table 이름 체크하기
				
				stage++;
			}
			else if (stage == 3) {
				// where 문 체크하고 없으면 통과
				if (current.equals("where")) {
					// where 실행할것
				}
				stage++;
			}
			else if (stage == 4) {
				// order by 체크하기
				if (current.equals("order")) {
					if (texts[++i].equals("by")) {
						// order by 실행
					}
					else {
						// order by 구문이 틀렸기 때문에
						grammer_error = true;
					}
				}
			}

			
			
			
		}
		return i++;
	}
}