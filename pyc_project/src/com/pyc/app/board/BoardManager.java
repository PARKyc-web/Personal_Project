package com.pyc.app.board;

import java.util.List;

import com.pyc.app.book.Book;
import com.pyc.app.book.BookDAO;
import com.pyc.app.common.Manager;
import com.pyc.app.member.Member;
import com.pyc.app.reply.Reply;
import com.pyc.app.reply.ReplyDAO;

public class BoardManager extends Manager {

	private BoardDAO dao = BoardDAO.getInstatnce();
	private BookDAO bDAO = BookDAO.getInstance();
	private ReplyDAO rDAO = ReplyDAO.getInstance();

	int curPage = 1;
	int pageNumber = 10;

	public BoardManager(Member memberInfo) {
		initPage();
		whoAreYou(memberInfo);
		while (true) {
			printMenu();
			int menu = selectMenu();
			if (menu == 1) {
				// 게시판 조회
				searchAll(memberInfo);

			} else if (menu == 2) {
				// 게시글 작성
				insertBoard(memberInfo);

			} else if (menu == 3) {
				// 게시글 검색(제목)
				
			} else if (menu == 4) {
				// 게시글 검색(내용)

			} else if (menu == 5) {
				// 내가 작성한 게시글
				searchBoardByMember(memberInfo);

			} else if (menu == 0) {
				back();
				break;
				
			}else {
				inputError();
			}
		}
	}

	private void initPage() {
		curPage = 1;
		pageNumber = 10;
	}

	private void printMenu() {
		System.out.println();
		System.out.println("============================================================================================");
		System.out.println("| 1.전체검색, 2.게시글 작성, 3.게시글 검색(제목), 4.게시글 검색(내용), 5.작성한 글, 0.종료 |");
		System.out.println("============================================================================================");
	}
	
	// 게시판 전체 조회 어떤 게시물이 있나 제목만 확인
	private void searchAll(Member memberInfo) {		
		while(true) {		
			List<Board> list = dao.searchAll(curPage, pageNumber);			
			if(list.size() == 0) {
				System.out.println("현재 게시물이 존재하지 않습니다.!");
				return;
			}
			
			int totalPage = list.size()/pageNumber;
			if(list.size() % pageNumber != 0) {
				totalPage += 1;
			}
			
			int count = ((curPage-1)*pageNumber)+1;
			System.out.println("\n=====모든 게시물");
			System.out.println("현재 페이지 : " + curPage + "(Total Page : " + totalPage + ")");
			for(Board b : list) {
				System.out.println((count++) + ". - " + b.getBoardTitle());
			}			
			
			searchMenu();			
			int sMenu = selectMenu();			
			if(sMenu == 1) {
				// Next
				if(totalPage > curPage) {
					curPage++;
				}else {
					System.out.println("마지막 페이지 입니다!");
				}
				
			}else if(sMenu == 2){
				// Previous
				if(curPage <= 1) {
					System.out.println("첫번째 페이지 입니다.");
				}else {
					curPage--;
				}
				
			}else if(sMenu == 3) {
				//게시글 조회(내용)
				System.out.print("조회 할 게시글 : ");
				int boardNum = getNumber();
				if(boardNum >= count || boardNum == 0) {
					System.out.println("글 목록을 보고 다시 입력해주세요!");
					
				}else {
					List<Reply> rList = rDAO.getAllReply(list.get(boardNum-1));
					
					System.out.println("\n*********************************************************************************");
					System.out.println(list.get(boardNum-1));
					System.out.println("*********************************************************************************");
					
					System.out.println("\n== REPLY");	
					int rCount = 1;
					for(Reply r : rList) {
						System.out.println((rCount++)+ ". " +r);
					}
					System.out.print("\nReply(1), Delete-Reply(2), Delete-Board(3), Exit(0) :: ");
					
					int num = getNumber();			
					if(num == 1) {
						//reply RUN
						insertReply(list.get(boardNum-1), memberInfo);

					} else if(num == 2) {
						deleteReply(rList, memberInfo);
						
					} else if(num == 3) {
						deleteBoard(list.get(boardNum-1), memberInfo);
						
					} else if(num == 0) {
						// exit
						break;
						
					}else {
						inputError();
					}
				}
				
			}else if(sMenu == 0) {
				break;
				
			}else {
				inputError();
			}
		}
		
	}

	private void deleteReply(List<Reply> list, Member memberInfo) {		
		if(list.size() == 0) {
			return;
		}		
		
		System.out.print("삭제할 댓글 선택 > ");
		int number =0;
		while(true) {
			number = getNumber();
			
			if(number > list.size() || number <= 0) {
				System.out.println("댓글 목록을 확인 후 다시 선택해 주세요");
				System.out.print("선택 > ");
			}else {
				break;
			}
		}		
		
		if(list.get(number-1).getMember_id().equals(memberInfo.getMemberId()) || memberInfo.getMemberRole() == 1) {
			rDAO.deleteReply(list.get(number-1));
			
		} else {
			System.out.println("관리자 또는 작성자만 삭제할 수 있습니다.");
		}		
	}
	
	private void insertBoard(Member memberInfo) {
		Board board = new Board();

		System.out.println("============== 게시글 작성 ==============");
		System.out.print("추천도서 검색: ");
		String book = sc.nextLine();
		List<Book> list = null;
		
		if(book.equals("0")) {
			list = bDAO.searchAllBook();
		}else {
			list = bDAO.searchBook(book);
		}
		
		if(list.size() == 0) {
			System.out.println("도서가 존재하지 않습니다.");
			return;
		}
		
		System.out.println("============== 도서 목록 ==============");
		int count = 1;
		for (Book b : list) {
			System.out.println((count++) + ". -" + b);
		}
		System.out.println("책 선택 : ");

		int number = getNumber();
		while (number >= count) {
			number = getNumber();
			System.out.println("도서목록 안에서 선택해주세요");
		}

		board.setRecommendBookId(list.get(number - 1).getBookId());

		System.out.println("+++++++++++++++++++++++++++++++++++++++++");
		System.out.print("글 제목 : ");
		board.setBoardTitle(sc.nextLine());

		System.out.println("글 내용 : ");
		board.setBoardContents(sc.nextLine());
		board.setMemberId(memberInfo.getMemberId());
		
		dao.insertBoard(board);
	}

	private void searchMenu() {
		System.out.println();
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("| 1.Next  ||  2.Previous  ||  3.글 조회  ||  0.Exit |");
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
	}

	private void searchBoardByMember(Member memberInfo) {
		initPage();
		while (true) {
			List<Board> list = dao.searchBoardByMember(memberInfo, curPage, pageNumber);

			if (list.size() == 0) {
				System.out.println("작성한 게시글이 존재하지 않습니다.");
				return;
			}

			int totalPage = list.size() / pageNumber;
			if (list.size() % pageNumber != 0) {
				totalPage += 1;
			}

			int count = ((curPage - 1) * pageNumber) + 1;
			System.out.println("\n+++++++++ 내가 작성한 게시글+++++++++");
			System.out.println("현재페이지 : " + curPage + "(Total Page : " + totalPage + ")");
			for (Board b : list) {
				System.out.println((count++) + ". - " + b.getBoardTitle());
			}
			System.out.println("+++++++++++++++++++++++++++++++++++++");
			
			searchMenu();
			int sMenu = selectMenu();
			if (sMenu == 1) {
				// Next
				if (totalPage > curPage) {
					curPage++;
				} else {
					System.out.println("마지막 페이지 입니다!");
				}

			} else if (sMenu == 2) {
				// Previous
				if (curPage == 1) {
					System.out.println("첫번째 페이지 입니다.");
				} else {
					curPage--;
				}
				
			} else if (sMenu == 3) {
				// 게시글 조회(내용)
				System.out.print("조회 할 게시글 : ");
				int boardNum = getNumber();
				if (boardNum >= count || boardNum == 0) {
					System.out.println("글 목록을 보고 다시 입력해주세요!");
					
				} else {
					List<Reply> rList = rDAO.getAllReply(list.get(boardNum - 1));

					System.out.println("******************************");
					System.out.println(list.get(boardNum - 1));
					System.out.println("******************************");
					
					System.out.println("== REPLY");					
					int rCount = 1;
					for(Reply r : rList) {
						System.out.println((rCount++)+ ". " +r);
					}
					System.out.print("\nReply(1), Delete-Reply(2), Delete-Board(3), Exit(0) :: ");
					
					int num = getNumber();
					if (num == 1) {
						insertReply(list.get(boardNum - 1), memberInfo);
						// reply RUN

					} else if(num == 2) {
						deleteReply(rList, memberInfo);
						
					} else if(num == 3) {
						deleteBoard(list.get(boardNum-1), memberInfo);
						
					} else if (num == 0) {
						// exit
						break;
					}
				}
				
			} else if (sMenu == 0) {
				break;
			}
		}
	}

	private void insertReply(Board board, Member member) {
		Reply reply = new Reply();

		System.out.println("내용 : ");
		reply.setReply_contents(sc.nextLine());
		reply.setBoard_id(board.getBoardId());
		reply.setMember_id(member.getMemberId());

		rDAO.insertReply(reply);
	}

	private void deleteBoard(Board board, Member memberInfo) {
		
		if(board.getMemberId().equals(memberInfo.getMemberId()) || memberInfo.getMemberRole() == 1) {
			dao.deleteBoard(board);
		}else {
			System.out.println("관리자 또는 작성자만 삭제할 수 있습니다.");
		}
		
		
	}
	
	
	
}
