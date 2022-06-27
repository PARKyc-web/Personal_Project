package com.pyc.app.book;

import java.util.ArrayList;
import java.util.List;

import com.pyc.app.common.Manager;
import com.pyc.app.deal.DealDAO;
import com.pyc.app.deal.RentedBook;
import com.pyc.app.member.Member;
import com.pyc.app.member.MemberDAO;

public class BookManager extends Manager{

	private BookDAO bDAO = BookDAO.getInstance();
	private MemberDAO mDAO = MemberDAO.getInstance();
	private DealDAO dDAO = DealDAO.getInstance();
	
	public BookManager(Member memberInfo) {
		whoAreYou(memberInfo);
		
		int menu = -1;		
		while(true) {			
			printMenu(memberInfo);
			
			menu = selectMenu();			
			if(menu == 1) {
				//도서검색
				searchBook();
							
			} else if(menu == 2 && memberInfo.getMemberRole() == 1) {
				//도서대출
				bookRent();
				
			} else if(menu == 3 && memberInfo.getMemberRole() == 1) {
				//도서반납
				bookBack();			
				
			} else if(menu == 4 && memberInfo.getMemberRole() == 1) {
				//책등록
				bookIn();
				
			} else if(menu == 5 && memberInfo.getMemberRole() == 1) {
				//책 폐기
				scrapBook();
								
			} else if(menu == 6 && memberInfo.getMemberRole() == 1) {
				//사용자 정보확인(관리자용)
				searchMemberInfo();				
				
			} else if(menu == 2) {
				//기간연장
				extendPeriod(memberInfo);
				
			} else if(menu == 3) {
				searchMyInfo(memberInfo);				
				
			} else if(menu == 0) {
				back();
				break;
				
			} else {
				inputError();
			}			
		}	
	}
	
	private void printMenu(Member memberInfo) {		
		System.out.println();
		if(memberInfo.getMemberRole() == 0) {
			System.out.println("=================================================================");
			System.out.println("|  1.도서검색  |  2.1차 기간연장  |  3.정보확인  |  0.뒤로가기  |");
			System.out.println("=================================================================");
			
		}else {			
			System.out.println("========================================================================================================================");
			System.out.println("|  1.도서검색  |  2.도서대출  |  3.도서반납  |  4.책등록  |"
							  + "  5.책폐기  |  6.회원정보확인 |  0.뒤로가기  |");
			System.out.println("========================================================================================================================");
		}		
	}
	
	private void bookIn() {		
		Book book = inputBookInfo();		
		bDAO.insertBook(book);		
	}
	
	private Book inputBookInfo() {		
		Book book = new Book();
		
		System.out.println("++++++++++ 책 등록 ++++++++++");
		System.out.print("책 이름 > ");
		book.setBookName(sc.nextLine());
		
		System.out.print("책 저자 > ");
		book.setBookAuthor(sc.nextLine());
		
		System.out.print("책 수량 > ");
		book.setBookAmount(getNumber());
		
		System.out.print("책 가격 > ");
		book.setBookPrice(getNumber());
		
		return book;
	}
	
	private void searchBook() {			
		System.out.println("====================================");
		System.out.println("|  1.전체조회  |  2.특정 도서조회  |");
		System.out.println("====================================");
		
		int menu = selectMenu();
		List<Book>list = new ArrayList<>();
		
		if(menu == 1) {
			list = bDAO.searchAllBook();
			
			if(list.size() == 0) {
				System.out.println("현재 도서관에는 책이 없습니다");
				return;
			}
			
			int count =1;
			System.out.println("+++++++++++++++ 검색결과 +++++++++++++++");
			for(Book b : list) {
				System.out.println((count++) + ". - " + b);
			}			
			System.out.println("++++++++++++++++++++++++++++++++++++++++");
		}else {
			System.out.print("검색할 책 이름 > ");
			list = bDAO.searchBook(sc.nextLine());
			
			if(list.size() == 0) {
				System.out.println("검색한 이름의 도서가 존재하지 않습니다");
				return;
			}
			
			int count =1;
			System.out.println("+++++++++++++++ 검색결과 +++++++++++++++");
			for(Book b : list) {
				System.out.println((count++) + ". - " + b);
			}
			System.out.println("++++++++++++++++++++++++++++++++++++++++");
		}
	}
		
	private void bookRent() {		
		System.out.print("대여할 사람의 ID > ");
		Member member = mDAO.searchOneMember(sc.nextLine());
		// 회원인지 체크
		// 회원이면 책 빌리기 가능
		
		if(member == null) {
			System.out.println("도서관의 회원이 아닙니다!");
			return;
		} 
		
		// 책 검색  >> 현재 남은 권수 까지 표시
		System.out.print("대여할 책 검색 > ");
		List<Book> list = bDAO.searchBook(sc.nextLine());
		
		if(list.size() == 0) {
			System.out.println("검색한 도서가 존재하지 않습니다!");
			return;
		}
		
		int count =1;
		System.out.println("++++++++++ 검색된 도서 ++++++++++");
		for(Book b : list) {
			System.out.println((count++) + ". - " + b);
		}
		System.out.println("+++++++++++++++++++++++++++++++++");
		
		System.out.print("대여할 책을 번호를 입력! > ");
		int index = getNumber();
		
		if(index >= count) {
			index = getNumber();
			
		} else {
			dDAO.bookRent(list.get(index-1), member);
		}		
	}	
	
	private void bookBack() {
		System.out.print("반납할 사람의 ID > ");
		Member member = mDAO.searchOneMember(sc.nextLine());
		
		if(member == null) {
			System.out.println("도서관의 회원이 아닙니다!");
			return;
		}
		
		//멤버가 빌린 책 목록 프린트
		List<RentedBook> list = dDAO.rentedBookList(member);			
		
		if(list.size() == 0) {
			System.out.println("해당 회원은 대여한 도서가 없습니다!");
			return;
		}
		
		int count =1;
		System.out.println("++++++++++ 검색된 도서 ++++++++++");
		for(RentedBook rb : list) {
			System.out.println((count++) + ". - " + rb);
		}
		System.out.println("+++++++++++++++++++++++++++++++++");
		
		System.out.print("반납할 책을 번호를 입력! > ");
		int index = getNumber();
		
		if(index >= count) {
			index = getNumber();
			
		} else {
			dDAO.bookBack(list.get(index-1), member);
		}		
	}
	
	private void searchMemberInfo() {		
		System.out.print("검색할 회원 ID > ");
		String memberId = sc.nextLine();
		
		Member member = mDAO.searchOneMember(memberId);
		
		if(member == null) {
			System.out.println("도서관 회원이 아닙니다!");
			return;
		}
		
		List<RentedBook> list = dDAO.rentedBookList(member);		
		System.out.println("+++++++++++++++ 회원정보 +++++++++++++++");
		System.out.println(member);
		
		System.out.println("\n+++++++++++++++ 회원 도서대출정보 +++++++++++++++");
		System.out.println("== 현재 대여중인 도서");
		int count =1;
		for(RentedBook rb : list) {
			System.out.println((count++) + " - "+ rb);
		}
		
		list = dDAO.overdueBook(member);
		System.out.println("\n== 현재 대여기간이 끝난 도서");
		count =1;
		for(RentedBook rb : list) {
			System.out.println((count++) + " - "+ rb);
		}		
	}
	
	private void extendPeriod(Member member) {
		List<RentedBook> list = dDAO.rentedBookList(member);
		
		System.out.println("\n+++++++++++++++ 회원 도서대출정보 +++++++++++++++");
		System.out.println("== 현재 대여중인 도서");
		int count =1;
		for(RentedBook rb : list) {
			System.out.println((count++) + " - "+ rb);
		}
		
		System.out.print("연장할 도서선택 > ");
		while(true) {
			int number = getNumber();
			
			if(list.get(number-1).getDelayTime() > 0) {
				System.out.println("연장할 수 없는 도서입니다.!(최대 연장횟수 1회)");
				return;
			}
			
			if(number >= count || number == 0) {
				System.out.println("목록을 확인 후 다시 선택해 주세요.");
			}else {
				dDAO.extendPeriod(list.get(number-1));
				break;
			}
		}
		
		
	}

	private void searchMyInfo(Member member) {		
		List<RentedBook> list = dDAO.rentedBookList(member);		
		System.out.println("+++++++++++++++ 회원정보 +++++++++++++++");
		System.out.println(member);
		
		System.out.println("\n+++++++++++++++ 회원 도서대출정보 +++++++++++++++");
		System.out.println("== 현재 대여중인 도서");
		int count =1;
		for(RentedBook rb : list) {
			System.out.println((count++) + " - "+ rb);
		}
		
		list = dDAO.overdueBook(member);
		System.out.println("\n== 현재 대여기간이 끝난 도서");
		count =1;
		for(RentedBook rb : list) {
			System.out.println((count++) + " - "+ rb);
		}		
	}
	
	private void scrapBook() {
		System.out.print("폐기할 책 이름 > ");
		List<Book> list = bDAO.searchBook(sc.nextLine());
		int number =0;
		if(list.size() == 0) {
			System.out.println("검색한 이름의 도서가 존재하지 않습니다");
			return;
		}
		
		int count =1;
		System.out.println("+++++++++++++++ 검색결과 +++++++++++++++");
		for(Book b : list) {
			System.out.println((count++) + ". - " + b);
		}
		System.out.println("++++++++++++++++++++++++++++++++++++++++");
		
		while(true) {
			System.out.print("폐기할 책 번호 > ");
			number = getNumber();
			
			if(number >= count) {
				System.out.println("목록확인 후 다시 입력해주세요");
				continue;
			}
			break;
		}
		
		if(list.get(number-1).getBookAmount() <= 0) {
			System.out.println("책의 수량이 부족합니다");
			return;
		}
		
		dDAO.scrapBook(list.get(number-1));		
	}
}
