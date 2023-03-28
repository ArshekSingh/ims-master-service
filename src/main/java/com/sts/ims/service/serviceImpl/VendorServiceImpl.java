package com.sts.ims.service.serviceImpl;

import com.sts.ims.dto.VendorDto;
import com.sts.ims.entity.ApprovalDetail;
import com.sts.ims.entity.OrganisationHierarchy;
import com.sts.ims.entity.VendorMaster;
import com.sts.ims.repository.ApprovalDetailRepository;
import com.sts.ims.repository.OrganisationHierarchyRepository;
import com.sts.ims.repository.VendorMasterRepository;
import com.sts.ims.response.Response;
import com.sts.ims.service.VendorService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VendorServiceImpl implements VendorService {
    private final VendorMasterRepository vendorMasterRepository;
    private final OrganisationHierarchyRepository organisationHierarchyRepository;
    private final ApprovalDetailRepository approvalDetailRepository;

    @Autowired
    com.sas.utils.UserUtils userUtils;

    @Override
    public Response getVendorById(Long vendorId) {
        Optional<VendorMaster> vendorMaster = vendorMasterRepository.findById(vendorId);
        return vendorMaster.map(master -> {
            VendorDto vendorDto = vendorMasterToDto(vendorMaster.get());
            return new Response("Transaction Successful", vendorDto, HttpStatus.OK);
        }).orElseGet(() -> new Response("Vendor not found!", HttpStatus.NOT_FOUND));
    }

    @Override
    public Response getVendorList(Integer page, Integer size) {
        List<VendorMaster> vendors = vendorMasterRepository.findByOrgId(userUtils.getLoggedUser().getOrgId(), PageRequest.of(page, size)).getContent();
        List<VendorDto> vendorDtoList = vendors.stream().map(this::vendorMasterToDto).collect(Collectors.toList());
        return new Response("Transaction Successful", vendorDtoList, HttpStatus.OK);
    }

    @Override
    public Response saveVendor(VendorDto vendorDto) {
        //Create vendor master from request
        VendorMaster vendorMaster = vendorDtoToMaster(vendorDto);

        //Fetch approval matrix from Organisation Hierarchy
        List<OrganisationHierarchy> organisationHierarchyList = organisationHierarchyRepository.findByOrgIdAndHierarchyCodeAndHierarchyTypeAndStatus(userUtils.getLoggedUser().getOrgId(), "VENDOR", "GEO", true);
        if (!organisationHierarchyList.isEmpty()) {
            organisationHierarchyList.forEach(organisationHierarchy -> {
                ApprovalDetail approvalDetail = new ApprovalDetail();
                approvalDetail.setOrgId(userUtils.getLoggedUser().getOrgId());
                approvalDetail.setEntityId(organisationHierarchy.getId());
                approvalDetail.setEntityType(organisationHierarchy.getHierarchyType());
                approvalDetail.setApprovalType("VENDOR");
                approvalDetail.setApprovalStatus("I");
                approvalDetailRepository.save(approvalDetail);
            });
        }

        vendorMasterRepository.save(vendorMaster);
        return new Response("Transaction Successful", HttpStatus.OK);
    }

    @Override
    public Response updateVendor(VendorDto vendorDto) {
        Optional<VendorMaster> vendorMaster = vendorMasterRepository.findById(vendorDto.getVendorId());
        if (vendorMaster.isPresent()) {

        }
        return null;
    }

    @Override
    public Response removeVendor(VendorDto vendorDto) {
        Optional<VendorMaster> vendorMaster = vendorMasterRepository.findById(vendorDto.getVendorId());
        if (vendorMaster.isPresent()) {
            return new Response();
        } else {
            return new Response("Vendor Doesn't Exists", HttpStatus.NOT_FOUND);
        }
    }

    private VendorDto vendorMasterToDto(VendorMaster vendorMaster) {
        VendorDto vendorDto = new VendorDto();
        vendorDto.setOrgId(vendorMaster.getOrgId());
        vendorDto.setVendorId(vendorMaster.getVendorId());
        vendorDto.setVendorName(vendorMaster.getVendorName());
        vendorDto.setVendorGroup(vendorMaster.getVendorGroup());
        vendorDto.setVendorType(vendorMaster.getVendorType());
        vendorDto.setAddress1(vendorMaster.getAddress1());
        vendorDto.setAddress2(vendorMaster.getAddress2());
        vendorDto.setAddress3(vendorMaster.getAddress3());
        vendorDto.setPincode(vendorMaster.getPincode());
        vendorDto.setPhone(vendorMaster.getPhone());
        vendorDto.setTelefax(vendorMaster.getTelefax());
        vendorDto.setEmailId(vendorMaster.getEmailId());
        vendorDto.setWebsite(vendorMaster.getWebsite());
        vendorDto.setMobileNumber(vendorMaster.getMobileNumber());
        vendorDto.setBankIfscCode(vendorMaster.getBankIfscCode());
        vendorDto.setBankName(vendorMaster.getBankName());
        vendorDto.setBankMmid(vendorMaster.getBankMmid());
        vendorDto.setBankVpa(vendorMaster.getBankVpa());
        vendorDto.setBankAccount(vendorMaster.getBankAccount());
        vendorDto.setBankBranch(vendorMaster.getBankBranch());
        vendorDto.setTaxpayerNumber(vendorMaster.getTaxpayerNumber());
        vendorDto.setPan(vendorMaster.getPan());
        vendorDto.setSvctaxRegnum(vendorMaster.getSvctaxRegnum());
        vendorDto.setSource(vendorMaster.getSource());
        vendorDto.setGstin(vendorMaster.getGstin());
        vendorDto.setStateId(vendorMaster.getStateId());
        vendorDto.setGstrType(vendorMaster.getGstrType());
        vendorDto.setTypeOfEnterprises(vendorMaster.getTypeOfEnterprises());
        vendorDto.setRegistrationNo(vendorMaster.getRegistrationNo());
        vendorDto.setOneTimeVendor(vendorMaster.getOneTimeVendor());
        vendorDto.setVendorStatus(vendorMaster.getVendorStatus());
        vendorDto.setDocumentPath(vendorMaster.getDocumentPath());
        vendorDto.setRcmFlag(vendorMaster.getRcmFlag());
        vendorDto.setVendorOpeningDate(vendorMaster.getVendorOpeningDate());
        vendorDto.setVendorClosingDate(vendorMaster.getVendorClosingDate());
        return vendorDto;
    }

    private VendorMaster vendorDtoToMaster(VendorDto vendorDto) {
        VendorMaster vendorMaster = new VendorMaster();
        vendorMaster.setOrgId(vendorDto.getOrgId());
        vendorMaster.setVendorName(vendorDto.getVendorName());
        vendorMaster.setVendorGroup(vendorDto.getVendorGroup());
        vendorMaster.setVendorType(vendorDto.getVendorType());
        vendorMaster.setAddress1(vendorDto.getAddress1());
        vendorMaster.setAddress2(vendorDto.getAddress2());
        vendorMaster.setAddress3(vendorDto.getAddress3());
        vendorMaster.setPincode(vendorDto.getPincode());
        vendorMaster.setPhone(vendorDto.getPhone());
        vendorMaster.setTelefax(vendorDto.getTelefax());
        vendorMaster.setEmailId(vendorDto.getEmailId());
        vendorMaster.setWebsite(vendorDto.getWebsite());
        vendorMaster.setMobileNumber(vendorDto.getMobileNumber());
        vendorMaster.setBankIfscCode(vendorDto.getBankIfscCode());
        vendorMaster.setBankName(vendorDto.getBankName());
        vendorMaster.setBankMmid(vendorDto.getBankMmid());
        vendorMaster.setBankVpa(vendorDto.getBankVpa());
        vendorMaster.setBankAccount(vendorDto.getBankAccount());
        vendorMaster.setBankBranch(vendorDto.getBankBranch());
        vendorMaster.setTaxpayerNumber(vendorDto.getTaxpayerNumber());
        vendorMaster.setPan(vendorDto.getPan());
        vendorMaster.setSvctaxRegnum(vendorDto.getSvctaxRegnum());
        vendorMaster.setSource(vendorDto.getSource());
        vendorMaster.setGstin(vendorDto.getGstin());
        vendorMaster.setStateId(vendorDto.getStateId());
        vendorMaster.setGstrType(vendorDto.getGstrType());
        vendorMaster.setTypeOfEnterprises(vendorDto.getTypeOfEnterprises());
        vendorMaster.setRegistrationNo(vendorDto.getRegistrationNo());
        vendorMaster.setOneTimeVendor(vendorDto.getOneTimeVendor());
        vendorMaster.setVendorStatus(vendorDto.getVendorStatus());
        vendorMaster.setDocumentPath(vendorDto.getDocumentPath());
        vendorMaster.setRcmFlag(vendorDto.getRcmFlag());
        vendorMaster.setVendorOpeningDate(vendorDto.getVendorOpeningDate());
        vendorMaster.setVendorClosingDate(vendorDto.getVendorClosingDate());
        return vendorMaster;
    }
}