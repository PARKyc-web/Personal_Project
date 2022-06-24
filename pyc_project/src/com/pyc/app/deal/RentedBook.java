package com.pyc.app.deal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RentedBook {

	private int bookId;
	private String memberId;
	private String bookName;
	private String sDate; // 대출일
	private String eDate; // 반납 예정일
	private String bDate; // 반납일
	private int delayTime;

	@Override
	public String toString() {

		String result = "";

		if (bDate != null) { // 책을 반납했으면
			result = "책 이름 :" + bookName + " | 대출일 :" + sDate + 
					" | 반납일 :" + bDate + " | 기간연장 :" + delayTime + "회";
		} else {
			result = "책 이름 :" + bookName + " | 대출일 :" + sDate + 
					" | 반납예정일 :" + eDate + " | 기간연장 :" + delayTime + "회";
		}

		return result;
	}

}
