package com.pcwk.ehr.cmn;

import java.util.List;

import com.pcwk.ehr.member.MemberVO;

public interface WorkDiv<T> {
	int writeFile(String path);
	/**
	 * 파일 읽기
	 * @param path
	 * @return
	 */
	
	int readFile(String path);
	
	/**
	 * 등록
	 * @param vo
	 * @return 1/0
	 */
	int doSave(T vo);

	/**
	 * 수정
	 * @param vo
	 * @return 1(성공)/0(실패)
	 */
	int doUpdate(T vo);

	/**
	 * 삭제
	 * @param vo
	 * @return 1(성공)/0(실패)
	 */
	int doDelete(T vo);

	/**
	 * 회원단건 조회
	 * @param vo
	 * @return MemberVO
	 */
	T doSelectOne(T vo);

	/**
	 * 회원단건 조회
	 * @param vo
	 * @return MemberVO
	 */
	List<T> doRetrieve(DTO vo);

}