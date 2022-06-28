package com.pyc.app.board;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pyc.app.common.DAO;
import com.pyc.app.member.Member;

public class BoardDAO extends DAO{

	private static BoardDAO dao = null;
	
	private BoardDAO() {}
	
	public static BoardDAO getInstatnce() {
		if(dao == null) {
			dao = new BoardDAO();
		}
				
		return dao;
	}
	
	
	public void insertBoard(Board board) {
		try {
			connect();
			String query = "INSERT INTO pyc_board "
						 + "VALUES(pyc_board_SEQ.nextval, ?, ?, ?, ?, DEFAULT)";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, board.getMemberId());
			pstmt.setString(2, board.getBoardTitle());
			pstmt.setString(3, board.getBoardContents());
			pstmt.setInt(4, board.getRecommendBookId());
			
			int result = pstmt.executeUpdate();
			
			if(result > 0) {
				System.out.println("정상적으로 게시글(" + board.getBoardTitle() + ")를 작성하였습니다");
			}else {
				System.out.println("게시글 작성에 실패하였습니다!");
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			
		}finally {
			disconnect();
		}
		
	}	
	
	public List<Board> searchAll(){	
		List<Board> list = new ArrayList<>();		
		
		try {
			connect();		
			
			String query = "SELECT *"
						 + "FROM pyc_board "
						 + "WHERE board_abled = 0 "
						 + "ORDER BY rownum";
			
			pstmt = conn.prepareStatement(query);			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Board b = new Board();
				
				b.setBoardId(rs.getInt("board_id"));
				b.setBoardTitle(rs.getString("board_title"));
				b.setBoardContents(rs.getString("board_contents"));
				b.setRecommendBookId(rs.getInt("recommended_book_id"));
				b.setMemberId(rs.getString("member_id"));
				
				list.add(b);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			
		}finally {
			disconnect();
		}
		
		return list;
	}
	
	public List<Board> searchBoardByMember(Member member){
		
		List<Board> list = new ArrayList<>();
		try {
			connect();
			
			String query= "SELECT *"
						+ "FROM pyc_board "
						+ "WHERE board_abled = 0 "
						+ "AND member_id = ? "
						+ "ORDER BY rownum";
		
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, member.getMemberId());
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Board b = new Board();
				
				b.setBoardId(rs.getInt("board_id"));
				b.setBoardTitle(rs.getString("board_title"));
				b.setBoardContents(rs.getString("board_contents"));
				b.setRecommendBookId(rs.getInt("recommended_book_id"));
				b.setMemberId(rs.getString("member_id"));
				
				list.add(b);
			}
			
			
		}catch(SQLException e) {
			e.printStackTrace();
			
		}finally {
			disconnect();
		}		
		
		return list;		
	}
	
	public void deleteBoard(Board board) {	
		try {
			connect();
			
			String query = "UPDATE pyc_board "
						 + "SET board_abled = 1 "
						 + "WHERE board_id = " + board.getBoardId();
			
			stmt = conn.createStatement();
			
			int result = stmt.executeUpdate(query);
			
			if(result > 0) {
				System.out.println("정상적으로 게시글을 삭제하였습니다");
			}else {
				System.out.println("게시글 삭제를 실패했습니다.");
			}
			
		}catch(SQLException e) {
			e.printStackTrace();			
			
		}finally {
			disconnect();
		}		
	}
	
	public List<Board> searchBoardByTitle(String title){
		
		List<Board> list = new ArrayList<>();
		
		try {
			connect();
			
			title = title.toLowerCase();			
			String query = "SELECT *"
						 + "FROM pyc_board "
						 + "WHERE LOWER(board_title) LIKE '%" + title + "%' "
						 + "AND board_abled = 0 "
						 + "ORDER BY rownum ";
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				Board board = new Board();
				
				board.setBoardTitle(rs.getString("board_title"));
				board.setBoardId(rs.getInt("board_id"));
				board.setMemberId(rs.getString("member_id"));
				board.setBoardContents(rs.getString("board_contents"));
				board.setRecommendBookId(rs.getInt("RECOMMENDED_BOOK_ID"));
				
				list.add(board);
			}
		
		}catch(SQLException e) {
			e.printStackTrace();
			
		}finally {
			disconnect();
		}		
		
		return list;		
	}
	
	public List<Board> searchBoardByContents(String contents){
		List<Board> list = new ArrayList<>();
		
		try {
			connect();
			
			contents = contents.toLowerCase();			
			String query = "SELECT *"
						 + "FROM pyc_board "
						 + "WHERE LOWER(board_contents) LIKE '%" + contents + "%' "
						 + "AND board_abled = 0 "
						 + "ORDER BY rownum ";
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				Board board = new Board();
				
				board.setBoardTitle(rs.getString("board_title"));
				board.setBoardId(rs.getInt("board_id"));
				board.setMemberId(rs.getString("member_id"));
				board.setBoardContents(rs.getString("board_contents"));
				board.setRecommendBookId(rs.getInt("RECOMMENDED_BOOK_ID"));
				
				list.add(board);
			}
		
		}catch(SQLException e) {
			e.printStackTrace();
			
		}finally {
			disconnect();
		}		
		
		return list;	
		
	}
	
	
	
	
}
