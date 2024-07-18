package com.springbook.biz.board.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.springbook.biz.board.BoardVO;

@Repository
public class BoardDAOSpring{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	//SQL 명령어들
	
	private final String BOARD_LIST_T = 
			"select * from board where title like '%'||?||'%' order by seq desc";	 
	 private final String BOARD_LIST_C = 
	    		"select * from board where content like '%'||?||'%' order by seq desc";
	
	private final String BOARD_INSERT = "insert into board(seq, title, writer, content) values((select nvl(max(seq),0)+1 from board),?,?,?)";
	private final String BOARD_UPDATE = "update board set title=?, content=? where seq=?";
	private final String BOARD_DELETE = "delete board where seq=?";
	private final String BOARD_GET = "select * from board where seq=?";
	private final String BOARD_LIST = "select * from board order by seq desc";
	
	public void insertBoard(BoardVO vo) {
		System.out.println("===> Spring JDBC濡� insertBoard() 湲곕뒫泥섎━");
		jdbcTemplate.update(BOARD_INSERT, vo.getTitle(), vo.getWriter(), vo.getContent());
	}
	
	public void updateBoard(BoardVO vo) {
		System.out.println("===> Spring JDBC濡� updateBoard() 湲곕뒫泥섎━");
		jdbcTemplate.update(BOARD_UPDATE, vo.getTitle(), vo.getContent(), vo.getSeq());
		
	}
	
	public void deleteBoard(BoardVO vo) {
		System.out.println("===> Spring JDBC濡� deleteBoard() 湲곕뒫泥섎━");
		jdbcTemplate.update(BOARD_DELETE, vo.getSeq());
		
	}
	
	public BoardVO getBoard(BoardVO vo) {
		System.out.println("===> Spring JDBC濡� getBoard() 湲곕뒫泥섎━");
		Object[] args = {vo.getSeq()};
		return jdbcTemplate.queryForObject(BOARD_GET, args, new BoardRowMapper());
	}
	
	//글 목록 조회
	public List<BoardVO> getBoardList(BoardVO vo){
		System.out.println("===> Spring JDBC로 getBoardList() 기능처리");
		Object[] args = {vo.getSearchKeyword()};
		if(vo.getSearchCondition().equals("TITLE")) {
		return jdbcTemplate.query(BOARD_LIST_T,args, new BoardRowMapper());
		}else if(vo.getSearchCondition().equals("CONTENT")) {
			return jdbcTemplate.query(BOARD_LIST_C,args, new BoardRowMapper());			
		}
		return null;
		
	}
	
	
}


class BoardRowMapper implements RowMapper<BoardVO>{
	
	public BoardVO mapRow(ResultSet rs, int rowNum) throws SQLException{
		BoardVO board = new BoardVO();
		board.setSeq(rs.getInt("SEQ"));
		board.setTitle(rs.getString("TITLE"));
		board.setWriter(rs.getString("WRITER"));
		board.setContent(rs.getString("CONTENT"));
		board.setRegDate(rs.getDate("REGDATE"));
		board.setCnt(rs.getInt("CNT"));
		return board;
		
	}
	
}
