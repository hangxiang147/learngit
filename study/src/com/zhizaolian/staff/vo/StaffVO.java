package com.zhizaolian.staff.vo;

import java.io.File;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class StaffVO {

	private String userID;  //用户ID
	private Integer staffID; 
	private String staffNumber;  //员工工号
	private String lastName;  //中文姓名
	private String userName;  //系统登录用户名
	private Integer positionCategory;//岗位类型
	private String password;  //登录密码
	private String gender;  //性别
	private String birthday;  //出生年月
	private String telephone;  //联系电话
	private String idNumber;  //身份证号码
	private String education;  //最高学历
	private String major;  //专业
	private String school;  //毕业学校
	private String graduationDate;  //毕业日期 
	private String educationID;  //学历证书编号
	private String degreeID;  //学位证书编号
	private Integer maritalStatus;  //婚姻状况
	private String nativePlace;  //籍贯
	private String address;  //住址
	private String emergencyContract;  //紧急联系人
	private String emergencyPhone;  //紧急联系人电话
	private File picture;  //照片
	private String pictureExt;  //照片类型
	private String entryDate;  //入职日期
	private Integer companyID;  //所属公司ID
	private String companyName;  //公司名称
	private Integer departmentID;  //所属部门ID
	private String departmentName;  //部门名称
	private Integer positionID;  //职位ID
	private String positionName;  //ְ职位名称
	private Integer gradeID;  //职级ID
	private String gradeName;  //职级名称
	private String salary;  //薪资标准
	private Double performance;//绩效工资
	private Integer status;  //在职状态
	private String superiorName;  //直系上司中文名
	private Integer auditStatus;  //审核状态
	private String criminalRecord;  //案底记录
	private List<GroupDetailVO> groupDetailVOs;  //岗位列表
	private Date entryDate1;//入职日期
	private String days;//入职天数
	private String age;//年龄
	private String groupDetail;//岗位
	private String formalDate;//转正日期
    private String examineDate;//审批日期
    private String formalBeginDate;
    private String formalEndDate;
    private String examineBeginDate;
    private String examineEndDate;
    private String attachementNames;  //上传附件文件名   
    private String nickName;
	private String[] skills;
	private String skill;
	private String[] masters;
	private String master;
	private Integer skillID;
	private byte[] picArray;  //个人照片二进制流
	private String companyPhone;//公司电话
	private String email;
	private String starSign;
	private boolean isPartner;//是否是合伙人
	private String registrationFormId;
	private File registrationForm;
	private String registrationFormFileName;
	private String annualVacationInfo;//年假信息
	private String monthlyRestDays;//每月公休天数
	private String monthlyWorkDays;//每月应出勤天数
	private String insurance;
	private String openId;
	private String headImgId;
	private Integer staffSalaryId;//薪资标准表的id
	private Double standardSalary;
	private String bankAccount;//银行卡号
	private String bank;//开户行 
	private String personalPost;//岗位名称
	private Integer managePersonNum;//管理人员下限
	private String weixinCodeId;
	private File weixinCode;
	private String weixinCodeFileName;
	private String[] checkItem;//体检项目
	private String checkItems;
	private String contractEndDate;//劳动合同到期时间
	private String payFund;//是否交公积金
	private String positionType;
	private List<String> departmentNames;
	}
