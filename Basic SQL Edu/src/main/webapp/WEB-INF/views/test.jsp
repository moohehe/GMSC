<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html >
<html>
<head>
<meta name="viewport" content="height=device-height, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Basic SQL Edu</title>
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/Navibar.css"/>"/>
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/mainPage.css"/>"/>
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/view_menu.css"/>"/>
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/type_menu.css"/>"/>
<link rel="stylesheet" type="text/css" href="resources/css/contactus.css" />

<!-- web font -->
<link href="https://fonts.googleapis.com/css?family=Hammersmith+One" rel="stylesheet">
<link href="https://fonts.googleapis.com/css?family=Nunito+Sans" rel="stylesheet">
<link href="https://fonts.googleapis.com/css?family=Nanum+Gothic+Coding" rel="stylesheet">
<link href="https://fonts.googleapis.com/earlyaccess/sawarabimincho.css" rel="stylesheet" />


	<!-- BootStrap -->
	<link href="<c:url value='/resources/css/bootstrap.css' />" rel="stylesheet">
	<link href="<c:url value='/resources/css/mdb.css' />" rel="stylesheet">
	<link href="<c:url value='/resources/css/style.css' />" rel="stylesheet"> 
	<link href="<c:url value='/resources/css/animate.css' />" rel="stylesheet">



<script type="text/javascript" src="<c:url value="/resources/js/jquery-3.2.1.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/navigationbarjs.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/type_menu.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/view_menu.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/jquery-linenumbers.min.js"/>"></script>


    <!-- SCRIPTS -->
    <!-- Bootstrap tooltips -->
    <script type="text/javascript" src="<c:url value='/resources/js/popper.min.js'/> "></script>
    <!-- Bootstrap core JavaScript -->
    <script type="text/javascript" src="<c:url value='/resources/js/bootstrap.min.js' /> "></script>
    <!-- MDB core JavaScript -->
    <script type="text/javascript" src="<c:url value='/resources/js/mdb.js' />" ></script>
    <!-- Initializations -->
    <script type="text/javascript">
        // Animations initialization
        new WOW().init();
    </script> 
    
    
<style type="text/css">
	.hide-on-bush {
		display:none;
	}

	html, body { 
	    height: 100%; 
	    min-height: 100%;
	    width: 100%;
	    min-width:100%;
	    position: relative;+
	    
	    padding:0px;
	    marin:0px;
	    background:url('resources/image/bg.png');
    }
    .contactus-bg {
    	position:absolute;
    	height:100%;
    	width:100%;
    	margin:0;
    	z-index: 2000;
    	background-color:gray;
    	opacity: .9;
    	
    }
    .contactus {
    	position:absolute; height:100%; width:100%;
    	display:table-cell;text-align:center;vertical-align:middle;
    	z-index: 3000; padding-top: 10%;
    }
    .contactus-content {
    	height:500px;
    	width:500px;
    	margin:0;
    	display:inline-block;
    }
    .block {
    	width:100%;
    }
	@media ( min-height: 600px ) {
	  .block { height:0px }
	}
	@media ( min-height: 700px ) {
	  .block { height:60px }
	}
/* 	@media ( min-height: 800px ) {
		.block { height:100px }
	} */
	
</style>
</head>
<body>
	<input id="currentLv" type="hidden" value="${questext.lvstatus }"></input>
	<input id="currentLang" type="hidden" value="${questext.textLang }" ></input>
<div class="contactus-bg hide-on-bush"></div>
<div class="contactus hide-on-bush">
	<div class="contactus-content">
		<%@ include file="board/writeForm.jsp" %>
	</div>
</div>
<div class="left_menu">
	<!-- 그림이 표시되는 부분 -->
	<div class="view_menu">
		<div class="block test-title" >    ◈BASIC SQL EDU</div>
		<%@ include file="view_menu.jsp" %>
	</div>
	
	<!-- 타이핑 파트 -->
	<div class="type_menu">
		<div class="block"></div>
		<%@ include file="type_menu.jsp" %>
	</div>
</div>

	<!-- Navigation Bar -->
	<%@ include file="navigation.jsp" %>

</body>
</html>