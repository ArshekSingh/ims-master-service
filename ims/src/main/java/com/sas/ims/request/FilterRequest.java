package com.sas.ims.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilterRequest {
    private Integer branchId;
    private Long centerId;
    private Long clientId;
    private Long departmentId;
    private Long organizationId;
    private Long applicationId;
    private Integer paymentAttempt;
    private String applicationStatus;
    private String date;
    private int start;
    private int limit;
    private String startDate;
    private String endDate;
    private String genericId;
    private String genericId1;
    private String status;
    private String meetingType;
    private String assignedTo;
    private String centerCode;
    private Long loanId;
    private Long employeeId;
    private String employeeName;
    private Long designationId;
    private String batchId;
    private Integer stateId;
    private Integer purposeId;
    private Integer subPurposeId;
    private String ledgerType;
    private Integer reconNumber;
    private Long disbSeqNo;
    private Integer collSequence;
    private Long meetingId;
    private String reschDate;
    private String reschTime;
    private String reschReason;
    private String reschPermanent;
    private String centerDayFlag;
    private String emplDesigType;
    private Long emplDesigAreaId;
    private String isManager;
    private String isMeetingTransfer;
    private Long basedLocationId;
    private String criteriaId;
    private String funderId;
    private String lenderId;
    private Long disbSeqNum;
    private Integer deathId;
    private String caseProcessStartDate;
    private String caseProcessEndDate;
    private String userId;
    private String name;
    private String isActive;
    private String branchType;
    private String branchCode;
    private Integer pincode;
    private List<Integer> branchList;
    private List<Long> centerList;
    private String type;
    private Integer parentId;
    private Integer villageId;
    private Integer productGroupId;
    private Integer productId;
    private String bcPartnerId;
    private String bankName;
    private String bankCode;
    private String aadharNumber;
    private String votercardNumber;
    private String isCsv;
    private Integer foId;
    private String currentDate;
    private String documentType;
    private String documentName;
    private String bankAccCode;
    private String clientCode;
    private String loanCode;
    private String fromClosureDate;
    private String toClosureDate;
    private String waiverType;
    private String waiverDate;
    private BigDecimal waiverAmount;
    private String waiverRemarks;
    private Long waiverSeqNumber;
    private Integer districtId;
    private String ifscCode;
    private String infoVerifiedStatus;
    private String employeeCode;
    private String loanStatus;
    private String agentId;
    private Integer zoneId;
    private Integer divisionId;
    private String neftStartDate;
    private String neftEndDate;
    private String repaymentOver;
    private String extLanNo;
    private String extLoanId;
    private String isSubCenter;
    private List<String> centerStatus;
    private String insertedBy;
    private String cbPartner;
    private String productGroupType;
    private String bcPartner;
    private Long orgId;
    private String eligibleForClm;
    private String isClmAssingned;
    private String clmStatus;
    private String clientName;
    private String sanctionStatus;
    private Integer circleId;
    private Integer id;
    private String branchBCPartner;
    private String sanctionStartDate;
    private String sanctionEndDate;
}