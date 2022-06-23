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
	
	@Override
	public String toString() {		
		return "책 이름 : " + bookName + " / 책 저자 : " + bookAuthor
				+ "책 수량 : " + bookAmount +" / 책 가격 : " + bookPrice;
	}
}
