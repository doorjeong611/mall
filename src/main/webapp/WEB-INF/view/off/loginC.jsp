<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>login</title>

 <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<style>
@import url(http://weloveiconfonts.com/api/?family=entypo);
@import url(https://fonts.googleapis.com/css?family=Roboto);

/* zocial */
[class*="entypo-"]:before {
  font-family: 'entypo', sans-serif;
}

*,
*:before,
*:after {
  -moz-box-sizing: border-box;
  -webkit-box-sizing: border-box;
  box-sizing: border-box; 
}


h2 {
  color:rgba(255,255,255,.8);
  margin-left:12px;
}

body {
  background: #8C8C8C;
  font-family: 'Roboto', sans-serif;
  
}

form {
  position:relative;
  margin: 50px auto;
  width: 380px;
  height: auto;
}

input {
  padding: 16px;
  border-radius:7px;
  border:0px;
  background: rgba(255,255,255,.2);
  display: block;
  margin: 15px;
  width: 300px;  
  color:white;
  font-size:18px;
  height: 54px;
}

input:focus {
  outline-color: rgba(0,0,0,0);
  background: rgba(255,255,255,.95);
  color: #e74c3c;
}

button {
  float:right;
  height: 121px;
  width: 50px;
  border: 0px;
  background: #e74c3c;
  border-radius:7px;
  padding: 10px;
  color:white;
  font-size:22px;
}

.inputUserIcon {
  position:absolute;
  top:68px;
  right: 80px;
  color:white;
}

.inputPassIcon {
  position:absolute;
  top:136px;
  right: 80px;
  color:white;
}

input::-webkit-input-placeholder {
  color: white;
}

input:focus::-webkit-input-placeholder {
  color: #e74c3c;
}


</style>
<script>
$(document).ready(function(){ // <body>까지 메모리에 올라간 후 script 실행.
   
   $(".user").focusin(function(){
     $(".inputUserIcon").css("color", "#e74c3c");
   }).focusout(function(){
     $(".inputUserIcon").css("color", "white");
   });

   $(".pass").focusin(function(){
     $(".inputPassIcon").css("color", "#e74c3c");
   }).focusout(function(){
     $(".inputPassIcon").css("color", "white");
   });
   
   /* 유효성 검사 */
   const emailTest = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-Za-z0-9\-]+/; 
   const isStaffTest = /^@[a-zA-Z0-9]+$/;
   
   $('#btnLogin').click(function() {
      
      if($('#id').val() == null || $('#id').val() == ''){
         alert('id를 입력하세요.');
         $('#id').focus();
         return;
         
      } else if(isStaffTest.test($('#id').val()) == false){// staffId가 아니라면 회원이메일 유효성 검사하기
         
         if(emailTest.test($('#id').val()) == false){
            alert('E-Mail을 입력해주세요.');
            $('#id').focus();
            return;
         }
         
      } 
      
      if($('#pw').val() == null || $('#pw').val() == ''){
         alert('password를 입력해주세요.');
         $('#pw').focus();
         return;
      }
      
      
      $('#loginForm').submit();
      
   })
   

   
})
</script>

</head>
<body>


<form action="${pageContext.request.contextPath}/off/loginC" method="post" id="loginForm">
  
  <h2><span class="entypo-login"><i class="fa fa-sign-in"></i></span> Login</h2>
 
  <!-- 로그인 버튼  -->
  <button type="button" class="button" id="btnLogin"><span class="entypo-lock"><i class="fa fa-lock"></i></span></button>
  
  <!-- ID 입력부분 -->
  <span class="entypo-user inputUserIcon">
     <i class="fa fa-user"></i>
  </span>
  <input type="text" class="user" placeholder="ID" name="id" id="id" value="customer1@naver.com" />
  
  <!-- PW 입력부분 -->
  <span class="entypo-key inputPassIcon">
     <i class="fa fa-key"></i>
  </span>
  <input type="password" class="pass"placeholder="PASSWORD" name="pw" id="pw" value="1234" />
</form>





</body>

</html>