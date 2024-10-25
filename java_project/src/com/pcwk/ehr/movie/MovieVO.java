package com.pcwk.ehr.movie;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

import com.pcwk.ehr.cmn.DTO;
import com.pcwk.ehr.member.MemberDao;
import com.pcwk.ehr.member.MemberVO;


//영화 정보
public class MovieVO extends DTO {
	private String movieName; 	//영화 이름
	private LocalDate date; 	//개봉일
	private String supervision; //감독
	private int ageLimit;		//연령 제한
	private double rating;		//평점
	private char[][] seats;		//좌석
	private String reservedSeat;//예약된 좌석

	
	 // 생성자: 영화 정보와 기본 좌석 배열(5x5)을 설정
	public MovieVO(String movieName,LocalDate date, String supervision, int ageLimit, double rating ) {
		this.movieName = movieName;
		this.date = date;
		this.supervision = supervision;
		this.ageLimit = ageLimit;
		this.rating = rating;
		this.seats = new char[5][5]; // 기본적으로 5x5 좌석을 배정
		initializeSeats();				
	}

	public MovieVO() {
	
	}


	public MovieVO(String movieName) {
		this.movieName = movieName;
	}

	public String getMovieName() {
		return movieName;
	}



	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}



	public LocalDate getDate() {
		return date;
	}



	public void setDate(LocalDate date) {
		this.date = date;
	}



	public String getSupervision() {
		return supervision;
	}



	public void setSupervision(String supervision) {
		this.supervision = supervision;
	}



	public int getAgeLimit() {
		return ageLimit;
	}



	public void setAgeLimit(int ageLimit) {
		this.ageLimit = ageLimit;
	}



	public double getRating() {
		return rating;
	}



	public void setRating(double rating) {
		this.rating = rating;
	}



	public char[][] getSeats() {
		return seats;
	}



	public void setSeats(char[][] seats) {
		this.seats = seats;
	}
	
	

	public String getReservedSeat(MemberVO loginedMember) {
	    // 좌석 정보가 있는지 확인하고 반환
	    StringBuilder reservedSeats = new StringBuilder();

	    // reservations.csv 파일을 읽어서 처리
	    try (BufferedReader reader = new BufferedReader(new FileReader("reservations.csv"))) {
	        String line;

	        while ((line = reader.readLine()) != null) {
	            line = line.trim(); // 공백 제거
	            if (line.isEmpty() || !line.contains(",")) {
	                continue; // 빈 줄이나 유효하지 않은 데이터는 무시
	            }

	            // 데이터 구분 (memberId, movieName, row, col)
	            String[] data = line.split(",");
	            if (data.length < 4) {
	                continue; // 데이터 형식이 잘못된 경우 무시
	            }

	            String memberId = data[0]; // CSV에서 읽은 회원 ID
	            int row = Integer.parseInt(data[2]); // 좌석의 행
	            int col = Integer.parseInt(data[3]); // 좌석의 열

	            // 로그인한 회원의 ID와 일치하는지 확인
	            if (loginedMember.getId().equals(memberId)) {
	                // 예약된 좌석 정보 추가
	                reservedSeats.append(String.format("(%d열, %d행) ", row, col));
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    // 예약된 좌석이 있으면 반환하고, 없으면 "예매된 좌석이 없습니다." 반환
	    return reservedSeats.length() > 0 ? reservedSeats.toString() : "예매된 좌석이 없습니다.";
	}
	
	// 예약된 좌석 정보를 설정하는 메소드
	public void setReservedSeat(String reservedSeat) {
		this.reservedSeat = reservedSeat;
	}

	// 좌석 배열을 초기화하는 메소드 (모든 좌석을 '□'으로 설정)
    private void initializeSeats() {
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                seats[i][j] = '□'; // 모든 좌석을 빈 상태로 초기화
            }
        }
    }
	
    
    // 좌석 상태를 출력하는 메소드
	public void displaySeats() {
	    System.out.println("현재 좌석 상태:");
	    for (int i = 0; i < seats.length; i++) {
	        for (int j = 0; j < seats[i].length; j++) {
	            System.out.print(seats[i][j] + " ");
	        }
	        System.out.println();
	    }
	}
	
	
	// 좌석을 예약하는 메소드 (이미 예약된 좌석이면 경고 메시지 출력)
	public void bookSeat(int row, int col) {
	    if (seats[row][col] == '■') {
	        System.out.println("이미 예매된 좌석입니다. 다른 좌석을 선택해 주세요.");
	    } else {
	        seats[row][col] = '■';
	        System.out.println("좌석이 성공적으로 예매되었습니다.");
	    }
	}
	
	
	// 좌석 배열을 문자열로 변환하는 메소드 (CSV 형식으로 저장할 때 사용)
	public String seatsToString() {
	    StringBuilder sb = new StringBuilder();
	    for (char[] row : seats) {
	        for (char seat : row) {
	            sb.append(seat); // 좌석 정보를 문자열로 변환
	        }
	        sb.append(";"); // 각 행의 좌석 끝에 구분자를 추가
	    }
	    // 마지막 세미콜론 제거
	    if (sb.length() > 0) {
	        sb.setLength(sb.length() - 1);
	    }
	    return sb.toString();
	}

	// CSV에서 읽을 때 사용하기 위한 메서드
	public void stringToSeats(String seatData) {
	    // 'seatData'는 CSV 파일에서 가져온 좌석 정보를 ';'로 구분한 문자열로 되어 있다.
	    // 예시: "□□□■□;□□□□□;□□■□□;□□□□□;■□□□■"
	    String[] rows = seatData.split(";"); // ; 를 기준으로 행을 나눔
	    for (int i = 0; i < rows.length; i++) {
	        for (int j = 0; j < rows[i].length(); j++) {
	            seats[i][j] = rows[i].charAt(j); // 좌석 상태 복원
	        }
	    }
	}
	
	//movie.csv 저장 형식 선언
	public String toFileFormat() {
	    return movieName + "," + date + "," + supervision + "," + ageLimit + "," + rating + "," + seatsToString() ;
	}
	
	
}