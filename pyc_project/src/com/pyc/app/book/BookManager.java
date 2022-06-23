package com.pyc.app.book;

import com.pyc.app.common.Manager;
import com.pyc.app.member.Member;

public class BookManager extends Manager{

	private BookDAO bDAO = BookDAO.getInstance();
		
	public BookManager(Member memberInfo) {
		whoAreYou(memberInfo);
		
		int menu = -1;		
		while(true) {
			
			printMenu(memberInfo);
			
			menu = selectMenu();			
			if(menu == 1) {
				//도서검색
				
				
			} else if(menu == 2) {
				//도서대출
				
				
			} else if(menu == 3) {
				//도서반납
				
				
			} else if(menu == 4 && memberInfo.getMemberRole() == 1) {
				//기간연장
				
				
			} else if(menu == 5 && memberInfo.getMemberRole() == 1) {
				//책등록
				
				
			} else if(menu == 6 && memberInfo.getMemberRole() == 1) {
				//책 폐기
				
				
			} else if(menu == 7 && memberInfo.getMemberRole() == 1) {
				//회원정보 확인
				
				
			} else if(menu == 0) {
				exit();
				break;
				
			} else {
				inputError();
			}			
		}	
	}
	
	
	private void printMenu(Member memberInfo) {		
		System.out.println();
		if(memberInfo.getMemberRole() == 0) {
			System.out.println("=============================================================");
			System.out.println("|  1.도서검색  |  2.도서대출  |  3.도서반납  |  0.뒤로가기  |");
			System.out.println("=============================================================");
			
		}else {			
			System.out.println("======================================================================================================================");
			System.out.println("|  1.도서검색  |  2.도서대출  |  3.도서반납  |  4.기간연장  |"
							  + "  5.책등록  |  6.책폐기  |  7.회원정보확인  |  0.뒤로가기  |");
			System.out.println("======================================================================================================================");
		}		
	}


	
	
}
