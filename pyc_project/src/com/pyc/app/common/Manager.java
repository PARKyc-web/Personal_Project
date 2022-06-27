package com.pyc.app.common;

import java.util.Scanner;

import com.pyc.app.member.Member;

public class Manager {	
	
	protected Scanner sc = new Scanner(System.in);
	
	protected int selectMenu() {
		int menu = 0;
		try {
			System.out.print("선택 > ");
			menu = Integer.parseInt(sc.nextLine());

		} catch (NumberFormatException e) {
			System.out.println("메뉴 확인 후 숫자를 입력해주세요!");
			System.out.print("입력 > ");
			menu = selectMenu();
		}

		return menu;
	}
	
	protected int getNumber() {
		int number = 0;

		try {
			number = Integer.parseInt(sc.nextLine());

		} catch (NumberFormatException e) {
			System.out.println("숫자를 입력해주세요");
			System.out.print("입력 > ");
			number = getNumber();
		}				
	
		return number;
	}
	
	protected void back() {
		System.out.println("로그인 화면으로 돌아갑니다!");
	}
	
	protected void exit() {
		System.out.println("프로그램을 종료합니다!");
	}
	
	protected void inputError() {
		System.out.println("메뉴를 확인 후 다시 입력해주세요!");
	}
	
	protected void whoAreYou(Member memberInfo) {
		System.out.println("환영합니다! " + memberInfo.getMemberId() + "님 ");
	}
	
	
	
	
	
	
	
}
