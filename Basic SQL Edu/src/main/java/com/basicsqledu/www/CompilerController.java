package com.basicsqledu.www;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.basicsqledu.www.dao.QuizDAO;
import com.basicsqledu.www.vo.SQLCompiler;
import com.google.gson.Gson;

// 데이터 전송과 관련된 내용 특히 ajax관련 컨트롤러
@Controller
public class CompilerController
{
	@Autowired
	QuizDAO quizDAO;
	
	@Autowired
	private SQLCompiler compiler;
	
	
	@ResponseBody
	@RequestMapping(value="sqlCompiler", method = RequestMethod.POST
			, produces = "application/text; charset=utf8")
	public String compiler(String sql, HttpServletResponse response
			, @RequestParam(defaultValue="animal_view") String table_name) {
		// setup UTF-8
		response.setContentType("text/html;charset=UTF-8");
		// 0. 빈값이면 생략
		if (sql.equals("")) {
			return "";
		}
		
		// 1. sql 구문 입력 / 해석 
		// 입력받은 sql 구문을 compiler 객체에 삽입
		compiler.setText(sql);
		// DB 테이블 입력
		HashMap<String, Object> map = quizDAO.getTable(table_name); // <- 여기 나중에 변수로 바꿔야됨!!!!!!!!!!!!!!!!!!!!!!!!!!
		ArrayList<Object> list = (ArrayList<Object>) map.get("table_value");
		compiler.setTable(list);
		
		// cookie에서 현재 문제 번호를 받아온 뒤에 그걸 이용해서 프린트
		
		
		// sql 구문 해석
		HashMap<String, Object> resultMap = compiler.getResult();
		

		// 2. sql 구문 해석 후 데이터 처리 및 전송
		// 오류가 발생하면 error message 출력하고 종료
		/*if (!((boolean) resultMap.get("complete"))) {
			System.out.println("errorMessage : "+compiler.getErrorMessage());
			return compiler.getErrorMessage();
		}*/
		// 오류가 없으면
		// 데이터 테이블을 json으로 출력해서 보내준다. 그럼 그걸 받아서 js로 그림으로 출력함.
		Gson gson = new Gson();
		String json = gson.toJson(resultMap);
		System.out.println("[ResultData]\n"+json);
		
		return "success!!!\n"+json;
	}
	
	@ResponseBody
	@RequestMapping(value="sqlCompiler2", method = RequestMethod.POST
			, produces = "application/text; charset=utf8")
	public String compiler2(String sql, HttpServletResponse response
			, @RequestParam(defaultValue="animal") String table_name) {
		// setup UTF-8
		response.setContentType("text/html;charset=UTF-8");
		// 0. 빈값이면 생략
		if (sql.equals("")) {
			return "";
		}
		
		// 1. sql 구문 입력 / 해석 
		// 입력받은 sql 구문을 compiler 객체에 삽입
		compiler.setText(sql);
		// DB 테이블 입력
		HashMap<String, Object> map = quizDAO.getTable(table_name); // <- 여기 나중에 변수로 바꿔야됨!!!!!!!!!!!!!!!!!!!!!!!!!!
		ArrayList<Object> list = (ArrayList<Object>) map.get("table_value");
		compiler.setTable(list);
		

		HashMap<String, Object> resultMap = compiler.getResult();
		
		Gson gson = new Gson();
		String json = gson.toJson(resultMap);
		System.out.println("[ResultData]\n"+json);
		
		return "success!!!\n"+json;
	}
	
}
