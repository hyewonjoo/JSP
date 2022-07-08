package com.test.toy.board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Random;

import com.test.toy.DBUtil;

//페이징 하기 위해 더미데이터 만드는 클래스
public class Dummy {
	public static void main(String[] args) {
	
	//게시글 더미데이터 생성하기
		String sql = "insert into tblBoard (seq, subject, content, id, regdate, readcount) values (seqBoard.nextVal, ?, ?, ?, default, default)";
		
		
		//시드 데이터
		String[] subject= {"안녕","하세요","이것은","더미","데이터를","만들기","위한","노가다","라고하죠","왜이렇게","귀찮은지","모르겠지만","해야하긴","해야",
				"합니다","요즘은","장마철","이에요","우산을","꼭","챙기고다니셔야","합니다","다들","우리모두","건강이","최우선","인거","아시죠"};
		
		
		String content="내용입니다";
		
		String[] id= {"hong","admin","heywon"};
		
		
		
		//더미데이터를 만들고 업데이트까지 직접 db에 insert까지 해줌
		Connection conn=null;
		PreparedStatement stat=null;
		
		try {
			conn=DBUtil.open();
			stat=conn.prepareStatement(sql);
			
			Random rnd=new Random();
			
			for(int i=0; i<256;i++) {
				stat.setString(1, subject[rnd.nextInt(subject.length)]+" "+subject[rnd.nextInt(subject.length)]+
						" "+subject[rnd.nextInt(subject.length)]+" "+subject[rnd.nextInt(subject.length)]+" "+subject[rnd.nextInt(subject.length)]+" ");
			
				
				stat.setString(2, content);
				stat.setString(3, id[rnd.nextInt(id.length)]);
				
				stat.executeUpdate();
				
				System.out.println(i);
			
			
			}
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
}
