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
				System.out.println("현재 못만듬.. 책에 관련된 정보 + 대여, 반납에 대한 정보가 있어야 됨.");
				// searchOverdueMember();

			} else if (menu == 0) {
				// 뒤로가기
				exit();
				break;

			} else {
				inputError();
			}
		}
	}

	private void printMenu() {
		System.out.println();
		System.out.println("===========================================================================================");
		System.out.println("|  1.회원등록  |  2.전체 회원조회  |  3.특정 회원조회  |  4.연체 회원조회  |  0.뒤로가기  |");
		System.out.println("===========================================================================================");
	}
	
	private void registMember() {		
		Member member = new Member();
		
		System.out.print("ID > ");
		member.setMemberId(sc.nextLine());

		System.out.print("PWASSWORD > ");
		member.setMemberPwd(sc.nextLine());

		System.out.print("NAME > ");
		member.setMemberName(sc.nextLine());
		
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
		
		System.out.println(m);		
	}
	
}
