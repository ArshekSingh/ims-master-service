package com.sas.ims.service.serviceImpl;

import com.sas.ims.constant.Constant;
import com.sas.ims.dto.ProductsToBranchMappingDto;
import com.sas.ims.dto.ServerSideDropDownDto;
import com.sas.ims.entity.BranchProductMapping;
import com.sas.ims.exception.BadRequestException;
import com.sas.ims.exception.ObjectNotFoundException;
import com.sas.ims.repository.BranchMasterRepository;
import com.sas.ims.repository.BranchProductMappingRepository;
import com.sas.ims.repository.OrganisationHierarchyRepository;
import com.sas.ims.repository.ProductMasterRepository;
import com.sas.ims.response.Response;
import com.sas.ims.service.BranchService;
import com.sas.ims.dto.BranchMasterDto;
import com.sas.ims.entity.BranchMaster;
import com.sas.ims.entity.OrganisationHierarchy;
import com.sas.ims.request.FilterRequest;
import com.sas.ims.utils.DateTimeUtil;
import com.sas.ims.utils.ObjectMapperUtil;
import com.sas.tokenlib.service.UserCredentialService;
import com.sas.tokenlib.utils.UserSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class BranchServiceImpl implements BranchService, Constant {
    private UserCredentialService userCredentialService;

    private BranchMasterRepository branchMasterRepository;

    private OrganisationHierarchyRepository hierarchyRepository;

    private ProductMasterRepository productMasterRepository;

    private BranchProductMappingRepository branchProductMappingRepository;

    @Override
    public Response getBranchDetail(Long branchId) throws ObjectNotFoundException {
        UserSession userSession = userCredentialService.getUserSession();
        BranchMaster branchMaster = branchMasterRepository.findByBranchIdAndOrgId(branchId, userSession.getCompany().getCompanyId()).orElseThrow(() -> new ObjectNotFoundException("Invalid Branch Id.", HttpStatus.NOT_FOUND));
        BranchMasterDto branchMasterDto = new BranchMasterDto();
        ObjectMapperUtil.map(branchMaster, branchMasterDto);
        branchMasterDto.setCreatedOn(DateTimeUtil.dateTimeToString(branchMaster.getInsertedOn()));
        branchMasterDto.setModifiedOn(DateTimeUtil.dateTimeToString(branchMaster.getUpdatedOn()));
        return new Response(SUCCESS, branchMasterDto, HttpStatus.OK);
    }

    @Override
    public Response getBranchDetailList(FilterRequest filterRequest) throws BadRequestException {
        Pageable pageable = PageRequest.of(filterRequest.getStart(), filterRequest.getLimit(), Sort.by("branchId").descending());
        List<BranchMaster> branchMasterList = filteredData(specification(filterRequest), pageable);
        List<BranchMasterDto> branchMasterDtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(branchMasterList)) {
            for (BranchMaster branchMaster : branchMasterList) {
                BranchMasterDto branchMasterDto = new BranchMasterDto();
                ObjectMapperUtil.map(branchMaster, branchMasterDto);
                branchMasterDto.setCreatedOn(DateTimeUtil.dateTimeToString(branchMaster.getInsertedOn()));
                branchMasterDto.setModifiedOn(DateTimeUtil.dateTimeToString(branchMaster.getUpdatedOn()));
                branchMasterDtos.add(branchMasterDto);
            }
        } else {
            throw new BadRequestException(NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        return new Response(SUCCESS, branchMasterDtos, HttpStatus.OK);

    }

    public List<BranchMaster> filteredData(Specification<BranchMaster> spec, Pageable pageable) {
        Page<BranchMaster> pageData = branchMasterRepository.findAll(spec, pageable);
        List<BranchMaster> filteredData = pageData.getContent();
        return filteredData;
    }

    public static Specification<BranchMaster> specification(FilterRequest filterRequest) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = null;
            if (filterRequest.getBranchId() != null) {
                predicate = criteriaBuilder.equal(root.get("branchId"), filterRequest.getBranchId());
            }
            return predicate;
        };
    }

    @Override
    public Response addBranch(BranchMasterDto branchMasterDto) throws ObjectNotFoundException, BadRequestException {
        Response response = new Response();
        validateRequest(branchMasterDto);
        Long parentId = null;
        List<OrganisationHierarchy> organisationHierarchyList = hierarchyRepository.findByOrgIdAndHierarchyTypeOrderByHierarchySequenceAsc(Long.valueOf("1"), "GEO").orElseThrow(() -> new ObjectNotFoundException("Invalid Type.", HttpStatus.NOT_FOUND));      // get parent id using organisation hierarchy
        if (organisationHierarchyList == null) {
            throw new BadRequestException(NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        if (branchMasterRepository.existsByBranchCode(branchMasterDto.getBranchCode())) {
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("This Branch already exists.");
            response.setStatus(HttpStatus.BAD_REQUEST);
        } else {
            BranchMaster branch = new BranchMaster();
            ObjectMapperUtil.map(branchMasterDto, branch);
            branch.setInsertedBy("");
            branchMasterDto.setParentId(getParentId(branchMasterDto, parentId, organisationHierarchyList));
            branch.setInsertedOn(LocalDateTime.now());
            branch = branchMasterRepository.save(branch);
            response.setCode(HttpStatus.OK.value());
            response.setStatus(HttpStatus.OK);
            response.setMessage("Branch saved successfully with Branch Id :" + " " + branch.getBranchId());
        }
        return response;
    }

    private static Long getParentId(BranchMasterDto branchMasterDto, Long parentId, List<OrganisationHierarchy> organisationHierarchyList) {
        Map<String, Long> organisationHierarchyMap = organisationHierarchyList.stream().collect(Collectors.toMap(OrganisationHierarchy::getHierarchyName, c -> c.getHierarchySequence()));
        if (organisationHierarchyMap.get(branchMasterDto.getBranchName()) != null) {
            Long value = organisationHierarchyMap.get(branchMasterDto.getBranchName());
            for (Map.Entry<String, Long> entry : organisationHierarchyMap.entrySet()) {
                if (entry.getValue().equals(value + 1)) {
                    parentId = entry.getValue();
                    break;
                }
            }
        }
        return parentId;

    }

    private void validateRequest(BranchMasterDto request) throws BadRequestException {
        if (request == null || StringUtils.hasText(request.getBranchName())) {
            throw new BadRequestException(INVALID_REQUEST, HttpStatus.BAD_REQUEST);
        }
    }


    @Override
    public Response updateBranch(BranchMasterDto branchMasterDto) throws ObjectNotFoundException {
        UserSession userSession = userCredentialService.getUserSession();
        BranchMaster branchMaster = branchMasterRepository.findByBranchIdAndOrgId(branchMasterDto.getBranchId(), userSession.getCompany().getCompanyId()).orElseThrow(() -> new ObjectNotFoundException("Branch does not exist.", HttpStatus.NOT_FOUND));
//        branchMasterDto.mapDtoToEntityForBranchUpdate(branchMasterDto, branchMaster, userSession);
        branchMasterRepository.save(branchMaster);
        return new Response("Branch updated successfully", HttpStatus.OK);
    }

    @Override
    public Response getProductsAssignedOrAvailableToBranch(Long branchId) {
        Response response = new Response();
        UserSession userSession = userCredentialService.getUserSession();
        List<Object[]> productsAssignedToProducts = branchProductMappingRepository.getProductsByOrgIdAndBranchId(userSession.getCompany().getCompanyId(), branchId);
        ProductsToBranchMappingDto productsToBranchMappingDto = new ProductsToBranchMappingDto();
        List<Integer> productList = new ArrayList<>();
        productsToBranchMappingDto.setBranchId(branchId.intValue());
        List<ServerSideDropDownDto> productsAssignedToBranch = productsAssignedToProducts.stream().map(branchData -> populateBranchDataForProduct(branchData, productList)).collect(Collectors.toList());
        List<Object[]> availableProductList;
        if (productList.isEmpty()) {
            availableProductList = productMasterRepository.findAllProductDetailByOrgId(userSession.getCompany().getCompanyId());
        } else {
            availableProductList = productMasterRepository.findAllProductsByOrganizationIdNotIn(userSession.getCompany().getCompanyId(), productList);
        }
        List<ServerSideDropDownDto> productsAvailableForBranch = availableProductList.stream().map(this::populateAvailableProductData).collect(Collectors.toList());
        productsToBranchMappingDto.setAssignedProducts(productsAssignedToBranch);
        productsToBranchMappingDto.setAvailableProducts(productsAvailableForBranch);
        response.setCode(HttpStatus.OK.value());
        response.setStatus(HttpStatus.OK);
        response.setData(productsToBranchMappingDto);
        response.setMessage("Transaction completed successfully.");
        return response;
    }

    private ServerSideDropDownDto populateBranchDataForProduct(Object[] branchData, List<Integer> productList) {
        ServerSideDropDownDto branchAssignedProduct = new ServerSideDropDownDto();
        Integer productId = (int) branchData[0];
        branchAssignedProduct.setId(productId.toString());
        branchAssignedProduct.setLabel((String) branchData[1]);
        productList.add(productId);
        return branchAssignedProduct;
    }

    private ServerSideDropDownDto populateAvailableProductData(Object[] product) {
        ServerSideDropDownDto availableProduct = new ServerSideDropDownDto();
        availableProduct.setId(product[0].toString());
        availableProduct.setLabel(product[1].toString());

        return availableProduct;
    }
}
