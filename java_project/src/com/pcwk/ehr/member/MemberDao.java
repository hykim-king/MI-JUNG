package com.pcwk.ehr.member;

import java.io.*;
import java.util.*;

import com.pcwk.ehr.cmn.DTO;
import com.pcwk.ehr.cmn.WorkDiv;

public class MemberDao implements WorkDiv<MemberVO> {

	// TODO : 파일 경로로 변경
	private final String fileName = "member.csv";
	public static List<MemberVO> members = new ArrayList<MemberVO>();

	public MemberDao() {
		super();
		int count = readFile(fileName);
	}

	public void displayList(List<MemberVO> list) {
		if (list.size() > 0) {
			String message = "\r\n" + "  __  __           _               _    _    _   \r\n"
					+ " |  \\/  |___ _ __ | |__  ___ _ _  | |  (_)__| |_ \r\n"
					+ " | |\\/| / -_) '  \\| '_ \\/ -_) '_| | |__| (_-<  _|\r\n"
					+ " |_|  |_\\___|_|_|_|_.__/\\___|_|   |____|_/__/\\__|\r\n"
					+ "                                                 \r\n" + " ";
			System.out.println(message);
			for (MemberVO vo : list) {
				System.out.println(vo); //회원 정보 출력
			}
		} else {
			System.out.println("회원정보가 없습니다.");
		}
	}

	/**
	 * 등록
	 * 
	 * @param vo
	 * @return 1(성공)/0(실패)/2(memberId 중복)
	 */

	// boolean isExistsMember
	private boolean isExistsMember(MemberVO member) {
		boolean flag = false;

		for (MemberVO v : members) {
			if (v.getId().equals(member.getId())) { //회원 ID 중복 체크
				flag = true;
				return flag;
			}
		}

		return flag;
	}

	@Override
	public int doSave(MemberVO vo) {
		removeDuplicates(); //중복 회원 삭제
		// param 입력된 데이터를 members 추가.
		// 입력전에 memberId check 필요
		int flag = 0;

		if (isExistsMember(vo) == true) {
			flag = 2; //Id 중복인 경우 2
			return flag;
		}

		boolean check = this.members.add(vo);
		flag = check == true ? 1 : 0;

		return flag;
	}

	/**
	 * 수정
	 * 
	 * @param vo
	 * @return 1(성공)/0(실패)
	 */
	@Override
	public int doUpdate(MemberVO vo) {
	    int flag = 0;
	    for (int i = 0; i < members.size(); i++) {
	        if (members.get(i).getId().equals(vo.getId())) {
	            members.set(i, vo);
	            flag = 1;
	            writeFile(fileName); // 파일에 즉시 반영
	            break;
	        }
	    }
	    return flag;
	}



	/**
	 * 삭제
	 * 
	 * @param vo
	 * @return 1(성공)/0(실패)
	 */
	@Override
	public int doDelete(MemberVO vo) {
		// 회원목록에서 동일한 회원을 찾고 삭제
		int flag = 0;
		for (MemberVO member : members) {
			if (member.getId().equals(vo.getId())) {
//				flag=members.remove(vo)==true?1:0;
				members.remove(vo);

				flag = 1;
				break;
			}
		}
		return flag;
	}

	/**
	 * 회원단건 조회
	 * 
	 * @param vo
	 * @return MemberVO
	 */
	@Override
	public MemberVO doSelectOne(MemberVO vo) {
		// members에 회원 ID에 해당되는 회원 정보 전체를 return
		MemberVO outVO = null;
		for (MemberVO member : members) {
			if (member.getId().equals(vo.getId())) {
				outVO = member;
				break;
			}
		}
		return outVO;
	}

	/**
	 * 회원단건 조회
	 * 
	 * @param vo
	 * @return MemberVO
	 */
	@Override
	public List<MemberVO> doRetrieve(DTO dto) {
	    List<MemberVO> retrievedMembers = new ArrayList<>();
	    retrievedMembers.addAll(members);
	    return retrievedMembers;
	}

	@Override
	public int writeFile(String path) {
	    int flag = 0;
	    
	    try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
	        for (MemberVO member : members) {
	            String memberData = member.toFileFormat();
	            bw.write(memberData);
	            bw.newLine();//줄바꿈
	        }
	        flag = 1; 
	    } catch (IOException e) {
	        System.out.println("IOException while writing file: " + e.getMessage());
	    }
	    return flag; 
	}

	public MemberVO StringtoMember(String data) {
	    MemberVO out = null;

	    // Null 또는 빈 문자열 체크
	    if (data == null || data.isEmpty()) {
	        System.out.println("입력된 데이터가 null이거나 비어 있습니다.");
	        return out;
	    }

	    String[] memberArr = data.split(",");

	    // 배열의 길이가 충분한지 검사
	    if (memberArr.length < 6) {
	        System.out.println("잘못된 데이터 형식입니다. 필요한 정보가 부족합니다.");
	        return out;
	    }

	    try {
	        String id = memberArr[0];//회원 Id	
	        String pass = memberArr[1];//비밀번호	
	        String name = memberArr[2];//이름	
	        boolean manager = Boolean.parseBoolean(memberArr[3]);//관리자 여부
	        int wallet = Integer.parseInt(memberArr[4]);//소지금
	        int age = Integer.parseInt(memberArr[5]);//나이
	        out = new MemberVO(id, pass, name, manager, wallet, age);
	    } catch (NumberFormatException e) {
	        System.out.println("숫자 형식 변환 중 오류가 발생했습니다: " + e.getMessage());
	    }

	    return out;
	}

	@Override
	public int readFile(String path) {

		try (BufferedReader br = new BufferedReader(new FileReader(path))) {

			String data = "";
			while ((data = br.readLine()) != null) {
				 data = data.trim(); // 데이터의 공백 제거
		            // 빈 줄이면 넘어가기
		            if (data.isEmpty()) {
		                continue; // 빈 줄을 무시
		            }
				MemberVO outVO = StringtoMember(data);
				members.add(outVO);//회원 목록에 추가
			}

		} catch (IOException e) {
			System.out.println("IOException : " + e.getMessage());
		}

		// 회원정보 전체 조회
//		displayList(members);s
		return members.size();
	}
	
	public void removeDuplicates() {//중복 회원 삭제
	    Set<String> uniqueMemberIds = new HashSet<>();
	    List<MemberVO> uniqueMembers = new ArrayList<>();//중복 삭제된 회원 목록

	    for (MemberVO member : members) {
	        if (uniqueMemberIds.add(member.getId())) {
	            uniqueMembers.add(member);//중복이 아니면 회원목록에 추가
	        }
	    }

	    members = uniqueMembers;
	}
}