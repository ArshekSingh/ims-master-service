package com.sas.ims.service.serviceImpl;

import com.sas.ims.constant.Constant;
import com.sas.ims.dto.ProductsToAreaMappingDto;
import com.sas.ims.dto.ServerSideDropDownDto;
import com.sas.ims.entity.AreaProductMapping;
import com.sas.ims.entity.AreaProductMappingPK;
import com.sas.ims.exception.BadRequestException;
import com.sas.ims.exception.InternalServerErrorException;
import com.sas.ims.exception.ObjectNotFoundException;
import com.sas.ims.repository.AreaMasterRepository;
import com.sas.ims.repository.AreaProductMappingRepository;
import com.sas.ims.repository.OrganisationHierarchyRepository;
import com.sas.ims.repository.ProductMasterRepository;
import com.sas.ims.service.AreaService;
import com.sas.ims.dto.AreaMasterDto;
import com.sas.ims.entity.AreaMaster;
import com.sas.ims.entity.OrganisationHierarchy;
import com.sas.ims.request.FilterRequest;
import com.sas.ims.utils.DateTimeUtil;
import com.sas.ims.utils.ObjectMapperUtil;
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
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class AreaServiceImpl implements AreaService, Constant {
    private final UserCredentialService userCredentialService;

    private final AreaMasterRepository areaMasterRepository;

    private final OrganisationHierarchyRepository hierarchyRepository;

    private ProductMasterRepository productMasterRepository;

    private AreaProductMappingRepository areaProductMappingRepository;

    @Override
    public Response getAreaDetail(Long branchId) throws ObjectNotFoundException {
        UserSession userSession = userCredentialService.getUserSession();
        AreaMaster areaMaster = areaMasterRepository.findByAreaIdAndOrgId(branchId, userSession.getCompany().getCompanyId()).orElseThrow(() -> new ObjectNotFoundException("Invalid Area Id", HttpStatus.NOT_FOUND));
        AreaMasterDto areaMasterDto = new AreaMasterDto();
        ObjectMapperUtil.map(areaMaster, areaMasterDto);
        areaMasterDto.setCreatedOn(DateTimeUtil.dateTimeToString(areaMaster.getInsertedOn()));
        areaMasterDto.setModifiedOn(DateTimeUtil.dateTimeToString(areaMaster.getUpdatedOn()));
        return new Response(SUCCESS, areaMasterDto, HttpStatus.OK);
    }

    @Override
    public Response getAreaDetailList(FilterRequest filterRequest) throws BadRequestException {
        UserSession userSession = userCredentialService.getUserSession();
        if (!StringUtils.hasText(String.valueOf(filterRequest.getPage()))
                || !StringUtils.hasText(String.valueOf(filterRequest.getSize()))
                || !StringUtils.hasText(filterRequest.getAreaType())) {
            throw new BadRequestException("Invalid Request", HttpStatus.BAD_REQUEST);
        }
        List<AreaMaster> areaMasterList = areaMasterRepository.findAllByAreaTypeAndOrgIdAndStatus(filterRequest.getAreaType(), userSession.getCompany().getCompanyId(), "A", PageRequest.of(filterRequest.getPage(), filterRequest.getSize())).getContent();
        List<AreaMasterDto> areaMasterDtos = new ArrayList<>();
        for (AreaMaster areaMaster : areaMasterList) {
            AreaMasterDto areaMasterDto = new AreaMasterDto();
            ObjectMapperUtil.map(areaMaster, areaMasterDto);
            areaMasterDto.setCreatedOn(DateTimeUtil.dateTimeToString(areaMaster.getInsertedOn()));
            areaMasterDto.setModifiedOn(DateTimeUtil.dateTimeToString(areaMaster.getUpdatedOn()));
            areaMasterDtos.add(areaMasterDto);
        }
        return new Response(SUCCESS, areaMasterDtos, HttpStatus.OK);
    }

    @Override
    public Response addArea(AreaMasterDto areaMasterDto) throws BadRequestException {
        UserSession userSession = userCredentialService.getUserSession();

        Response response = new Response();
        validateRequest(areaMasterDto);

        if (areaMasterRepository.existsByOrgIdAndAreaName(userSession.getCompany().getCompanyId(), areaMasterDto.getAreaName())) {
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("This Area already exists.");
            response.setStatus(HttpStatus.BAD_REQUEST);
        } else {
            Optional<List<OrganisationHierarchy>> organisationHierarchyList = hierarchyRepository.findByOrgIdAndHierarchyTypeOrderByHierarchySequenceAsc(userSession.getCompany().getCompanyId(), "GEO");
            if (organisationHierarchyList.isEmpty()) {
                log.info("No organisation hierarchy found while creating area: {}", areaMasterDto.getAreaName());
                throw new BadRequestException(NOT_FOUND, HttpStatus.BAD_REQUEST);
            }
            Long latestSequence = areaMasterRepository.findLatestSequence();

            AreaMaster area = new AreaMaster();
            ObjectMapperUtil.map(areaMasterDto, area);
            area.setAreaId(latestSequence);
            area.setOrgId(userSession.getCompany().getCompanyId());
            area.setStatus("A");
            area.setAreaCode(areaMasterDto.getAreaType() + "_" + latestSequence);
            area.setInsertedBy(userSession.getSub());
            area.setInsertedOn(LocalDateTime.now());
            area = areaMasterRepository.save(area);
            response.setCode(HttpStatus.OK.value());
            response.setStatus(HttpStatus.OK);
            response.setMessage("Area saved successfully with Area Id :" + " " + area.getAreaId());
        }
        return response;
    }

    private void validateRequest(AreaMasterDto request) throws BadRequestException {
        if (request == null || !StringUtils.hasText(request.getAreaName()) || !StringUtils.hasText(request.getAreaType())) {
            throw new BadRequestException(INVALID_REQUEST, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Response updateArea(AreaMasterDto areaMasterDto) throws ObjectNotFoundException, BadRequestException {
        if (StringUtils.hasText(String.valueOf(areaMasterDto.getAreaId())) || StringUtils.hasText(areaMasterDto.getAreaCode())) {
            throw new BadRequestException("Invalid Request", HttpStatus.BAD_REQUEST);
        }
        UserSession userSession = userCredentialService.getUserSession();
        AreaMaster areaMaster = areaMasterRepository.findByAreaIdAndOrgId(areaMasterDto.getAreaId(), userSession.getCompany().getCompanyId()).orElseThrow(() -> new ObjectNotFoundException("Branch does not exist.", HttpStatus.NOT_FOUND));
        ObjectMapperUtil.map(areaMasterDto, areaMaster);
        areaMasterRepository.save(areaMaster);
        return new Response("Branch updated successfully", HttpStatus.OK);
    }

    @Override
    public Response getAreaTypeMap() throws InternalServerErrorException {
        UserSession userSession = userCredentialService.getUserSession();
        try {
            List<ServerSideDropDownDto> serverSideDropDownDtoList = new ArrayList<>();
            //Fetch hierarchy details by ORG ID
            Optional<List<OrganisationHierarchy>> organisationHierarchies = hierarchyRepository.findByOrgIdAndHierarchyTypeAndStatusOrderByHierarchySequenceAsc(userSession.getCompany().getCompanyId(), "GEO", "A");
            if (organisationHierarchies.isPresent()) {
                organisationHierarchies.get().forEach(organisationHierarchy -> {
                    ServerSideDropDownDto serverSideDropDownDto = new ServerSideDropDownDto();
                    serverSideDropDownDto.setId(organisationHierarchy.getHierarchyCode());
                    serverSideDropDownDto.setLabel(organisationHierarchy.getHierarchyName());
                    serverSideDropDownDtoList.add(serverSideDropDownDto);
                });
                return new Response(SUCCESS, serverSideDropDownDtoList, HttpStatus.OK);
            } else {
                throw new ObjectNotFoundException("No area type found!", HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            log.error("Exception occurred while preparing Area Type Map , message : {}", exception.getMessage(), exception);
            throw new InternalServerErrorException("Exception occurred while preparing Area Type Map", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Response parentAreaCodeMap(String areaType) throws InternalServerErrorException {
        UserSession userSession = userCredentialService.getUserSession();
        try {
            List<AreaMaster> areaMasterList;
            switch (areaType) {
                case "BR": {
                    areaMasterList = areaMasterRepository.findAllByOrgIdAndAreaType(userSession.getCompany().getCompanyId(), "CL");
                    return new Response(SUCCESS, entityToDtoList(areaMasterList), HttpStatus.OK);
                }
                case "CL": {
                    areaMasterList = areaMasterRepository.findAllByOrgIdAndAreaType(userSession.getCompany().getCompanyId(), "DV");
                    return new Response(SUCCESS, entityToDtoList(areaMasterList), HttpStatus.OK);

                }
                case "DV": {
                    areaMasterList = areaMasterRepository.findAllByOrgIdAndAreaType(userSession.getCompany().getCompanyId(), "RG");
                    return new Response(SUCCESS, entityToDtoList(areaMasterList), HttpStatus.OK);

                }
                case "RG": {
                    areaMasterList = areaMasterRepository.findAllByOrgIdAndAreaType(userSession.getCompany().getCompanyId(), "ST");
                    return new Response(SUCCESS, entityToDtoList(areaMasterList), HttpStatus.OK);
                }
                case "ST": {
                    areaMasterList = areaMasterRepository.findAllByOrgIdAndAreaType(userSession.getCompany().getCompanyId(), "ZO");
                    return new Response(SUCCESS, entityToDtoList(areaMasterList), HttpStatus.OK);
                }
                case "ZO": {
                    areaMasterList = areaMasterRepository.findAllByOrgIdAndAreaType(userSession.getCompany().getCompanyId(), "HO");
                    return new Response(SUCCESS, entityToDtoList(areaMasterList), HttpStatus.OK);
                }
                default:
                    throw new IllegalStateException("Unexpected value: " + areaType);
            }
        } catch (Exception ex) {
            log.error("Exception occurred while preparing Parent Area Code Map , message : {}", ex.getMessage(), ex);
            throw new InternalServerErrorException("Exception occurred while preparing Parent Area Code Map", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Response areaCodeMap(String areaType) throws InternalServerErrorException {
        try {
            UserSession userSession = userCredentialService.getUserSession();
            List<ServerSideDropDownDto> serverSideDropDownDtoList = new ArrayList<>();
            List<AreaMaster> areaMasterList = areaMasterRepository.findAllByOrgIdAndAreaType(userSession.getCompany().getCompanyId(), areaType);
            for (AreaMaster areaMaster : areaMasterList) {
                ServerSideDropDownDto serverSideDropDownDto = new ServerSideDropDownDto();
                serverSideDropDownDto.setId(areaMaster.getAreaId().toString());
                serverSideDropDownDto.setLabel(areaMaster.getAreaCode() + "-" + areaMaster.getAreaName());
                serverSideDropDownDtoList.add(serverSideDropDownDto);
            }
            return new Response(SUCCESS, serverSideDropDownDtoList, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Exception occurred while preparing Area Code Map , message : {}", ex.getMessage(), ex);
            throw new InternalServerErrorException("Exception occurred while preparing Area Code Map", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<ServerSideDropDownDto> entityToDtoList(List<AreaMaster> areaMasterList) {
        if (CollectionUtils.isEmpty(areaMasterList))
            return Collections.emptyList();
        return areaMasterList.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    public ServerSideDropDownDto entityToDto(AreaMaster areaMaster) {
        if (areaMaster == null)
            return null;
        ServerSideDropDownDto serverSideDropDownDto = new ServerSideDropDownDto();
        serverSideDropDownDto.setId(areaMaster.getAreaId().toString());
        serverSideDropDownDto.setLabel(areaMaster.getAreaCode() + "-" + areaMaster.getAreaName());
        return serverSideDropDownDto;
    }

    @Override
    public Response getProductsAssignedOrAvailableToArea(Long areaId) {
        Response response = new Response();
        UserSession userSession = userCredentialService.getUserSession();
        List<Object[]> productsAssignedToProducts = areaProductMappingRepository.getProductsByOrgIdAndAreaId(userSession.getCompany().getCompanyId(), areaId);
        ProductsToAreaMappingDto productsToAreaMappingDto = new ProductsToAreaMappingDto();
        List<Integer> productList = new ArrayList<>();
        productsToAreaMappingDto.setAreaId(areaId);
        List<ServerSideDropDownDto> productsAssignedToArea = productsAssignedToProducts.stream().map(branchData -> populateAreaDataForProduct(branchData, productList)).collect(Collectors.toList());
        List<Object[]> availableProductList;
        if (productList.isEmpty()) {
            availableProductList = productMasterRepository.findAllProductDetailByOrgId(userSession.getCompany().getCompanyId());
        } else {
            availableProductList = productMasterRepository.findAllProductsByOrganizationIdNotIn(userSession.getCompany().getCompanyId(), productList);
        }
        List<ServerSideDropDownDto> productsAvailableForArea = availableProductList.stream().map(this::populateAvailableProductData).collect(Collectors.toList());
        productsToAreaMappingDto.setAssignedProducts(productsAssignedToArea);
        productsToAreaMappingDto.setAvailableProducts(productsAvailableForArea);
        response.setCode(HttpStatus.OK.value());
        response.setStatus(HttpStatus.OK);
        response.setData(productsToAreaMappingDto);
        response.setMessage("Transaction completed successfully.");
        return response;
    }

    private ServerSideDropDownDto populateAreaDataForProduct(Object[] areaData, List<Integer> productList) {
        ServerSideDropDownDto areaAssignedProduct = new ServerSideDropDownDto();
        Integer productId = (int) areaData[0];
        areaAssignedProduct.setId(productId.toString());
        areaAssignedProduct.setLabel((String) areaData[1]);
        productList.add(productId);
        return areaAssignedProduct;
    }

    private ServerSideDropDownDto populateAvailableProductData(Object[] product) {
        ServerSideDropDownDto availableProduct = new ServerSideDropDownDto();
        availableProduct.setId(product[0].toString());
        availableProduct.setLabel(product[1].toString());
        return availableProduct;
    }

    @Override
    public Response assignProductsToBranch(ProductsToAreaMappingDto productsToAreaMapping) {
        Response response = new Response();
        UserSession userSession = userCredentialService.getUserSession();
        List<AreaProductMapping> areaProductMappingList = areaProductMappingRepository.findByAreaProductMappingPK_AreaIdAndAreaProductMappingPK_OrgId(productsToAreaMapping.getAreaId(), userSession.getOrgId());
        if (!CollectionUtils.isEmpty(areaProductMappingList)) {
            areaProductMappingRepository.deleteAll(areaProductMappingList);
        }
        productsToAreaMapping.getAssignedProducts().forEach(serverSideDropDownDto -> saveNewAreaProductMapping(serverSideDropDownDto, productsToAreaMapping, userSession));
        response.setCode(HttpStatus.OK.value());
        response.setStatus(HttpStatus.OK);
        response.setMessage("Transaction completed successfully.");
        return response;
    }

    void saveNewAreaProductMapping(ServerSideDropDownDto assignedBranches, ProductsToAreaMappingDto productsToAreaMapping, UserSession userSession) {
        AreaProductMapping areaProductMapping = new AreaProductMapping();
        AreaProductMappingPK areaProductMappingPK = new AreaProductMappingPK();
        areaProductMappingPK.setProductId(Long.parseLong(assignedBranches.getId()));
        areaProductMappingPK.setAreaId(productsToAreaMapping.getAreaId());
        areaProductMappingPK.setOrgId(userSession.getCompany().getCompanyId());
        areaProductMapping.setAreaProductMappingPK(areaProductMappingPK);
        areaProductMapping.setProductGroupId(productMasterRepository.findProductGroupIdByProductId(areaProductMappingPK.getProductId()));
        areaProductMapping.setStatus("Y");
        areaProductMapping.setInsertedOn(LocalDateTime.now());
        areaProductMapping.setInsertedBy(userSession.getSub());
        areaProductMappingRepository.save(areaProductMapping);
    }

}
