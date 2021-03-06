package com.zhizaolian.staff.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OA_BussinessTrip")
public class TripEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BussinessTrip_id", unique = true)
	private Integer bussinessTripID;
	private String requestUserID;
	private String requestUserName;
	private String reason;
	private Date startTime;
	private Date endTime;
	private Integer isNeedTicket;
	private String ticketDetail;
	private String processInstanceID;
	private Integer applyResult;
	private Integer isDeleted;
	private Date addTime;
	private Date updateTime;
	private String userID ;
	private String itemPlace;
	private String vehicle;
}
