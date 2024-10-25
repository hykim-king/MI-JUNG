package com.pcwk.ehr.movie;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.pcwk.ehr.cmn.DTO;
import com.pcwk.ehr.cmn.WorkDiv;
import com.pcwk.ehr.member.MemberVO;

public class MovieDao {
    public static List<MovieVO> movieList = new ArrayList<>();
    private static final String MOVIE_FILE_PATH = "movie.csv"; 
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
    
    
    //영화 정보 저장 메소드(movie.csv)
    public int doSave(MovieVO vo) {
        movieList.add(vo); //영화 리스트에 새로운 영화 추가
        return writeFile(MOVIE_FILE_PATH); 
    }

    //영화 정보 업데이트 메소드
    public int doUpdate(MovieVO movie) {
        for (int i = 0; i < movieList.size(); i++) {
        	//받은 값과 영화 이름이 같은 경우 리스트 내 해당 인덱스에서 영화 정보를 업데이트
            if (movieList.get(i).getMovieName().equalsIgnoreCase(movie.getMovieName())) {
                movieList.set(i, movie);
                return writeFile(MOVIE_FILE_PATH); 
            }
        }
        return 0; // 업데이트 실패 시 0 반환
    }

    
   //영화 정보 삭제 메소드
    public int doDelete(MovieVO vo) {
        MovieVO movieToDelete = doSelectOne(vo); //영화 리스트에서 일치하는 영화 찾기
        if (movieToDelete != null) {
            movieList.remove(movieToDelete); //영화 삭제
            return writeFile(MOVIE_FILE_PATH);
        }
        return 0; // 삭제 실패 시 0 반환
    }

    
    //영화 이름을 기준으로 검색하여 영화 하나의 정보를 반환하는 메소드
    public MovieVO doSelectOne(MovieVO vo) {
        for (MovieVO movie : movieList) {
        	//영화 이름이 일치하는 경우 해당 영화 정보 반환
            if (movie.getMovieName().equalsIgnoreCase(vo.getMovieName())) {
                return movie;
            }
        }
        return null; // 검색 실패 시 null 반환
    }

   
    //모든 영화 정보 반환
    public List<MovieVO> doRetrieve(DTO dto) {
        return movieList; // 모든 영화 목록을 반환
    }

    
    //영화 리스트를 파일로 저장하는 메소드
    public int writeFile(String path) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (MovieVO movie : movieList) {
            	
            	// 각 영화 정보를 CSV 형식으로 변환 후 파일에 저장
                String movieData = String.format("%s,%s,%s,%d,%.2f,%s",
                        movie.getMovieName(),
                        movie.getDate().format(dateFormatter),
                        movie.getSupervision(),
                        movie.getAgeLimit(),
                        movie.getRating(),
                        movie.seatsToString()); // 좌석 정보를 문자열로 저장
                bw.write(movieData);
                bw.newLine(); //줄바꿈
            }
            return 1; // 파일 저장 성공 시 1 반환
        } catch (IOException e) {
            System.out.println("파일 저장 중 오류가 발생했습니다: " + e.getMessage());
            return 0; // 파일 저장 실패 시 0 반환
        }
    }

    
    //영화 리스트에서 파일 읽어오는 메소드
    public int readFile(String path) {
        movieList.clear(); // 기존 데이터를 초기화
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
            	//각 줄을 쉼표로 구분하여 영화 정보를 읽어둠
                String[] data = line.split(",");
                MovieVO movie = new MovieVO(data[0], LocalDate.parse(data[1]), data[2],
                        Integer.parseInt(data[3]), Double.parseDouble(data[4]));
                movie.stringToSeats(data[5]); // 좌석 정보를 복원
                movieList.add(movie);
            }
            return 1; // 파일 로드 성공 시 1 반환
        } catch (IOException e) {
            System.out.println("파일 읽기 중 오류가 발생했습니다: " + e.getMessage());
            return 0; // 파일 로드 실패 시 0 반환
        }
    }


	public int doUpdate(MemberVO vo) {
		// TODO Auto-generated method stub
		return 0;
	}


	public int doDelete(MemberVO vo) {
		// TODO Auto-generated method stub
		return 0;
	}

	public MemberVO doSelectOne(MemberVO vo) {
		// TODO Auto-generated method stub
		return null;
	}
}