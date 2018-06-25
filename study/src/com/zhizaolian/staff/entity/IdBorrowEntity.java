package com.zhizaolian.staff.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "OA_IdBorrow")
public class IdBorrowEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IDBorrow_Id", unique = true)
	private Integer IDBorrow_Id;
	private String User_Id;
	private String Item_User_Id;
	private String Reason;
	private Integer IsBorrow;
	private Date StartTime;
	private Date EndTime;
	private Date Real_startTime;
	private Date Real_endTime;
	private String ProcessInstanceID;
	private Integer ProcessStatus;
	private Integer ApplyResult;
	private Integer IsDeleted;
	private Date AddTime;
	private Date UpdateTime;
}
