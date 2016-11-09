package com.cjon.bank.service;

import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.context.support.GenericXmlApplicationContext;

import com.cjon.bank.dao.BankDAO;
import com.cjon.bank.dto.BankDTO;
import com.cjon.bank.util.DBTemplate;

public class BankService {
	//injection이 필요하다. 따라서 필드가 존재해야 한다.
	
	private DBTemplate template;
	private BankDAO dao;
	
	public DBTemplate getTemplate() {
		return template;
	}

	public void setTemplate(DBTemplate template) {
		this.template = template;
	}

	public BankDAO getDao() {
		return dao;
	}

	public void setDao(BankDAO dao) {
		this.dao = dao;
	}

	public BankDTO deposit(BankDTO dto) {
		//입금에 대한 로직 처리
		
		//DBTemplate template=new DBTemplate();		
		//BankDAO dao=new BankDAO(template);
		
		dao.setTemplate(template);
		
		dto=dao.update(dto);
		if(dto.isResult()){
			template.commit();
		}else{
			template.rollback();
		}
		try {
			template.getCon().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dto;
		
	}

	public BankDTO withdraw(BankDTO dto) {
		//DBTemplate template=new DBTemplate();		
		//BankDAO dao=new BankDAO(template);
		
		dao.setTemplate(template);
		
		dto=dao.update(dto);
		if(dto.isResult()){
			template.commit();
		}else{
			template.rollback();
		}
		try {
			template.getCon().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dto;
		//BankDAO dao=new BankDAO();
		//dto=dao.updatewithdraw(dto);
		//return dto;
	}

	public ArrayList<BankDTO> transfer(BankDTO dto1, BankDTO dto2) {
		//DBTemplate template=new DBTemplate();
		//BankDAO dao=new BankDAO(template);
		
		dao.setTemplate(template);
		
		dto1=dao.updatewithdraw(dto1); //출금
		dto2=dao.update(dto2); //입금
		
		if(dto1.isResult() && dto2.isResult()){
			template.commit();
		}else{
			template.rollback();
		}
		
		ArrayList<BankDTO> list=new ArrayList<BankDTO>();
		list.add(dto1);
		list.add(dto2);
		
		return list;
	}

}

