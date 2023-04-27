package com.sas.ims.service.serviceImpl;

import com.sas.ims.dto.ProductsToVendorMappingDto;
import com.sas.ims.dto.ServerSideDropDownDto;
import com.sas.ims.dto.VendorDto;
import com.sas.ims.entity.*;
import com.sas.ims.enums.Vendor;
import com.sas.ims.exception.BadRequestException;
import com.sas.ims.repository.*;
import com.sas.ims.service.VendorService;
import com.sas.ims.utils.DateTimeUtil;
import com.sas.tokenlib.response.Response;
import com.sas.tokenlib.service.UserCredentialService;
import com.sas.tokenlib.utils.UserSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class VendorServiceImpl implements VendorService {
    private final UserCredentialService userCredentialService;
    private final VendorMasterRepository vendorMasterRepository;
    private final OrganisationHierarchyRepository organisationHierarchyRepository;
    private final ApprovalDetailRepository approvalDetailRepository;
    private final ProductMasterRepository productMasterRepository;
    private final VendorProductMappingRepository vendorProductMappingRepository;

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
        if (page == null || size == null) {
            return new Response("Invalid Request", HttpStatus.BAD_REQUEST);
        }
        UserSession userSession = userCredentialService.getUserSession();
        List<VendorMaster> vendors = vendorMasterRepository.findByOrgId(userSession.getCompany().getCompanyId(), PageRequest.of(page, size)).getContent();
        List<VendorDto> vendorDtoList = vendors.stream().map(this::vendorMasterToDto).collect(Collectors.toList());
        return new Response("Transaction Successful", vendorDtoList, HttpStatus.OK);
    }

    @Override
    public Response saveVendor(VendorDto vendorDto) {
        if (!StringUtils.hasText(vendorDto.getVendorName()) ||
                !StringUtils.hasText(vendorDto.getVendorCode()) ||
                !StringUtils.hasText(vendorDto.getAddress1()) ||
                !StringUtils.hasText(String.valueOf(vendorDto.getPincode())) ||
                !StringUtils.hasText(vendorDto.getPhone()) ||
                !StringUtils.hasText(vendorDto.getStateId())) {
            return new Response("Invalid Request!", HttpStatus.BAD_REQUEST);
        }
        UserSession userSession = userCredentialService.getUserSession();

        //Create vendor master from request
        VendorMaster vendorMaster = vendorDtoToMaster(vendorDto, Vendor.PENDING.getKey(), userSession, false);

//        ApprovalMatrix approvalMatrix =

        //Fetch approval matrix from Organisation Hierarchy
//        List<OrganisationHierarchy> organisationHierarchyList = organisationHierarchyRepository.findByOrgIdAndHierarchyCodeAndHierarchyTypeAndStatus(userSession.getCompany().getCompanyId(), "VENDOR", "GEO", true);
//        if (!organisationHierarchyList.isEmpty()) {
//            organisationHierarchyList.forEach(organisationHierarchy -> {
//                ApprovalDetail approvalDetail = new ApprovalDetail();
//                approvalDetail.setOrgId(userSession.getCompany().getCompanyId());
//                approvalDetail.setEntityId(organisationHierarchy.getId());
//                approvalDetail.setEntityType(organisationHierarchy.getHierarchyType());
//                approvalDetail.setApprovalType("VENDOR");
//                approvalDetail.setApprovalStatus("I");
//                approvalDetailRepository.save(approvalDetail);
//            });
//        }
        vendorMasterRepository.save(vendorMaster);
        return new Response("Transaction Successful", HttpStatus.OK);
    }

    @Override
    public Response updateVendor(VendorDto vendorDto) throws BadRequestException {
        UserSession userSession = userCredentialService.getUserSession();
        if (!StringUtils.hasText(vendorDto.getVendorName()) ||
                !StringUtils.hasText(vendorDto.getVendorCode()) ||
                !StringUtils.hasText(vendorDto.getAddress1()) ||
                !StringUtils.hasText(String.valueOf(vendorDto.getPincode())) ||
                !StringUtils.hasText(vendorDto.getStateId())) {
            return new Response("Invalid Request!", HttpStatus.BAD_REQUEST);
        }
        try {
            Optional<VendorMaster> vendorMaster = vendorMasterRepository.findById(vendorDto.getVendorId());
            if (vendorMaster.isPresent()) {
                VendorMaster master = vendorDtoToMaster(vendorDto, vendorDto.getVendorStatus(), userSession, true);
                vendorMasterRepository.save(master);
                return new Response("Transaction Completed!", HttpStatus.OK);
            } else {
                throw new BadRequestException("No vendor found !", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            log.info("Exception occurred while updating vendor for id: {}", vendorDto.getVendorId());
            throw new BadRequestException("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Response removeVendor(Long vendorId) {
        UserSession userSession = userCredentialService.getUserSession();
        Optional<VendorMaster> vendorMaster = vendorMasterRepository.findById(vendorId);
        if (vendorMaster.isPresent()) {
            vendorMasterRepository.updateVendorDetails(userSession.getCompany().getCompanyId(), vendorId);
            return new Response("Vendor Status has been Updated SUccessfully", HttpStatus.OK);
        } else {
            return new Response("Vendor Doesn't Exists", HttpStatus.NOT_FOUND);
        }
    }

    private VendorDto vendorMasterToDto(VendorMaster vendorMaster) {
        VendorDto vendorDto = new VendorDto();
        vendorDto.setOrgId(vendorMaster.getOrgId());
        vendorDto.setVendorCode(vendorMaster.getVendorCode());
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
        vendorDto.setVendorOpeningDate(DateTimeUtil.dateTimeToString(vendorMaster.getVendorOpeningDate(), DateTimeUtil.DDMMYYYY));
        vendorDto.setVendorClosingDate(DateTimeUtil.dateTimeToString(vendorMaster.getVendorClosingDate(), DateTimeUtil.DDMMYYYY));
        return vendorDto;
    }

    private VendorMaster vendorDtoToMaster(VendorDto vendorDto, String vendorStatus, UserSession userSession, Boolean update) {
        VendorMaster vendorMaster = new VendorMaster();
        if (update) vendorMaster.setVendorId(vendorDto.getVendorId());
        vendorMaster.setOrgId(userSession.getCompany().getCompanyId());
        vendorMaster.setVendorName(vendorDto.getVendorName());
        vendorMaster.setVendorCode(vendorDto.getVendorCode());
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
        vendorMaster.setVendorStatus(vendorStatus);
        vendorMaster.setDocumentPath(vendorDto.getDocumentPath());
        vendorMaster.setRcmFlag(vendorDto.getRcmFlag());
        vendorMaster.setVendorOpeningDate(DateTimeUtil.stringTimeToDateTime(vendorDto.getVendorOpeningDate(), DateTimeUtil.DDMMYYYY));
        vendorMaster.setVendorClosingDate(DateTimeUtil.stringTimeToDateTime(vendorDto.getVendorClosingDate(), DateTimeUtil.DDMMYYYY));
        vendorMaster.setInsertedOn(LocalDateTime.now());
        vendorMaster.setInsertedBy(userSession.getSub());
        return vendorMaster;
    }


    @Override
    public Response getProductsAssignedOrAvailableToVendor(Long vendorId) {
        Response response = new Response();
        UserSession userSession = userCredentialService.getUserSession();
        List<Object[]> productsAssignedToProducts = vendorProductMappingRepository.getProductsByOrgIdAndVendorId(userSession.getCompany().getCompanyId(), vendorId);
        ProductsToVendorMappingDto productsToVendorMappingDto = new ProductsToVendorMappingDto();
        List<Long> productList = new ArrayList<>();
        productsToVendorMappingDto.setVendorId(vendorId);
        List<ServerSideDropDownDto> productsAssignedToVendor = productsAssignedToProducts.stream().map(branchData -> populateVendorDataForProduct(branchData, productList)).collect(Collectors.toList());
        List<Object[]> availableProductList;
        if (productList.isEmpty()) {
            availableProductList = productMasterRepository.findAllProductDetailByOrgId(userSession.getCompany().getCompanyId());
        } else {
            availableProductList = productMasterRepository.findAllProductsByOrganizationIdNotIn(userSession.getCompany().getCompanyId(), productList);
        }
        List<ServerSideDropDownDto> productsAvailableForVendor = availableProductList.stream().map(this::populateAvailableProductData).collect(Collectors.toList());
        productsToVendorMappingDto.setAssignedProducts(productsAssignedToVendor);
        productsToVendorMappingDto.setAvailableProducts(productsAvailableForVendor);
        response.setCode(HttpStatus.OK.value());
        response.setStatus(HttpStatus.OK);
        response.setData(productsToVendorMappingDto);
        response.setMessage("Transaction completed successfully.");
        return response;
    }

    private ServerSideDropDownDto populateVendorDataForProduct(Object[] vendorData, List<Long> productList) {
        ServerSideDropDownDto vendorAssignedProduct = new ServerSideDropDownDto();
        Integer productId = (Integer) vendorData[0];
        vendorAssignedProduct.setId(productId.toString());
        vendorAssignedProduct.setLabel((String) vendorData[1]);
        productList.add(productId.longValue());
        return vendorAssignedProduct;
    }

    private ServerSideDropDownDto populateAvailableProductData(Object[] product) {
        ServerSideDropDownDto availableProduct = new ServerSideDropDownDto();
        availableProduct.setId(product[0].toString());
        availableProduct.setLabel(product[1].toString());
        return availableProduct;
    }

    @Override
    public Response assignProductsToVendor(ProductsToVendorMappingDto productsToVendorMappingDto) {
        Response response = new Response();
        UserSession userSession = userCredentialService.getUserSession();
        List<VendorProductMapping> vendorProductMappingList = vendorProductMappingRepository.findByVendorProductMappingPK_VendorIdAndVendorProductMappingPK_OrgId(productsToVendorMappingDto.getVendorId(), userSession.getCompany().getCompanyId());
        if (!CollectionUtils.isEmpty(vendorProductMappingList)) {
            vendorProductMappingRepository.deleteAll(vendorProductMappingList);
        }
        productsToVendorMappingDto.getAssignedProducts().forEach(serverSideDropDownDto -> saveNewAreaProductMapping(serverSideDropDownDto, productsToVendorMappingDto, userSession));
        response.setCode(HttpStatus.OK.value());
        response.setStatus(HttpStatus.OK);
        response.setMessage("Transaction completed successfully.");
        return response;
    }

    void saveNewAreaProductMapping(ServerSideDropDownDto assignedVendors, ProductsToVendorMappingDto productsToVendorMappingDto, UserSession userSession) {
        VendorProductMapping vendorProductMapping = new VendorProductMapping();
        VendorProductMappingPK vendorProductMappingPK = new VendorProductMappingPK();
        vendorProductMappingPK.setProductId(Long.parseLong(assignedVendors.getId()));
        vendorProductMappingPK.setVendorId(productsToVendorMappingDto.getVendorId());
        vendorProductMappingPK.setOrgId(userSession.getCompany().getCompanyId());
        vendorProductMapping.setVendorProductMappingPK(vendorProductMappingPK);
        vendorProductMapping.setProductGroupId(productMasterRepository.findProductGroupIdByProductId(vendorProductMappingPK.getProductId()));
        vendorProductMapping.setStatus("Y");
        vendorProductMapping.setInsertedOn(LocalDateTime.now());
        vendorProductMapping.setInsertedBy(userSession.getSub());
        vendorProductMappingRepository.save(vendorProductMapping);
    }
}