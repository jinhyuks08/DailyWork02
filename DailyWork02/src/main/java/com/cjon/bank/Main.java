package com.cjon.bank;

import java.util.ArrayList;
import java.util.Scanner;

import org.springframework.context.support.GenericXmlApplicationContext;

import com.cjon.bank.dao.BankDAO;
import com.cjon.bank.dto.BankDTO;
import com.cjon.bank.service.BankService;

public class Main {

	public static void main(String[] args) {
		
		String config="classpath:applicationCtx.xml";
		
		GenericXmlApplicationContext ctx=new GenericXmlApplicationContext();
		//BankDAO dao=ctx.getBean("dao", BankDAO.class);
		
		ctx.load(config);
		
		ctx.refresh();
		
		Scanner s=new Scanner(System.in);
		int menu=0;
		
		do{
			System.out.println("----은행 시스템----");
			System.out.println("1. 입금");
			System.out.println("2. 출금");
			System.out.println("3. 이체");
			System.out.println("4. 종료");			
			System.out.print("사용할 메뉴를 입력하세요 ==> ");			
			String menuString=s.nextLine();	//입력받은 숫자를 넘겨받는 명령어
			menu=Integer.parseInt(menuString);
			
			if(menu==1){
				System.out.println("----입금 업무입니다.----");
				System.out.print("입금할 사용자의 ID를 입력하세요 ==> ");
				String id=s.nextLine();
				System.out.print("입금할 금액을 입력하세요 ==> ");
				String moneyString=s.nextLine();
				int money=Integer.parseInt(moneyString);
				//데이터의 전달을 위해서 DTO 객체를 생성
				//BankDTO dto=new BankDTO();
				BankDTO dto=ctx.getBean("dto", BankDTO.class); //bean 객체
				dto.setUserid(id);
				dto.setBalance(money);
				//로직처리를 위해 서비스 객체를 생성한다.
				BankService service=ctx.getBean("service", BankService.class);
				//BankService service=new BankService();
				dto=service.deposit(dto);
				System.out.println("입금 내역은 다음과 같습니다.");
				System.out.println("userID: " + dto.getUserid() + ", 잔액: " + dto.getBalance());
			}
			if(menu==2){
				System.out.println("----출금 업무입니다.----");
				System.out.print("출금할 사용자의 ID를 입력하세요 ==> ");
				String id=s.nextLine();
				System.out.print("출금할 금액을 입력하세요 ==> ");
				String moneyString=s.nextLine();
				int money=Integer.parseInt(moneyString);
				//데이터의 전달을 위해서 DTO 객체를 생성
				//BankDTO dto=new BankDTO();
				BankDTO dto=ctx.getBean("dto", BankDTO.class);
				dto.setUserid(id);
				dto.setBalance(money);
				//로직처리를 위해 서비스 객체를 생성한다.
				//BankService service=new BankService();
				BankService service=ctx.getBean("service", BankService.class);
				dto=service.withdraw(dto);
				System.out.println("출금 내역은 다음과 같습니다.");
				System.out.println("userID: " + dto.getUserid() + ", 잔액: " + dto.getBalance());
			}
			if(menu==3){
				System.out.println("----이체 업무입니다.----");
				System.out.print("이체 금액을 출금할 사용자의 ID를 입력하세요 ==> ");
				String id1=s.nextLine(); //출금자의 ID 
				System.out.print("이체 금액이 입금될 사용자의 ID를 입력하세요 ==> ");
				String id2=s.nextLine(); //입금자의 ID
				System.out.print("이체할 금액을 입력하세요 ==> ");
				String moneyString=s.nextLine();
				int money=Integer.parseInt(moneyString);
				//데이터의 전달을 위해서 DTO 객체를 생성
				BankDTO dto1=ctx.getBean("dto", BankDTO.class);
				//BankDTO dto1=new BankDTO(); //출금처리를 위한 DTO
				dto1.setUserid(id1);
				dto1.setBalance(money);
				
				BankDTO dto2=ctx.getBean("dto", BankDTO.class);
				//BankDTO dto2=new BankDTO(); //입금처리를 위한 DTO
				dto2.setUserid(id2);
				dto2.setBalance(money);
				
				//로직처리를 위해 서비스 객체를 생성한다.
				//BankService service=new BankService();
				BankService service=ctx.getBean("service", BankService.class);
				ArrayList<BankDTO> list= service.transfer(dto1,dto2);
				
				//dto1=service.withdraw(dto1);
				//dto2=service.deposit(dto2);
				
				dto1=list.get(0);	//처리된 출금자에 대한 정보를 가져옴
				dto2=list.get(1);	//처리된 입금자에 대한 정보를 가져옴
				
				System.out.println("이체 후 출금자의 내역은 다음과 같습니다.");
				System.out.println("userID: " + dto1.getUserid() + ", 잔액: " + dto1.getBalance());
				System.out.println("이체 후 입금자의 내역은 다음과 같습니다.");
				System.out.println("userID: " + dto2.getUserid() + ", 잔액: " + dto2.getBalance());
			}
			if(menu==4){
				System.out.println("----은행 시스템을 종료합니다.----");
			}
			
		}while(menu!=4);
		
		s.close();
		ctx.close();
		
	}

}

