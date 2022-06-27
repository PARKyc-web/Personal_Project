package com.pyc.app.reply;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pyc.app.board.Board;
import com.pyc.app.common.DAO;

public class ReplyDAO extends DAO {
	
	private static ReplyDAO dao = null;
	
	private ReplyDAO() {}
	
	public static ReplyDAO getInstance() {		
		if(dao == null) {
			dao = new ReplyDAO();
		}
		
		return dao;
	}
	
	
	public List<Reply> getAllReply(Board board){		
		List<Reply> list = new ArrayList<>();
		
		try {
			connect();
			
			String query = "SELECT * "
						 + "FROM pyc_reply "
						 + "WHERE reply_abled = 0 AND board_id = ?";
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, board.getBoardId());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {				
				Reply reply = new Reply();
				
				reply.setBoard_id(rs.getInt("board_id"));
				reply.setReply_id(rs.getInt("reply_id"));
				reply.setMember_id(rs.getString("member_id"));
				reply.setReply_contents(rs.getString("reply_contents"));
								
				list.add(reply);
			}			
			
		}catch(SQLException e) {
			e.printStackTrace();
			
		}finally {
			disconnect();			
		}		
		
		return list;		
	}	
	
	public void insertReply(Reply reply) {		
		try {
			connect();
			
			String query ="INSERT INTO pyc_reply "
					 	+ "VALUES(pyc_reply_SEQ.nextval, ?, ?, ?, DEFAULT)";			
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, reply.getBoard_id());
			pstmt.setString(2, reply.getReply_contents());
			pstmt.setString(3, reply.getMember_id());
			
			int result = pstmt.executeUpdate();
			
			if(result > 0) {
				System.out.println("성공적으로 댓글을 등록하였습니다.");
			}else {
				System.out.println("댓글 등록에 실패하였습니다.");
			}			
		}catch(SQLException e) {
			e.printStackTrace();
			
		}finally {
			disconnect();
		}
	}
	
	
	public void deleteReply(Reply reply) {		
		try {
			connect();
			
			String query = "DELETE FROM pyc_reply "
					 	 + "WHERE reply_id = ? ";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, reply.getReply_id());
			
			int result = pstmt.executeUpdate();
			
			if(result > 0) {
				System.out.println("정상적으로 댓글을 삭제하였습니다");
				
			}else {
				System.out.println("댓글 삭제에 실패하였습니다.");
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			
		}finally {
			disconnect();
		}		
	}
	
}
