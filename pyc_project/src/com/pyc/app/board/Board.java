package com.pyc.app.board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Board {

	private int boardId;
	private String memberId;
	private String boardTitle;
	private String boardContents;
	private int recommendBookId;	
	
	@Override
	public String toString() {
		String result = "";
		
		result += "제목 : " + boardTitle;
		result += "\n작성자 : " + memberId;
		result += "\n추천도서 : " + recommendBookId;
		result += "\n내용 : " + boardContents;
		
		return result;
	}	
}


/*


보드 테이블에 필요한 기능

1. 게시글 작성 >> 일반유저, 관리자유저 둘 다 사용가능
2. 게시글 확인 >> 이후, 추천한 도서검색 기능
3. 게시글 삭제 >> 일반유저의 경우, 자신이 작성한 글만 삭제 가능
	           >> 관리자 유저는 모든 글을 삭제할 수 있다.
	           >> 외래키 설정때문에 삭제가 힘들다?? 
	           >> 칼럼에 하나 추가해서 abled해서 0, 1로 해서 조정? >> 0 활성화, 1 비활성화
	           
4. 자신이 작성한 글 확인(abled = 0 인것들만)
5. 댓글 달기
6. 추천한 도서검색 기능 >> 도서정보를 확인할 수 있어야한다. >> 추천한 도서가 없을 경우, 없다고 떠야함(없을 수 있나?)

7. 게시글 전체조회


Board 데이터 테이블
CREATE TABLE pyc_BOARD(

	board_id NUMBER(10) PRIMARY KEY,
	board_title VARCHAR(50) NOT NULL,
	board_contents VARCHAR(1000),	
	recommended_book_id NUMBER(10)
	CONSTRAINT board_recommend_fk REFERENCES pyc_book(book_id),
	
	member_id VARCHAR2(50))
	CONSTRAINT board_memId_fk REFERENCES pyc_member(member_id)
	
	board_abled NUMBER(1) DEFAULT 0,
	

)


Reply 데이터 테이블
CREATE TABLE pyc_reply(

	reply_id NUMBER(10) PRIMARY KEY,
	board_id NUMBER(10) 
	CONSTRAINT reply_bid_fk REFERENCES board(board_id),
	board_contents VARCHAR(1000)
	
	member_id VARCHAR2(50)
	CONSTRAINT reply_memId_fk REFERENCES pyc_member(member_id)
	
	reply_abled NUMBER(1) DEFAULT 0
)

댓글기능에 필요한 기능

1. 댓글 작성
2. 댓글 삭제  >> 보드와 같이 외래키 때문에 삭제하기가 힘들다. reply_abled = 0이 기본 살아있는거
3. 자신이 작성한 댓글 확인 (abled = 0 인것들만)
4. 댓글 수정


CREATE SEQUENCE pyc_board_SEQ
INCREMENT BY 1
START WITH 1
NOCACHE
NOCYCLE;



게시판 기능

** 게시글 작성

- INSERT INTO pyc_board
  VALUES(pyc_board_SEQ.nextval, board.title, board.contents, board.book, member.memberId, DEFAULT);
  

** 

	1. 게시판 조회 선택하면 >> 바로 상위 10개의 게시물을 보여줌
	2. Next하면 다음 10개의 화면을 보여줌
	
	3. 어떻게 ? 어디서 어디까지 표시할거임?
	
		-----------------------------------------------> Next or Previous
		|
	게시판 조회 ---------> 특정 게시물을 봄 
						(댓글도 같이 나와야함)
								|
								|
								--------> 1. 댓글 작성 및 삭제, 수정
										  2. 등록된 도서내용 검색 
										  3. 뒤로가기 (전체조회)

	boardManager {	
		int curPage;  >> 현재 몇 페이지인지?
		int pageNumber;  >> 한 페이지에 몇개를 표시 할 것인가? 
		
		next >> curPage++;
		previous >> curPage--;
		
		시작 (rownum * curPage)  ~ (rownum*curPage)+pageNumber;
		결과창 Clear() 이클립스는 지원하지 않는다.
	}


   	(전체검색)
   	SELECT *
   	FROM pyc_board
   	WHERE board_abled = 0
   	ORDER BY board_id;
  
	(제목검색)
	SELECT *
	FROM pyc_board
	WHERE board_abled = 0 AND board_title LIKE '%name%'
	ORDER BY board_id;
	
	(내용검색)
	SELECT *
	FROM pyc_board
	WHERE board_abled = 0 AND board_contents LIKE '%contents'
	ORDER BY board_id;

	(제목+내용 검색)
	제목검색 UNION 내용검색?
	
	(내가 작성한 글)
	SELECT *
	FROM pyc_board
	WHERE board_abled = 0 AND member_id = ??
	ORDER BY board_id;
	
	

	******************* REPLY
	
	(댓글 작성)
	댓글을 작성하기 위해서는 우선 게시글을 조회해야 한다.
		
	reply_insert(Board board, reply reply)
	
	INSERT INTO pyc_reply
	VALUES()
	

	(내가 작성한 댓글)
	SELECT *
	FROM pyc_reply
	WHERE reply_abled = 0 AND member_id = ??
	
	>> 어느 게시글에, 작성했는지 확인할 수 있어야한다.
	
	
	

*/