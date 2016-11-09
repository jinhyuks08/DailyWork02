package com.cjon.bank.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.cjon.bank.dto.BankDTO;
import com.cjon.bank.util.DBTemplate;

public class BankDAO {
	
	private DBTemplate template;
	
	public DBTemplate getTemplate() {
		return template;
	}

	public void setTemplate(DBTemplate template) {
		this.template = template;
	}

	public BankDAO() {
		
	}
	
	public BankDAO(DBTemplate template) {
		this.template=template;
	}
	public BankDTO update(BankDTO dto) {
		//DB처리
		//일반 jdbc 처리 코드를 작성한다.
		//Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		Connection con=template.getCon();
		
		try {
			//1. jdbc driver loading(mysql용)
			//Class.forName("com.mysql.jdbc.Driver");
			
			//2. DB 접속
			//String url="jdbc:mysql://localhost:3306/library"; //jdbc url
			//String id="teddy";
			//String pw="teddy";
			//con=DriverManager.getConnection(url, id, pw);
			
			//con.setAutoCommit(false); //transaction 시작
			
			//3. 사용할 sql을 작성하고 sql을 가지고 preparedstatement를 생성한다.
			String sql="update bank set balance = balance+? where userid=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, dto.getBalance());
			pstmt.setString(2, dto.getUserid());
			
			//4. 실행하기
			int count= pstmt.executeUpdate();
			
			//5. 결과처리
			if(count==1){
				//정상적으로 처리된다면 실행되는 부분
				String sql1="select userid, balance from bank where userid=?";
				PreparedStatement pstmt1=con.prepareStatement(sql1);
				pstmt1.setString(1, dto.getUserid());	//in parameter 처리
				rs=pstmt1.executeQuery();
				if(rs.next()){
					dto.setBalance(rs.getInt("balance"));
				}
				//con.commit(); //commit을 선언해주지 않으면 transaction은 무조건 rollback된다.
				//template.commit();
				dto.setResult(true);	//정상처리되었다는 것을 DTO에 저장
				try {
					rs.close();
					pstmt1.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				//con.rollback();
				//template.rollback();
				dto.setResult(false);	//잘못처리되었다는 것을 DTO에 저장
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				pstmt.close();
				//con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return dto;
	}

	public BankDTO updatewithdraw(BankDTO dto) {
				//DB처리
				//일반 jdbc 처리 코드를 작성한다.
				//Connection con=null;
				PreparedStatement pstmt=null;
				ResultSet rs=null;
				Connection con=template.getCon();
				
				try {
					//1. jdbc driver loading(mysql용)
					//Class.forName("com.mysql.jdbc.Driver");
					
					//2. DB 접속
					//String url="jdbc:mysql://localhost:3306/library"; //jdbc url
					//String id="teddy";
					//String pw="teddy";
					//con=DriverManager.getConnection(url, id, pw);

					//con.setAutoCommit(false);	//transaction의 시작부분 기존의 autocommit은 true값으로 default 설정되어있다.
					//con에 대해서 commit되거나 rollback되거나 정상적으로 con이 close될 때 transaction이 종료(rollback 처리가 된다.)
					
					//3. 사용할 sql을 작성하고 sql을 가지고 preparedstatement를 생성한다.
					String sql="update bank set balance = balance-? where userid=?";
					pstmt=con.prepareStatement(sql);
					pstmt.setInt(1, dto.getBalance());
					pstmt.setString(2, dto.getUserid());
					
					//4. 실행하기
					int count= pstmt.executeUpdate();
					
					//5. 결과처리
					if(count==1){
						//정상적으로 처리된다면 실행되는 부분
						String sql1="select userid, balance from bank where userid=?";
						PreparedStatement pstmt1=con.prepareStatement(sql1);
						pstmt1.setString(1, dto.getUserid());	//in parameter 처리
						rs=pstmt1.executeQuery();
						if(rs.next()){
							dto.setBalance(rs.getInt("balance"));
						}
						if(dto.getBalance()<0){
							System.out.println("잔액 부족으로 인한 출금 실패");
							dto.setResult(false);
							//con.rollback();
						}else{
							dto.setResult(true);
							//con.commit();
						}
						try {
							rs.close();
							pstmt1.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					try {
						pstmt.close();
						//con.close();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
				return dto;
	}

}

