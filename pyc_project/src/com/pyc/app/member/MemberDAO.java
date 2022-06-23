package com.pyc.app.member;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pyc.app.common.DAO;

public class MemberDAO extends DAO{

	private static MemberDAO dao = null;
	
	private MemberDAO() {}
	
	public static MemberDAO getInstance() {
		if(dao == null) {
			dao = new MemberDAO();
		}
		
		return dao;
	}
	
	
	// 회원등록 메소드(Admin Only)
	public void registMember(Member mem) {			
		try {
			connect();
			String query = "INSERT INTO pyc_member "
						 + "VALUES(?, ?, ?, DEFAULT)";
			
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, mem.getMemberId());
			pstmt.setString(2, mem.getMemberPwd());
			pstmt.setString(3, mem.getMemberName());			
			
			int result = pstmt.executeUpdate();
			
			if(result > 0) {
				System.out.println(mem.getMemberId() + "님의 회원가입을 축하합니다!");
				
			}else {
				System.out.println("회원가입에 실패하였습니다.!");
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			
		}finally {
			disconnect();
		}		
	}
	
	// 회원 비밀번호 변경 메소드 ?? 필요한가??
	public void changePassword() {
		
	}
	
	
	// 아이디로 특정 회원(1명) 정보 검색
	// 리턴값이 null이면 등록된 회원정보가 없는것!
	public Member searchOneMember(String memberId) {	
		
		Member mem = null;		
		try {
			connect();
			
			String query = "SELECT * "
						 + "FROM pyc_member "
						 + "WHERE member_id = ?";
			
			pstmt = conn.prepareStatement(query);			
			pstmt.setString(1, memberId);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				mem = new Member();
				
				mem.setMemberId(rs.getString("member_id"));
				mem.setMemberName(rs.getString("member_name"));
				mem.setMemberPwd(rs.getString("member_pwd"));
				mem.setMemberRole(rs.getInt("member_role"));				
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			
		}finally {
			disconnect();
		}
		
		return mem;
	}
	
	// 전체회원 조회!
	public List<Member> searchAllMember(){		
		
		List<Member> list = new ArrayList<>();
		try {
			connect();		
			String query = "SELECT * "
						 + "FROM pyc_member "
						 + "ORDER BY member_role DESC";
			
			stmt = conn.createStatement();			
			rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				Member mem = new Member();
				
				mem.setMemberId(rs.getString("member_id"));
				mem.setMemberName(rs.getString("member_name"));
				mem.setMemberPwd(rs.getString("member_pwd"));
				mem.setMemberRole(rs.getInt("member_role"));			
				
				list.add(mem);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			
		}finally {
			disconnect();
		}		
		
		return list;		
	}
	
	//대여 기간이 지난 사람 조회
	//Book_rent Table에 대여일과 반납해야되는 날짜
	//Book_getBack Table를 빼서 남은 사람 중 현재 날짜보다 반납해야되는 날짜가 작은 사람을 프린트한다.
	public List<Member> searchOverdueMember(){
		
		List<Member> list = new ArrayList<>();
		
		try {
			connect();
		
			String query = "";
			
			pstmt = conn.prepareStatement(query);
			
		}catch(SQLException e) {
			e.printStackTrace();
			
		}finally {
			disconnect();
		}				
		
		return list;
	}
	
}


/*
  		
  		try {
			connect();
		
			String query ="";
			
			
		}catch(SQLException e) {
			e.printStackTrace();
			
		}finally {
			disconnect();
		}		
		
 */

