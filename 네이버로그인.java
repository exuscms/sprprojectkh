
//* 호스팅이나 서버에 올려서 서비스중이여야만 네이버 로그인이 가능함

	//네이버 로그인 처리(사용자가 발급된 네이버 로그인 링크를 클릭하면 특정 주소로 리다이레)
	@RequestMapping(value = "/users/naverlogin", method = { RequestMethod.GET})
	public String NaverLogin(HttpSession session, Model model)//@PathVariable String CODE, @PathVariable String STATE
	{
		String STATE = UUID.randomUUID().toString(); //Unique 랜덤 토큰을 생성한다.
		
		String CLIENT_ID = "netQK7LPG3xotV1ZObkd"; // 네이버 로그인 클라이언트 ID를 지정한다.
		String CLIENT_SECRET = "sahZPh_P3m"; // 네이버 로그인 시크릿 코드를 지정한다.
		String REDIRECT_URL = "http://localhost:8080/login/users/returnVars";
		
		String NAVER_OAUTH_TOKEN_URL = "https://nid.naver.com/oauth2.0/authorize?response_type=code"; // oAuth 토큰 주소
		String REQUEST_URL = NAVER_OAUTH_TOKEN_URL + "&client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URL + "&state=" + STATE;

		model.addAttribute("requestUrl", REQUEST_URL);
		
		// 유효성 검증을 위해서 난수를 세션에 담아둔다.
		session.setAttribute("OAUTH_NAVER_TOKEN", STATE);
		
		return "login/naverLogin"; //<a href="${requestUrl}">네이버 로그인</a>을 달아둔다.
	}

	//네이버 로그인 콜백(회원이 로그인을 완료했다면 이곳으로 리다이렉트됨, 회원 정보를 담고있음)
	@RequestMapping(value = "/users/naverlogin", method = { RequestMethod.GET})
	public void NaverCallback(HttpServletRequest request, HttpSession session, Model model)
	{
		String RECEIVED_TOKEN = request.getParameter("state");
		String RECEIVED_CODE = request.getParameter("code");
		String CLIENT_ID = "netQK7LPG3xotV1ZObkd"; // 네이버 로그인 클라이언트 ID를 지정한다.
		String CLIENT_SECRET = "sahZPh_P3m"; // 네이버 로그인 시크릿 코드를 지정한다.
		String REDIRECT_URL = "http://localhost:8080/login/users/returnVars";
		
		String OAUTH_TOKEN = (String)session.getAttribute("OAUTH_NAVER_TOKEN");

		
		// 유효성 검사가 실패하면 로그인 페이지로 돌린다.
		if (!RECEIVED_TOKEN.equals(OAUTH_TOKEN)) {
			return "redirect:/login/naverLogin";
		}
		
		//RESPONSED_DATA_URL의 JSON을 가져와서 access_token를 헤더에 담음
		String RESPONSED_DATA_URL = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&redirect_uri=" + (REDIRECT_URL) + "&code=" + RECEIVED_CODE + "&state=" + RECEIVED_TOKEN; 

		
		
		List<String> lst = new ArrayList<String>();
		lst.add('Content-Type: application/json');
		lst.add('Authorization: Bearer ' /* + JSON의 access_token 값 */);
		
		String AUTH_URL = "https://openapi.naver.com/v1/nid/me";
		//AUTH_URL에 헤더와 같이 전송
		
		//반환된 값의 response key에 id, nickname, gender, age, birthday, profile_image가 담겨져 있음
		

	}
	
