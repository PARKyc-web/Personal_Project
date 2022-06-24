package com.pyc.app.book;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

	public void insertBook(Book book) {
		int result = -1;
		try {
			connect();
			boolean check = checkSameBook(book);
			// 동인한 책이 있는지 확인 하는 메소드
			// true가 나오면 동일한 책이 있는것

			if (!check) { // 똑같은 책이 없다면!
				String SeqQuery = "SELECT pyc_book_SEQ.nextval " 
								+ "FROM dual";

				stmt = conn.createStatement();
				rs = stmt.executeQuery(SeqQuery);

				rs.next();
				int idAndLoc = rs.getInt(1);

				String insertQuery = "INSERT INTO pyc_book " 
								   + "VALUES(?, ?, ?, ?, ?, ?)";

				pstmt = conn.prepareStatement(insertQuery);

				pstmt.setInt(1, idAndLoc);
				pstmt.setString(2, book.getBookName());
				pstmt.setString(3, book.getBookAuthor());
				pstmt.setInt(4, book.getBookAmount());
				pstmt.setInt(5, book.getBookPrice());
				pstmt.setInt(6, idAndLoc);

				result = pstmt.executeUpdate();

				if (result > 0) {
					System.out.println("정상적으로 도서 :" + book.getBookName() + "를 등록하였습니다.");

				} else {
					System.out.println("도서등록에 실패하였습니다!");
				}

			} else { // 똑같은 책이 있다면!
				String updateQuery = "UPDATE pyc_book " 
								   + "SET book_amount = ? " 
								   + "WHERE book_id = ?";
				pstmt = conn.prepareStatement(updateQuery);

				pstmt.setInt(1, rs.getInt("book_amount") + book.getBookAmount());
				pstmt.setInt(2, rs.getInt("book_id"));

				result = pstmt.executeUpdate();
				if (result > 0) {
					System.out.println("정상적으로 도서 :" + book.getBookName() + "를 " + book.getBookAmount() + "권 추가하였습니다.");
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

			if (!rs.next()) { // rs.next 가 불린값을 반환함 > false가 나온다? 책이 없다
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return true; // true가 나온다 같은 책이 있다.
	}

	public List<Book> searchBook(String bookName) {
		List<Book> list = new ArrayList<>();
		try {
			connect();			
			String query = "SELECT * " 
						 + "FROM pyc_book " 
						 + "WHERE book_name LIKE '%" + bookName + "%' "
						 + "ORDER BY book_id";

			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				Book book = new Book();

				book.setBookId(rs.getInt("book_id"));
				book.setBookName(rs.getString("book_name"));
				book.setBookAuthor(rs.getString("book_author"));
				book.setBookAmount(rs.getInt("book_amount"));
				book.setBookPrice(rs.getInt("book_price"));
				book.setBookLocation(rs.getInt("book_location"));

				list.add(book);
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			disconnect();
		}

		return list;
	}
	
	public List<Book> searchAllBook(){
		List<Book> list = new ArrayList<>();
		try {
			connect();
			String query = "SELECT * " 
						 + "FROM pyc_book";

			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
		
			while (rs.next()) {
				Book book = new Book();

				book.setBookId(rs.getInt("book_id"));
				book.setBookName(rs.getString("book_name"));
				book.setBookAuthor(rs.getString("book_author"));
				book.setBookAmount(rs.getInt("book_amount"));
				book.setBookPrice(rs.getInt("book_price"));
				book.setBookLocation(rs.getInt("book_location"));

				list.add(book);
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			disconnect();
		}

		return list;		
	}

}
