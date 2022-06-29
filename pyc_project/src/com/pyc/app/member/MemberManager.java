package com.pyc.app.member;

import java.util.List;

import com.pyc.app.book.Book;
import com.pyc.app.common.Manager;
import com.pyc.app.deal.DealDAO;
import com.pyc.app.deal.RentedBook;

public class MemberManager extends Manager{
	
	private MemberDAO mDAO = MemberDAO.getInstance();
	
	int curPage;
	int pageNumber;
	
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
		
		if(list.size() == 0) {
			System.out.println("등록된 회원이 없습니다");
			return;
		}
		
		showMember(list);
	}

	private void searchOne() {		
		System.out.print("검색을 진행할 ID > ");		
		Member m = mDAO.searchOneMember(sc.nextLine());
		
		if(m == null) {
			System.out.println("도서관 회원이 아닙니다.!");
			return;
		}	
		System.out.println("+++++++++++ 검색결과 +++++++++++");
		System.out.println(m);
		System.out.println("== 대여중인 책 ");
		List<RentedBook> list = DealDAO.getInstance().rentedBookList(m);
		for(RentedBook b: list) {
			System.out.println(b);
		}
		
		System.out.println("== 대여중인 책 ");
		list = DealDAO.getInstance().overdueBook(m);
		for(RentedBook b: list) {
			System.out.println(b);
		}
		System.out.println("++++++++++++++++++++++++++++++++");
	}
	
	private void memberShipUpgrade() {
		System.out.print("등급을 올릴 회원 ID > ");
		Member m = mDAO.searchOneMember(sc.nextLine());
		
		if(m == null){
			System.out.println("존재하지 않는 회원입니다");
			return;
		}
		
		if(m.getMemberRole() == 1) {
			System.out.println("이미 관리자 권한을 가진 회원입니다!");
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
		showMember(list);
	}
	
	private void showMember(List<Member> list) {
		
		initPage(); // curPage =1, pageNumber = 10;

		int totalPage = list.size() / pageNumber;
		if (list.size() % pageNumber != 0) {
			totalPage += 1;
		}
		while(true) {			
			int count =((curPage-1)*pageNumber)+1;
			
			System.out.println("현재 페이지 " + curPage + " of " + totalPage);
			System.out.println("+++++++++++++++ 검색결과 +++++++++++++++");
			for (int i = count; i < (count + pageNumber); i++) {
				if (i > list.size()) {
					break;
				}					
				System.out.println(i + ". -" + list.get(i - 1));
			}		
			System.out.println("++++++++++++++++++++++++++++++++++++++++");
			
			searchMenu();
			
			int menu = 0;
			menu = selectMenu();
			if (menu == 0) { // exit
				break;
			}

			if (menu == 1) {
				// Next
				if (totalPage > curPage) {
					curPage++;
				} else {
					System.out.println("마지막 페이지 입니다!");
				}

			} else if (menu == 2) {
				// Previous
				if (curPage == 1) {
					System.out.println("첫번째 페이지 입니다.");
				} else {
					curPage--;
				}
				
			} else {
				inputError();
			}		
		}
	}
	
	private void initPage() {
		curPage =1;
		pageNumber =10;
	}
	
	private void searchMenu() {
		System.out.println();
		System.out.println("++++++++++++++++++++++++++++++++++++++");
		System.out.println("| 1.Next  ||  2.Previous  ||  0.Exit |");
		System.out.println("++++++++++++++++++++++++++++++++++++++");
	}
	
}
