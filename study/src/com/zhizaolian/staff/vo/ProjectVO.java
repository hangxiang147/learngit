package com.zhizaolian.staff.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectVO {
	private Integer id;
	private String name;
	private String description;
	private Integer isDelete;
	private Date addTime;
	private Date updateTime;
	private String code;
	private String projectHeaderId;
	private String projectHeaderName;
	private String testHeaderId;
	private String testHeaderName;
	private String creatorId;
	private String updatestVersion;
	private String versions;
	private String modules;
	private Double  xq;
	private Double ss;
	private Double jl;
	private Double kf;
	private Double cs;
	private Double zz;
}
