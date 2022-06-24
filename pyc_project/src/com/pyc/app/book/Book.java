package com.pyc.app.book;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Book {
	
	private int bookId;
	private String bookName;
	private String bookAuthor;
	private int bookAmount;
	private int bookPrice;	
	private int bookLocation;
	
	
	@Override
	public String toString() {				
		return "책 이름 : " + bookName + " / 책 저자 : " + bookAuthor
				+ " / 책 수량 : " + bookAmount + " / 책 가격 : " + bookPrice
				+ " / 책 위치 : " + getLocation();
	}
		
	// 책 위치 변환하는 법
	//
	// bookLocation / 100 == ?? >> 나오는 값이 0이면 A, 1이면 B .... 증가한다
	// bookLocation % 10 == ?? >> 나오는 값이 0이면 1, 1이면 2 ..... 증가한다 (최대 10)
	// 즉 책의 위치는 (영어)(1 ~ 10)으로 계산하여 출력한다.
	// 책 위치의 번호는 책 ID와 동일하게 저장된다.	
	private String getLocation() {
		char front = (char)((bookLocation/100)+65); // 아스키 코드상 최소 A
		int back = (bookLocation%10)+1;
		
		return front + "-" + back;		
	}
	
}
