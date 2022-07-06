package com.test.toy.board;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import com.test.toy.DBUtil;
import com.test.toy.etc.ChartDTO01;
import com.test.toy.etc.ChartDTO02;
import com.test.toy.etc.ChartDTO03;

public class BoardDAO {
	
	private Connection conn;
	private Statement stat;
	private PreparedStatement pstat;
	private ResultSet rs;
	
	public BoardDAO() {
		conn = DBUtil.open();
	}

	
	//AddOk 서블릿 > dto > 글쓰기
	public int add(BoardDTO dto) {
		
		try {
			String sql = "insert into tblBoard (seq, subject, content, id, regdate, readcount,thread,depth,filename,orgfilename) values (seqBoard.nextVal, ?, ?, ?, default, default,?,?,?,?)";
			
			
			pstat = conn.prepareStatement(sql);
			
			pstat.setString(1, dto.getSubject());
			pstat.setString(2, dto.getContent());
			pstat.setString(3, dto.getId());
			
			pstat.setInt(4, dto.getThread());
			pstat.setInt(5, dto.getDepth());
			
			pstat.setString(6, dto.getFilename());
			pstat.setString(7, dto.getOrgfilename());
			
			return pstat.executeUpdate();			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	//List 서블릿 > 목록 주세요
	public ArrayList<BoardDTO> list() {
		
		try {
			
			String sql = " select seq,subject,id,(select name from tbluser where id=tblBoard.id) as name,regdate,readcount,(select count(*) from tblComment where pseq=tblBoard.seq) as commentcount,depth,(sysdate-regdate) as isnew ,filename from tblBoard order by thread desc";
			
			stat = conn.createStatement();
			
			rs = stat.executeQuery(sql);
			
			ArrayList<BoardDTO> list = new ArrayList<BoardDTO>();
			
			while (rs.next()) {
				
				BoardDTO dto = new BoardDTO();
				
				dto.setSeq(rs.getString("seq"));
				dto.setSubject(rs.getString("subject"));
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("name"));
				dto.setRegdate(rs.getString("regdate"));
				dto.setReadcount(rs.getString("readcount"));
				dto.setCommentcount(rs.getString("commentcount"));
				
				dto.setDepth(rs.getInt("depth"));
				
				dto.setIsnew(rs.getDouble("isnew"));
				
				dto.setFilename(rs.getString("filename"));
				
				list.add(dto);
				
			}
			
			return list;			
			
		} catch (Exception e) {
			System.out.println("BoardDAO.list");
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	
	
	
	
	
	
	

	//View 서블릿 > seq > DTO 주세요.
	public BoardDTO get(String seq) {
		
		return getService(seq, "");
	}
	
	public BoardDTO get(BoardDTO dto) {
		
		return getService(dto.getSeq(), dto.getId()); 
	}


	private BoardDTO getService(String seq, String id) {
		
		try {
			
			String sql = "select tblBoard.*, (select name from tblUser where id = tblBoard.id) as name, (select sum(good) from tblGoodBad where bseq = tblBoard.seq) as good, (select sum(bad) from tblGoodBad where bseq = tblBoard.seq) as bad, (select  case when good = 1 then 1 when bad = 1 then 2 else 3 end from tblGoodBad where bseq = tblBoard.seq and id = ?) as goodbad from tblBoard where seq = ?";
			
			pstat = conn.prepareStatement(sql);
			pstat.setString(1, id);
			pstat.setString(2, seq);
			
			rs = pstat.executeQuery();
			
			BoardDTO dto = new BoardDTO();
			
			if (rs.next()) {
				
				dto.setSeq(rs.getString("seq"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("name"));
				dto.setRegdate(rs.getString("regdate"));
				dto.setReadcount(rs.getString("readcount"));
				
				dto.setThread(rs.getInt("thread"));
				dto.setDepth(rs.getInt("depth"));
				
				dto.setFilename(rs.getString("filename"));
				dto.setOrgfilename(rs.getString("orgfilename"));
				
				dto.setGood(rs.getString("good"));
				dto.setBad(rs.getString("bad"));
				dto.setGoodbad(rs.getString("goodbad"));
				
			}
			
			
			//해당글의 해시 태그들 가져오기
			sql = "select tag from tblHashTag h inner join tblTagging t on h.seq = t.hseq where bseq = ?";
			
			pstat = conn.prepareStatement(sql);
			pstat.setString(1, seq);
			
			rs = pstat.executeQuery();
			
			
			ArrayList<String> taglist = new ArrayList<String>();
			
			while (rs.next()) {
				taglist.add(rs.getString("tag"));
			}
			
			dto.setTaglist(taglist);
			
			
			return dto;
			
		} catch (Exception e) {
			System.out.println("BoardDAO.get");
			e.printStackTrace();
		}
		
		return null;
	}


	//View 서블릿 > seq > 조회수 증가시켜주세요
	public void updateReadcount(String seq) {

		try {
			
			String sql = "update tblBoard set readcount = readcount + 1 where seq = ?";
			
			pstat = conn.prepareStatement(sql);
			pstat.setString(1, seq);
			
			pstat.executeUpdate();			
			
		} catch (Exception e) {
			System.out.println("BoardDAO.updateReadcount");
			e.printStackTrace();
		}
		
	}


	//EditOk 서블릿 > dto > 수정해주세요~
	public int edit(BoardDTO dto) {
		
		try {
			
			String sql = "update tblBoard set subject = ?, content = ?,filename=? , orgfilename=?  where seq = ?";
			
			pstat = conn.prepareStatement(sql);
			
			pstat.setString(1, dto.getSubject());
			pstat.setString(2, dto.getContent());
			pstat.setString(3,dto.getFilename());
			pstat.setString(4, dto.getOrgfilename());
			pstat.setString(5, dto.getSeq());
			
			return pstat.executeUpdate();			
			
		} catch (Exception e) {
			System.out.println("BoardDAO.edit");
			e.printStackTrace();
		}
		
		return 0;
	}


	//DelOk 서블릿 > seq > 삭제해주세요~
	public int del(String seq) {
		
		try {
			
			String sql = "delete from tblBoard where seq = ?";
			
			pstat = conn.prepareStatement(sql);
			
			pstat.setString(1, seq);
			
			return pstat.executeUpdate();			
			
		} catch (Exception e) {
			System.out.println("BoardDAO.del");
			e.printStackTrace();
		}
		
		return 0;
	}

	
	//댓글입력한거 인서트
	public int addComment(CommentDTO dto) {
		try {
			
			String sql = "insert into tblComment (seq,content,id,regdate,pseq) values(seqComment.nextVal,?,?,default,?)";
			
			pstat = conn.prepareStatement(sql);
			
			pstat.setString(1, dto.getContent());
			pstat.setString(2, dto.getId());
			pstat.setString(3, dto.getPseq());
			
			return pstat.executeUpdate();			
			
		} catch (Exception e) {
			System.out.println("BoardDAO.del");
			e.printStackTrace();
		}
		
		
		return 0;
	}

	
	//부모글번호(seq) 줄테니 댓글 목록 주세요!  
	public ArrayList<CommentDTO> listComment(String seq) {
		try {
			//주의!!!!!!!where seq가 아니라 where pseq로 해줘야함!!!(부모글번호로!!)
			String sql="select tblComment.*, (select name from tblUser where id = tblComment.id) as name"
					+ " from tblComment where pseq= ? order by seq desc";
			
	
			pstat=conn.prepareStatement(sql);
			pstat.setString(1,seq);
			
			
			rs = pstat.executeQuery();
			
			
			ArrayList<CommentDTO> clist = new ArrayList<CommentDTO>();
			
			
			
			while(rs.next()) {
				
				CommentDTO dto =new CommentDTO();
				
				
				dto.setSeq(rs.getString("seq"));
				dto.setContent(rs.getString("content"));
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("name"));
				dto.setRegdate(rs.getString("regdate"));
				
				clist.add(dto);
			}
			
			return clist;
			
		}catch(Exception e) {
			e.printStackTrace();
		}
			
			

		return null;
	
	}
	

	
	
	//DelOk서블릿 > 원글삭제
	public int delcomment(String seq) {
		
		try {
		String sql="delete from tblComment where seq =?";
		
		pstat=conn.prepareStatement(sql);
		
		pstat.setString(1, seq);
		
		return pstat.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return 0;

	}

	
	//DelOk 서블릿 > 부모글번호 > 달린 댓글들 모두 삭제해보세요
	public void delCommentAll(String seq) {
		try {
			
		//부모의 글번호가 seq인것들을 삭제하시오!
		String sql="delete from tblComment where pseq =?";
		
		pstat=conn.prepareStatement(sql);
		
		pstat.setString(1, seq);
		
		pstat.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	
	
	//현존하는 게시글중 최고값 찾기
	public int getMaxTread() {
		
		try {
			
			String sql="select nvl(max(thread),0) as thread from tblBoard";
			
			stat=conn.createStatement();
			
			rs=stat.executeQuery(sql);
			
			if(rs.next()) {
				return rs.getInt("thread");
			}		
			
		}
		catch(Exception e) {
			
		}
		
		return 0;
	}

	
	//addok서블릿 > thread 업데이트
	public void updateThread(HashMap<String, Integer> map) {
		try {
			String sql="update tblBoard set thread=thread-1 where thread> ? and thread < ?";
			
			pstat=conn.prepareStatement(sql);
			
			pstat.setInt(1, map.get("previousThread"));
			pstat.setInt(2, map.get("parentThread"));
			
			pstat.executeUpdate();
		
		
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	
	
	//Addok 서블릿 > 방금 작성한 글번호 주세요~
	public String getSeq() {
		try {
			String sql="select max(seq) as seq from tblBoard";
			
			stat=conn.createStatement();
			
			rs=stat.executeQuery(sql);
			
			if(rs.next()) {
				return rs.getString("seq");
			}
			
		}
		catch(Exception e) {	
			e.printStackTrace();
		}
		return null;
	}

	
	
	//해쉬태그 관련 
	//hashtag테이블한테 태그 인서트해주기
	public void addHashTag(String tag) {
		try {
			String sql ="insert into tblHashTag (seq,tag) values(seqHashTag.nextVal,?)";
			pstat=conn.prepareStatement(sql);
			pstat.setString(1, tag);
			
			pstat.executeUpdate();
			
			
		}catch(Exception e){
			
		}
		
	}

	
	//해쉬태그 테이블의 seq가져오기
	public String getHashTagSeq() {
		try {
			String sql="select max(seq) as seq from tblHashTag";
			
			stat=conn.createStatement();
			
			rs=stat.executeQuery(sql);
			
			if(rs.next()) {
				return rs.getString("seq");
			}
			
		}
		catch(Exception e) {	
			e.printStackTrace();
		}
		return null;
	}

	
	
	//연결해주는(tagging)에다가 bseq(게시판 글번호), hseq(해쉬테이블의 글번호) 넣기
	public void addTagging(HashMap<String, String> map) {
		try {
			String sql = "insert into tblTagging (seq, bseq, hseq) values (seqTagging.nextVal, ?, ?)";
			
			pstat = conn.prepareStatement(sql);
			pstat.setString(1, map.get("bseq"));
			pstat.setString(2, map.get("hseq"));
			
			pstat.executeUpdate();
			
		}catch(Exception e){
			
		}
		
	}

	
	//add.java> 자동완성 기능을 위해 태그 목록 전부 가져오기 
	public ArrayList<String> taglist() {
		try {
			
			String sql ="select tag from tblHashTag order by tag asc";
			
			stat=conn.createStatement();
			rs=stat.executeQuery(sql);
			
			ArrayList<String> list = new ArrayList<String>();
			
			while(rs.next()) {
				list.add(rs.getString("tag"));
			}
			
			return list;
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	
	
	//GoodBad 서블릿 > dto > 좋아요/싫어요 반영해주세요.
		public int updateGoodBad(GoodBadDTO dto) {
			
			try {
				
				//1. 참여(X) > 참여(O)
				//2. 참여(O) > 같은 항목 참여 > 방지!! > XXXXX
				//3. 참여(O) > 다른 항목 참여 > 변경
				//4. 참여(O) > 같은 항목 참여 > 취소!!
				
				
				int state = 0;
				String good = "";
				String bad = "";
				String seq = "";
				
				String sql = "select * from tblGoodBad where bseq = ? and id = ?";
				
				pstat = conn.prepareStatement(sql);
				
				pstat.setString(1, dto.getBseq());
				pstat.setString(2, dto.getId());
				
				rs = pstat.executeQuery();
				
				if (rs.next() ) {
					//참여(O)
					
					
					if (!dto.getGood().equals(rs.getString("good"))) {
						//반대 의견 선택
						state = 2;
						
						seq = rs.getString("seq");
						good = rs.getString("good");
						bad = rs.getString("bad");
						
						if (good.equals("1")) {
							good = "0";
							bad = "1";
						} else {
							good = "1";
							bad = "0";
						}
					} else {
						//이미 선택 의견 취소
						state = 3;	
						
						seq = rs.getString("seq");
					}
					
				}//if(rs.next())
				else {
					//참여(X)
					state = 1;
				}
				
				rs.close();
				pstat.close();
				
				
				
				if (state == 1) {
					sql = "insert into tblGoodBad (seq, id, bseq, good, bad) values (seqGoodBad.nextVal, ?, ?, ?, ?)";
					
					pstat = conn.prepareStatement(sql);
					
					pstat.setString(1, dto.getId());
					pstat.setString(2, dto.getBseq());
					pstat.setString(3, dto.getGood());
					pstat.setString(4, dto.getBad());
					
					return pstat.executeUpdate();
				} else if (state == 2) {
					sql = "update tblGoodBad set good = ?, bad = ? where seq = ?";
					
					pstat= conn.prepareStatement(sql);
					
					pstat.setString(1, good);
					pstat.setString(2, bad);
					pstat.setString(3, seq);
					
					return pstat.executeUpdate();
				} else if (state == 3) {
					sql = "delete from tblGoodBad where seq = ?";
					
					pstat = conn.prepareStatement(sql);
					
					pstat.setString(1, seq);
					
					return pstat.executeUpdate();
				}
				
			} catch (Exception e) {
				System.out.println("BoardDAO.updateGoodBad");
				e.printStackTrace();
			}
			
			return 0;
		}
		
		
		
		
		


	public ArrayList<ChartDTO01> chart1() {
		
		try {
			
			String sql = "select u.id, (select name from tblUser where id = u.id) as name, (select count(*) from tblBoard where id = u.id) as cnt from tblBoard b right outer join tblUser u on u.id = b.id group by u.id";
			
			stat = conn.createStatement();
			
			rs = stat.executeQuery(sql);
			
			ArrayList<ChartDTO01> list = new ArrayList<ChartDTO01>();
			
			while (rs.next()) {
				
				ChartDTO01 dto = new ChartDTO01();
				
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("name"));
				dto.setCnt(rs.getString("cnt"));
				
				list.add(dto);
				
			}
			
			return list;			
			
			
		} catch (Exception e) {
			System.out.println("BoardDAO.chart1");
			e.printStackTrace();
		}
		
		return null;
	}



	public ArrayList<ChartDTO02> chart2() {
		
		try {
			
			String sql = "select u.id, (select name from tblUser where id = u.id) as name, (select count(*) from tblComment where id = u.id) as cnt from tblComment b right outer join tblUser u on u.id = b.id group by u.id";
			
			stat = conn.createStatement();
			
			rs = stat.executeQuery(sql);
			
			ArrayList<ChartDTO02> list = new ArrayList<ChartDTO02>();
			
			while (rs.next()) {
				
				ChartDTO02 dto = new ChartDTO02();
				
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("name"));
				dto.setCnt(rs.getString("cnt"));
				
				list.add(dto);
				
			}
			
			return list;			
			
			
		} catch (Exception e) {
			System.out.println("BoardDAO.chart2");
			e.printStackTrace();
		}
		
		return null;
	}


	public ArrayList<ChartDTO03> chart3() {
	try {
			
			String sql = "select h.tag, (select count(*) from tblTagging where hseq = h.seq) as cnt from tblHashTag h left outer join tblTagging t on h.seq = t.hseq group by h.tag, h.seq";
			
			stat = conn.createStatement();
			
			rs = stat.executeQuery(sql);
			
			ArrayList<ChartDTO03> list = new ArrayList<ChartDTO03>();
			
			while (rs.next()) {
				
				ChartDTO03 dto = new ChartDTO03();
				
				dto.setTag(rs.getString("tag"));
				dto.setCnt(rs.getString("cnt"));
				
				list.add(dto);
				
			}
			
			return list;			
			
			
		} catch (Exception e) {
			System.out.println("BoardDAO.chart3");
			e.printStackTrace();
		}
		
		return null;
	}


	public ArrayList<BoardDTO> list(HashMap<String, String> map) {
		// TODO Auto-generated method stub
		return null;
	}



}





















