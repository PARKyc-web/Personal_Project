package com.pyc.app.member;

import java.util.List;

import com.pyc.app.common.Manager;

public class MemberManager extends Manager{
	
	private MemberDAO mDAO = MemberDAO.getInstance();
	
	public MemberManager(Member memberInfo) {
		whoAreYou(memberInfo);
		
		int menu = -1;
		while (true) {

			printMenu();
			menu = selectMenu();

			if (menu == 1) {
				// 회원등록
				registMember();
				
			} else if (menu == 2) {
				// 전체 회원조회
				searchAll();

			} else if (menu == 3) {
				// 특정 회원조회
				searchOne();

			} else if (menu == 4) {
				// 연체 회원조회
//				System.out.println("현재 못만듬.. 책에 관련된 정보 + 대여, 반납에 대한 정보가 있어야 됨.");
				searchOverdueMember();
				//지금 만들어 둔건 특정 회원을 조회해서 연체된 도서가 있는지 확인하는 것
				//여기서 내가 원하는건 전체 회원 중에 연체내역이 있는 회원의 id를 찍는것
				//그렇다면
				//렌트 테이블에서 SYSDATE > e_date 인 사람의 id를 가져옴
				//그리고 표시함 
				
			} else if (menu == 5) {
				memberShipUpgrade();				
				
			} else if (menu == 0) {
				// 뒤로가기
				back();
				break;

			} else {
				inputError();
			}
		}
	}

	private void printMenu() {
		System.out.println();
		System.out.println("==================================================================================================");
		System.out.println("| 1.회원등록 | 2.전체 회원조회 | 3.특정 회원조회 | 4.연체 회원조회 | 5.회원등급조정 | 0.뒤로가기 |");
		System.out.println("==================================================================================================");
	}
	
	private void registMember() {		
		Member member = new Member();
		
		System.out.print("ID > ");
		member.setMemberId(sc.nextLine());

		System.out.print("PWASSWORD > ");
		member.setMemberPwd(sc.nextLine());
		
		mDAO.registMember(member);
	}
	
	private void searchAll() {
		List<Member> list = mDAO.searchAllMember();
		
		for(Member m : list) {
			System.out.println(m);
		}
	}

	private void searchOne() {		
		System.out.print("검색을 진행할 ID > ");		
		Member m = mDAO.searchOneMember(sc.nextLine());
		
		if(m == null) {
			System.out.println("도서관 회원이 아닙니다.!");
			return;
		}	
		
		System.out.println(m);		
	}
	
	private void memberShipUpgrade() {
		System.out.print("등급을 올릴 회원 ID > ");
		Member m = mDAO.searchOneMember(sc.nextLine());
		
		if(m == null){
			System.out.println("존재하지 않는 회원입니다");
			return;
		}
		System.out.println("==== 변경전");
		System.out.println(m);
		mDAO.memberShipUpgrade(m);
	}
	
	private void searchOverdueMember() {
		List<Member> list = mDAO.searchOverdueMember();
		
		if(list.size() == 0) {
			System.out.println("현재 연체중인 회원이 없습니다!");
			return;
		}
		
		System.out.println("+++++++++++ 현재시간 기중 연체중인 회원 +++++++++++");
		for(Member m : list ) {
			System.out.println(m);
		}
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++");
	}
	
}
