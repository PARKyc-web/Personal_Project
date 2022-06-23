package com.pyc.app.book;

import java.sql.SQLException;

import com.pyc.app.common.DAO;

public class BookDAO extends DAO {

	private static BookDAO dao = null;

	private BookDAO() {
	}

	public static BookDAO getInstance() {
		if (dao == null) {
			dao = new BookDAO();
		}

		return dao;
	}

	public boolean checkSameBook(Book book) {
		try {
			String query = "SELECT * " 
						 + "FROM pyc_book " 
						 + "WHERE book_name = ? " 
						 + "AND book_author = ?";
			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, book.getBookName());
			pstmt.setString(2, book.getBookAuthor());
			rs = pstmt.executeQuery();

			if(rs == null) { // null이라는 거는 똑같은 책이 없다는 거야
				return false;
			} 
			
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			disconnect();
		}
		
		return true;
	}

	public void insertBook(Book book) {

		int result = -1;
		try {
			connect();
			boolean check = checkSameBook(book);

			if (!check) { // 똑같은 책이 없다면!
				String insertQuery = "INSERT INTO pyc_book " 
								   + "VALUES(pyc_book_SEQ.nextval, ?, ?, ?, ?)";

				pstmt = conn.prepareStatement(insertQuery);

				pstmt.setString(1, book.getBookName());
				pstmt.setString(2, book.getBookAuthor());
				pstmt.setInt(3, book.getBookAmount());
				pstmt.setInt(4, book.getBookPrice());

				result = pstmt.executeUpdate();

				if (result > 0) {
					System.out.println("정상적으로 도서 :" + book.getBookName() + "를 등록하였습니다.");

				} else {
					System.out.println("도서등록에 실패하였습니다!");
				}
				
			} else { // 똑같은 책이 있다면!
				rs.next();
				String updateQuery = "UPDATE pyc_book " 
								   + "SET book_amount = ? " 
								   + "WHERE book_id = ?";
				pstmt = conn.prepareStatement(updateQuery);

				pstmt.setInt(1, rs.getInt("book_amount") + book.getBookAmount());
				pstmt.setInt(2, book.getBookId());

				result = pstmt.executeUpdate();

				if (result > 0) {
					System.out.println("정상적으로 도서 :" + book.getBookName() + "를 " 
									   					   + book.getBookAmount() + "만큼 추가하였습니다.");
				} else {
					System.out.println("도서추가에 실패하였습니다!");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			disconnect();
		}

	}
}
