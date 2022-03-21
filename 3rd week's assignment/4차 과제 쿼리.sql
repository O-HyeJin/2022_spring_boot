/* 연간 로그인 수
	year = 20 */
SELECT DISTINCT count(*) as totCnt, '20' as date, ri.requestCode
	from statistic.requestinfo ri
	WHERE (left(ri.createDate, 2) = '20') AND (ri.requestCode = "L");
	
-- ----------------------------------------
		
/* 월별 접속자 수(중복X)
	groupByMonth */
SELECT COUNT(*) AS totCnt, LEFT(ri.createDate, 4) AS date
	FROM statistic.requestinfo ri
	GROUP BY date;

/* 일별 접속자 수(중복X)
	groupByDay */
SELECT COUNT(*) AS totCnt, LEFT(ri.createDate, 6) AS date
	FROM statistic.requestinfo ri
	GROUP BY date;

/* 부서별(월별) 접속자 수(중복X)
	groupByOrgan */
SELECT COUNT(*) AS totCnt, LEFT(ri.createDate, 4) AS date, u.organization AS organ
	FROM statistic.requestinfo ri, statistic.user u
	WHERE ri.userID = u.userID
	GROUP BY u.organization, date; 
	
/* 평균 하루 로그인 수 
	groupByMonth, average */
SELECT AVG(reqCount.loginCnt) as totCnt, LEFT(sub_date, 4) AS date
	from 
		(SELECT COUNT(*) AS loginCnt, LEFT(ri.createDate, 6) AS sub_date
		FROM statistic.requestinfo ri
		WHERE ri.requestCode = "L" GROUP BY sub_date) reqCount
	GROUP BY date;

/* 휴일을 제외한 로그인 수(여기서는 주말 포함)
	따라서 3차 과제에서는 총 로그인 수를 구한다.
	groupByDay, weekday */
SELECT COUNT(*) AS totCnt, CONCAT("20", LEFT(ri.createDate, 6))  AS date 
	FROM statistic.requestinfo ri
	WHERE requestCode = "L" GROUP BY date;

-- 월별 로그인 수 
SELECT COUNT(*) AS totCnt, CONCAT("20", LEFT(ri.createDate, 4))  AS date 
	FROM statistic.requestinfo ri
	WHERE requestCode = "L" GROUP BY date;



/* 일자별 로그인 수(평일 하루 로그인 수 서브쿼리) */
SELECT COUNT(*) AS loginCnt, CONCAT("20", LEFT(ri.createDate, 6))  AS date 
	FROM statistic.requestinfo ri
	WHERE requestCode = "L" GROUP BY date;
