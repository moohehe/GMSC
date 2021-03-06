package com.basicsqledu.www;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.CookieGenerator;

import com.basicsqledu.www.dao.QuizDAO;
import com.basicsqledu.www.vo.SQLCompiler;
import com.google.gson.Gson;

// 데이터 전송과 관련된 내용 특히 ajax관련 컨트롤러
@Controller
public class CompilerController
{
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	@Autowired
	private QuizDAO quizDAO;
	@Autowired
	private SQLCompiler compiler;
	
	CookieGenerator cg = new CookieGenerator(); //쿠키 생성기. (완료한 문제 쿠키에 넣기 위함)
	
	@ResponseBody
	@RequestMapping(value="sqlCompiler", method = RequestMethod.POST
			, produces = "application/text; charset=utf8")
	public String compiler(String sql, HttpServletResponse response
			, HttpServletRequest request, HttpSession session
			, @RequestParam(defaultValue="animal_view") String table_name
			, @RequestParam(defaultValue="2") int questionNumber) {
		
		sql = sql.toLowerCase();
		if (sql.equals("abracatabra")) {
			System.out.println("아브라카타브라!");
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("password", "pass");
			map.put("url","list");
			Gson gson = new Gson();
			String json = gson.toJson(map);
			System.out.println("[ResultData]\n"+json);
			return json;
		}
		
		//20번 문제가 정답 == commit
		if(questionNumber == 20 && (sql.equals("commit;") || sql.equals("rollback;"))){
			HashMap<String, Object> map = new HashMap<String, Object>();
			System.out.println("20번 쿠키 및 커밋 검사");
			//쿠키 검사
			int k = 0;
			Cookie [] cok = request.getCookies();
			System.out.println("쿠키 문제번호 뭐니 : " + questionNumber);
			
			int [] cook = new int[20];
			for(Cookie c : cok){
				if(c.getValue().equals("pass")){
					int temp = Integer.valueOf(c.getName().replace("completeStage",""));
					cook[temp-1] = 1;
				}
			}
			for(int i : cook){
				if(i == 1){
					k++;
				}
			}
			System.out.println("k="+k);
			if(k >= 19){
				//인증서 가자 gg
				System.out.println("20 stage 전부 클리어함");
				map.put("end", true);
				if(sql.equals("commit;")){
					map.put("url", "goCertify");
				}else{
					map.put("url", "/www");
					session.setAttribute("certiBtn", true);
					session.setAttribute("url", "goCertify");
				}
			}else{
				map.put("end", false);
				map.put("errorMessage", "Please complete all questions");
			}
			
			Gson gson = new Gson();
			String json = gson.toJson(map);
			System.out.println("[ResultData]\n"+json);
			
			return json;
		}
		
		//테이블 이름 변경하기
		if(table_name.toLowerCase().contains("animal")){
			table_name = "animal_view";
		}else if(table_name.toLowerCase().contains("person")){
			table_name = "person_view";
		}else if(table_name.toLowerCase().contains("robot")){
			table_name = "robot_view";
		}
		
		// setup UTF-8
		response.setContentType("text/html;charset=UTF-8");
		// 0. 빈값이면 생략
		if (sql.equals("")) {
			return "";
		}
		
		// cheat key setting
		if (sql.split(".key.")[0].equals("pass")) {
			String[] cheats = {
					""
					,"create table animal(Animal_num number primary key,species varchar(40) unique,color varchar(40) not null,habitat  varchar(40) foreign key,legs number not null);" // stage1
					,"select * from animal;" // stage2
					,"select species from animal;" // stage3
					,"select habitat from animal;" // stage4
					,"select * from animal where color = red;" // stage5
					,"select * from animal where legs=2 and color = blue;" // stage6
					,"select * from animal where legs=4 or habitat= sky;" // stage7
					,"select * from animal where legs = 2 or legs = 4; " // stage8
					,"select * from animal order by legs desc;" // stage9
					,"select * from animal where color=blue and habitat=sky or habitat=land and color=red;" // stage10
					,"Alter table animal rename person;" // stage11
					,"Select h.job job from (select * from person where gender = 'female') h where h.hair_color= 'black';" // stage12
					,"Select * from (select * from person h2 where job=nurse) h1 Where h1.haircolor='pink' and h1.gender='female';" // stage13
					,"update person set hair_color = 'red' where hair_color=black and job=nurse;" // stage14
					,"insert into person(gender,hair_color,job,height) values('male', 'white', 'scientist', 177);" // stage15
					,"create table robot(r_color varchar(50),r_size varchar(50),r_type varchar(50),weapon varchar(50));" // stage16
					,"insert into robot (r_color, r_size, r_type, weapon) values('white','small','R2','beam');" // stage17
					,"delete from robot where r_color = grey and r_type = humanoid;" // stage18
					,"drop table robot;" // stage19
					,"commit;" // stage20
			};
			sql = cheats[questionNumber];
		}
		
		
		
		// 1. sql 구문 입력 / 해석 
		// 입력받은 sql 구문을 compiler 객체에 삽입
		compiler.setText(sql);
		compiler.setQuestionNumber(questionNumber, table_name);
		
		// DB 테이블 입력
		HashMap<String, Object> map = quizDAO.getTable(questionNumber); // <- 여기 나중에 변수로 바꿔야됨!!!!!!!!!!!!!!!!!!!!!!!!!!
		ArrayList<Object> list = (ArrayList<Object>) map.get("table_value");
		compiler.setTable(list);
		
		
		
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
		
		
		// cookie에서 현재 문제 번호를 받아온 뒤에 그걸 이용해서 프린트
		//Alter문제 처리
		int alterStep = 0;
		if(questionNumber == 11){
			String [][] alterArr = null;
			try{
				alterArr = (String[][])(resultMap.get("result"));
				System.out.println("알터 좀 나와라 씨벙" + alterArr[0][0]);
				
				//배열 검사
				for(int i = 0;i<alterArr[0].length;i++){
					if(alterArr[1][i].equals("true")){
						resultMap.put(alterArr[0][i], alterArr[1][i]);
						alterStep++;
					}
					
				}
			}catch(Exception e){
				resultMap.put("alterComplete", false);
			}
			
			if(alterStep == 5){
				resultMap.put("alterComplete", true);
			}
			
		}
		
		// cookie에서 현재 문제 번호를 받아온 뒤에 그걸 이용해서 프린트
		System.out.println("쿠키 성공 : " + (int)resultMap.get("success"));
		//alter 문은 5개 다 완성 됫을때만 실행해야댐
		if(questionNumber != 11){
			if(((int)resultMap.get("success")) == 1){		
				System.out.println("쿠키에 현재 완료한 스테이지만 저장"+questionNumber);
				cg.setCookieName("completeStage"+questionNumber);
				cg.addCookie(response, "pass"); 
				cg.setCookieMaxAge(72*60*60); //유효시간 3일 설정.
			}
		}else{
			//11일때
			if(alterStep == 5 && ((int)resultMap.get("success")) == 1){
				cg.setCookieName("completeStage"+questionNumber);
				cg.addCookie(response, "pass"); 
				cg.setCookieMaxAge(72*60*60); //유효시간 3일 설정.
			}
		}
		
		
		//resultMap에 무엇이 들었나?(확인용)
		  Set<String> keySet = map.keySet();
		  Iterator<String> iterator = keySet.iterator();
		  while (iterator.hasNext()) {
		   String key = iterator.next();
		   Object value = map.get(key);
		   System.out.printf("key : %s , value : %s %n", key, value);
		  }

		
		
		Gson gson = new Gson();
		String json = gson.toJson(resultMap);
		System.out.println("[ResultData]\n"+json);
		
		return json;
	}
	
	
}
