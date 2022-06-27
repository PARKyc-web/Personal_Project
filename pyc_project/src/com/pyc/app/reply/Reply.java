package com.pyc.app.reply;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Reply {
	
	private int reply_id;
	private int board_id;
	private String reply_contents;
	private String member_id;
	private int reply_abled;
	
	
	@Override
	public String toString() {
		String result = member_id + ": " + reply_contents;
		
		return result;		
	}
	
}
