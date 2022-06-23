package com.pyc.app.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member {

	private String memberId;
	private String memberPwd;
	private String memberName;
	private int memberRole;

	@Override
	public String toString() {
		String str = "";
		
		if(memberRole == 0) {
			str += "일반 사용자 계정 :: ";
		}else {
			str += "관리자 계정 :: ";
		}
		
		str += "사용자 ID :" + memberId;		
		return str;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Member) {
			Member mem = (Member)obj;			
			
			return this.memberId.equals(mem.getMemberId()) && this.memberPwd.equals(mem.getMemberPwd()); 
		}
		
		return false;
	}
	
}
