package com.pcwk.ehr.cmn;

public class UI {
	public static void displayMainMenu() {
		System.out.println("┌─────────────────────────────────────────┐");
		System.out.println("│  1.로그인   2.회원가입  3.사이트로 이동  4.종료   │ ");
		System.out.println("└─────────────────────────────────────────┘");
	}
	public static void displaySiteMenu() {
		System.out.println("┌─────────────────────────────────────────────────────────────────────────┐");
		System.out.println("│ 1.상영중인 영화 조회  2.영화 검색  3.영화 예매  4.개인 정보 확인  5.관리자 모드  6.로그아웃 │");
		System.out.println("└─────────────────────────────────────────────────────────────────────────┘");
	}
	public static void displayPersonalMenu() {
		System.out.println("┌───────────────────────────────────────────────────────────┐");
		System.out.println("│ 1.개인 정보 수정  2.예매한 영화 확인  3.나의 지갑  4.회원 탈퇴  5.나가기  │");
		System.out.println("└───────────────────────────────────────────────────────────┘");
	}
	public static void displayChangeMenu() {
		System.out.println("┌────────────────┐");
		System.out.println("│ 1.이름  2.비밀번호 │");
		System.out.println("└────────────────┘");
	}
	public static void displayChargeMenu() {
		System.out.println("┌────────────────┐");
		System.out.println("│ 1.충전  2.나가기  │");
		System.out.println("└────────────────┘");
	}
	public static void displayManagerMenu() {
		System.out.println("┌───────────────────────────────────────────────────┐");
		System.out.println("│ 1.영화 목록 등록  2.영화 목록 삭제  3.유저 계정 관리  4.나가기  │");
		System.out.println("└───────────────────────────────────────────────────┘");
	}
}
