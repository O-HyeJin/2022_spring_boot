/* 연간 로그인 수
	year = 20 */
SELECT DISTINCT count(*) as totCnt, '20' as date, ri.requestCode
	from statistic.requestinfo ri
	WHERE (left(ri.createDate, 2) = '20') AND (ri.requestCode = "L");
	
-- ----------------------------------------
		
/* 월별 접속자 수(중복X)
	yearMonth = 2008 */
SELECT COUNT(*) AS totCnt, '2008' AS date
	FROM statistic.requestinfo ri
	WHERE (left(ri.createDate, 4) = '2008');

/* 일별 접속자 수(중복X)
	yearMonthDay = 200821 */
SELECT COUNT(*) AS totCnt, '200821' AS date
	FROM statistic.requestinfo ri
	WHERE (left(ri.createDate, 6) = '200821');

/* 부서별(월별) 접속자 수(중복X)
	yearMonth = 2008, organ = A */
SELECT COUNT(*) AS totCnt, '2008' AS date, u.organization
	FROM statistic.requestinfo ri, statistic.user u
	WHERE (left(ri.createDate, 4) = '2008') AND ri.userID = u.userID
	GROUP BY u.organization;
	
/* 평균 하루 로그인 수 
	average = true */
SELECT AVG(reqCount.loginCnt) as totCnt
	from 
		(SELECT COUNT(*) AS loginCnt, LEFT(ri.createDate, 6) AS date 
		FROM statistic.requestinfo ri
		WHERE ri.requestCode = "L" GROUP BY date) reqCount;

/* 휴일을 제외한 로그인 수(여기서는 주말 포함)
	따라서 3차 과제에서는 총 로그인 수를 구한다.
	weekday = true */
SELECT COUNT(*) AS totCnt
	FROM statistic.requestinfo ri
	WHERE ri.requestCode = "L";



/* 일자별 로그인 수(평일 하루 로그인 수 서브쿼리) */
SELECT COUNT(*) AS loginCnt, LEFT(ri.createDate, 6) AS date 
	FROM statistic.requestinfo ri
	WHERE ri.requestCode = "L" GROUP BY date;
