package com.pyc.app.deal;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pyc.app.book.Book;
import com.pyc.app.common.DAO;
import com.pyc.app.member.Member;

public class DealDAO extends DAO{

	private static DealDAO dao = null;
	
	private DealDAO() {}
	
	public static DealDAO getInstance() {
		if(dao == null) {
			dao = new DealDAO();
		}		
		return dao;		
	}
	
	// 무조건 유효한 책과 멤버가 넘어온다!
	public void bookRent(Book book, Member member) {					
		try {
			connect();
			
			String query = "SELECT book_amount "
						 + "FROM pyc_book";
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			rs.next();
			
			int amount = rs.getInt(1);			
			if(amount < 0) {
				System.out.println("현재 도서수량이 부족합니다!");
				return;
			}
			
			query = "INSERT INTO pyc_book_rent(book_id, member_id) "
			   	  + "VALUES(?, ?)";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, book.getBookId());
			pstmt.setString(2, member.getMemberId());			
			
			int result = pstmt.executeUpdate();
			
			if(result > 0) {
				System.out.println("성공적으로 도서 : [" + book.getBookName() + "]를 대여 하였습니다");
				
			}else {
				System.out.println("도서 대여에 실패하였습니다!");
			}
			//책을 대여한 다음에 수량 조절
			downBookAmount(book);
			
		}catch(SQLException e) {
			e.printStackTrace();
			
		}finally {
			disconnect();
		}		
	}

	// 특정유저의 현재 반납이 안된 책 검색
	public List<RentedBook> rentedBookList(Member member) {
		List<RentedBook> list = new ArrayList<>();
		
		try {
			connect();
			// is_back = 0 대여는 했지만 반납은 안한 상태
			// is_back = 1 대여 후 반납까지 한 상태
			String query = "SELECT ta.*, b.book_name "
					     + "FROM pyc_book b JOIN(SELECT book_id, member_id, TO_CHAR(s_date) sdate, TO_CHAR(e_date) edate, "
					     					  + "TO_CHAR(b_date) bdate, delay_time "
					     					  + "FROM pyc_book_rent "
					     					  + "WHERE is_back = 0) ta "
					     + "ON (b.book_id = ta.book_id)"
					     + "WHERE member_id = ? ";
		
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, member.getMemberId());
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {				
				RentedBook rb = new RentedBook();
				
				rb.setBookId(rs.getInt("book_id"));
				rb.setMemberId(rs.getString("member_id"));
				rb.setBookName(rs.getString("book_name"));
				rb.setSDate(rs.getString("sdate"));
				rb.setEDate(rs.getString("edate"));
				rb.setBDate(rs.getString("bdate"));
				rb.setDelayTime(rs.getInt("delay_time"));

				list.add(rb);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			
		}finally {
			disconnect();
		}		
		
		return list;
	}
		
	
	// 무조건 유효한 책과 멤버가 넘어온다
	// 유효한 책이란 > 자기가 빌린 책 중 1개
	public void bookBack(RentedBook book, Member member) {				
		try {
			connect();			
			String query = "UPDATE pyc_book_rent "
						 + "SET is_back = 1, b_date = SYSDATE "
						 + "WHERE book_id = ? AND member_id = ?";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, book.getBookId());
			pstmt.setString(2, member.getMemberId());
			
			int result = pstmt.executeUpdate();
			
			if(result > 0) {
				System.out.println("성공적으로 도서 : [" + book.getBookName() + "]를 반납하였습니다.");
				
			}else {
				System.out.println("책 반납을 실패하였습니다.!");
			}
			
			increseBookAmount(book);
			
		}catch(SQLException e) {
			e.printStackTrace();
			
		}finally {
			disconnect();
		}		
	}
	
	public void increseBookAmount(RentedBook rb) throws SQLException {		
		String query = "SELECT book_amount "
					 + "FROM pyc_book "
					 + "WHERE book_id = " + rb.getBookId();
		
		stmt = conn.createStatement();
		rs = stmt.executeQuery(query);
		
		rs.next();
		int amount = rs.getInt("book_amount");		
		
		query = "UPDATE pyc_book "
			  + "SET book_amount = ? "
			  + "WHERE book_id = ?";
		
		pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, amount+1);
		pstmt.setInt(2, rb.getBookId());	
		pstmt.executeUpdate();
	}
	
	public void downBookAmount(Book book) throws SQLException {
		String query = "SELECT book_amount "
				 	 + "FROM pyc_book "
				 	 + "WHERE book_id = " + book.getBookId();
	
		stmt = conn.createStatement();
		rs = stmt.executeQuery(query);
	
		rs.next();
		int amount = rs.getInt("book_amount");		
	
		query = "UPDATE pyc_book "
				+ "SET book_amount = ? "
				+ "WHERE book_id = ?";
	
		pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, amount-1);
		pstmt.setInt(2, book.getBookId());	
		pstmt.executeUpdate();
	}
	
	public List<RentedBook> overdueBook(Member member){
		
		List<RentedBook> list = new ArrayList<>();
		try {
			connect();

			String query = "SELECT * "
						 + "FROM pyc_book_rent "
						 + "WHERE e_date < SYSDATE AND member_id = ?"
						 + "AND is_back = 0";
		
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, member.getMemberId());
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {				
				RentedBook rb = new RentedBook();
				
				rb.setBookId(rs.getInt("book_id"));
				rb.setMemberId(rs.getString("member_id"));
				rb.setBookName(rs.getString("book_name"));
				rb.setSDate(rs.getString("sdate"));
				rb.setEDate(rs.getString("edate"));
				rb.setBDate(rs.getString("bdate"));
				rb.setDelayTime(rs.getInt("delay_time"));

				list.add(rb);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			
		}finally {
			disconnect();
		}		
		
		return list;		
	}
	
}
