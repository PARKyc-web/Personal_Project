package com.pyc.app.login;

import java.util.Scanner;

import com.pyc.app.book.BookManager;
import com.pyc.app.common.Manager;
import com.pyc.app.member.Member;
import com.pyc.app.member.MemberDAO;
import com.pyc.app.member.MemberManager;

public class LoginManager extends Manager{

	private MemberDAO dao = MemberDAO.getInstance();
	private Member memberInfo = null;

	public LoginManager() {

		welcome();
		login();
		
		int menu = -1;
		while (true) {
			printMenu(memberInfo);
			menu = selectMenu();

			if (menu == 1) {
				System.out.println("도서대출 서비스 선택");
				new BookManager(memberInfo);
				
			} else if (menu == 2) {
				System.out.println("게시판 사용 서비스 선택");
				

			} else if (menu == 3 && memberInfo.getMemberRole() == 1) {
				System.out.println("회원관리 서비스 선택!");
				new MemberManager(memberInfo);

			} else if (menu == 0) {
				System.out.println("서비스 종료 선택");
				break;

			} else {
				System.out.println("메뉴를 확인 후 다시 입력해주세요!");
			}
		}
	}

	private void printMenu(Member member) {
		if (member.getMemberRole() == 0) {
			// 일반사용자의 경우
			System.out.println();
			System.out.println("========================================");
			System.out.println("|  1.도서대출  |  2.게시판  |  0.종료  |");
			System.out.println("========================================");

		} else {
			// 관리자로 로그인할 경우
			System.out.println();
			System.out.println("=======================================================");
			System.out.println("|  1.도서대출  |  2.게시판  |  3.회원관리  |  0.종료  |");
			System.out.println("=======================================================");
		}
	}

	private void welcome() {
		System.out.println("============= WELCOME TO Library =============");
		System.out.println(" 도서 대출을 사용하기 위해서 로그인을 해주십쇼 ");
		System.out.println("==============================================");
	}

	private void login() {
		while (true) {
			System.out.print("ID > ");
			String id = sc.nextLine();

			System.out.print("PWASSWORD > ");
			String pwd = sc.nextLine();

			Member mem = dao.searchOneMember(id);

			// ID 가 없는 경우
			if (mem == null) {
				System.out.println("ID를 다시 확인해주세요!");
				continue;

			} else {
				// ID는 있지만 비밀번호가 틀린겨우
				if (!mem.getMemberPwd().equals(pwd)) {
					System.out.println("PASSWORD를 다시 확인해주세요!");
					continue;
				}
				System.out.println("로그인 성공!");
				System.out.println(mem + "으로 현재 접속중입니다.");
				memberInfo = mem;
				break;
			}
		}
	}
}
