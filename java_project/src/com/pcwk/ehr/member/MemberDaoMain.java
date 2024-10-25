package com.pcwk.ehr.member;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.pcwk.ehr.cmn.UI;
import com.pcwk.ehr.movie.MovieDaoMain;

public class MemberDaoMain {
//	private List<MemberVO> members;
	private MemberVO loginedMember;
	private MemberDao memberDao; 
	private MovieDaoMain movieDaoMain;
	
	public MemberDaoMain() {
		this.memberDao = new MemberDao();
//		members = new ArrayList<>();
		loginedMember = null;
		this.movieDaoMain = new MovieDaoMain();
	}
	public void MemberRun(){
		Scanner sc = new Scanner(System.in);
		boolean managerId = false;  // 관리자 표시
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 개봉일 형식 변환을 위한 선언
		int myWallet = 0; //사용자 지갑 잔고
		
		memberDao.readFile("member.csv");
		while(true) {
			memberDao.removeDuplicates();
			UI.displayMainMenu();
			System.out.print("메뉴 선택 > ");
			String menu = sc.nextLine().trim();
			
			if (menu.equals("4")) { //종료 옵션
				System.out.println("프로그램을 종료합니다.");
				break;
			} else if (menu.equals("2")) { //회원가입 옵션
				String loginId = null; // 로그인된 상태 여부 확인
				if (isLogined()) { //사용자가 로그인 중인지 확인
					System.out.println("로그아웃 후 이용해주세요.");
					continue;
				}
				System.out.printf("이름 : ");
				String name = sc.nextLine();
				System.out.print("연령 : ");
				int userAge = sc.nextInt();

				sc.nextLine(); // nextLine() 사용 후 nextInt() 사용시 그냥 넘어가는 상황 발생
				while (true) {
					System.out.print("로그인 아이디 : ");
					loginId = sc.nextLine().trim();

					if (isLoginIdDup(loginId) == false) { // 중복 여부 확인
						System.out.println("이미 사용중인 아이디입니다.");
						continue;
					}

					System.out.printf("%s은(는) 사용가능한 아이디입니다.%n", loginId);
					break;
				}
				String loginPass = null;
				while (true) { //비밀번호 일치할 때까지 반복
					System.out.printf("로그인 비밀번호 : ");
					loginPass = sc.nextLine().trim();
					System.out.printf("로그인 비밀번호 확인 : ");
					String passCheck = sc.nextLine().trim();

					if (loginPass.equals(passCheck) == false) {
						System.out.println("비밀번호가 다릅니다.");
						continue;
					}
					break;
				}
				MemberVO member = new MemberVO(loginId, loginPass, name, managerId, myWallet, userAge);

				memberDao.doSave(member); // MemberDao의 members 리스트에 직접 추가
                memberDao.writeFile("member.csv"); // 파일에 저장

				System.out.printf("%s 회원님 환영합니다.%n", name);
				continue;
			} else if (menu.equals("1")) {
				if (isLogined()) {
					System.out.println("이미 로그인 되어 있습니다.");
					continue;
				}

				System.out.printf("아이디 : ");
				String loginId = sc.nextLine().trim();
				System.out.printf("비밀번호 : ");
				String loginPass = sc.nextLine().trim();
				MemberVO member = memberDao.doSelectOne(new MemberVO(loginId)); // 로그인 정보 확인

				if (member == null) {
					System.out.printf("%s은(는) 존재하지 않는 아이디입니다.\n", loginId);
					continue;
				}
				if (loginPass.equals(member.getPass()) == false) {
					System.out.println("비밀번호를 확인해주세요.");
					continue;
				}
				this.loginedMember = member;
				System.out.printf("%s님 환영합니다.%n", member.getName());

			}else if(menu.equals("3")) {
				 while (true) {
				        UI.displaySiteMenu();
				        System.out.print("메뉴 선택 > ");
				        String internalMenu = sc.nextLine().trim();

				        if (internalMenu.equals("6")) {
				            if (isLogined()) {
				                System.out.println("로그아웃 되었습니다.");
				                this.loginedMember = null;
				                break;
				            }
				        } else if (internalMenu.equals("1")) {
				            movieDaoMain.displayMovies();
				        } else if (internalMenu.equals("2")) {
				        	movieDaoMain.searchMovie();
				        } else if (internalMenu.equals("3")) {
				        	movieDaoMain.bookMovie(loginedMember);
				        } else if (internalMenu.equals("4")) {
				        	UI.displayPersonalMenu(); 
				        	movieDaoMain.verifyInfo(loginedMember);
				        } else if (internalMenu.equals("5")) {
				        	if(loginedMember.isManager() == false) {
				        		System.out.println("관리자 계정이 아닙니다.");
				        		continue;
				        	}
				        	movieDaoMain.managerMod();
				        }
				 }
			}
			
			
			
		}
	}
	public static void main(String[] args) {
		MemberDaoMain app = new MemberDaoMain();
        app.MemberRun(); // 프로그램 실행
	}
	
	private boolean isLogined() { // 로그인된 상태인지 확인하는 메소드
		return this.loginedMember != null;
	}

	private boolean isLoginIdDup(String loginId) { // 아이디 중복 생성 방지 메소드
		 return memberDao.doSelectOne(new MemberVO(loginId)) == null;
	}
	
}