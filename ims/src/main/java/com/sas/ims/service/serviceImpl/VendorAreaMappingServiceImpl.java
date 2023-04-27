package com.sas.ims.service.serviceImpl;

import com.sas.ims.dto.AreasToVendorMappingDto;
import com.sas.ims.dto.ServerSideDropDownDto;
import com.sas.ims.entity.VendorAreaMapping;
import com.sas.ims.entity.VendorAreaMappingPK;
import com.sas.ims.repository.AreaMasterRepository;
import com.sas.ims.repository.VendorAreaMappingRepository;
import com.sas.ims.repository.VendorMasterRepository;
import com.sas.ims.service.VendorAreaMappingService;
import com.sas.tokenlib.response.Response;
import com.sas.tokenlib.service.UserCredentialService;
import com.sas.tokenlib.utils.UserSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class VendorAreaMappingServiceImpl implements VendorAreaMappingService {
    private final UserCredentialService userCredentialService;
    private final VendorAreaMappingRepository vendorAreaMappingRepository;
    private final AreaMasterRepository areaMasterRepository;
    private final VendorMasterRepository vendorMasterRepository;

    @Override
    public Response getAreasAssignedOrAvailableToVendor(Long vendorId) {
        Response response = new Response();
        UserSession userSession = userCredentialService.getUserSession();
        List<Object[]> areasByOrgIdAndVendorId = vendorAreaMappingRepository.getAreasByOrgIdAndVendorId(userSession.getCompany().getCompanyId(), vendorId);
        AreasToVendorMappingDto areasToVendorMappingDto = new AreasToVendorMappingDto();
        List<Long> areaList = new ArrayList<>();
        areasToVendorMappingDto.setVendorId(vendorId);
        List<ServerSideDropDownDto> areasAssignedToVendor = areasByOrgIdAndVendorId.stream().map(vendorData -> populateVendorDataForArea(vendorData, areaList)).collect(Collectors.toList());
        List<Object[]> availableAreaList;
        if (areaList.isEmpty()) {
            availableAreaList = areaMasterRepository.findAllAreaDetailByOrgId(userSession.getCompany().getCompanyId());
        } else {
            availableAreaList = areaMasterRepository.findAllAreasByOrganizationIdNotIn(userSession.getCompany().getCompanyId(), areaList);
        }
        List<ServerSideDropDownDto> areasAvailableForVendor = availableAreaList.stream().map(this::populateAvailableAreaData).collect(Collectors.toList());
        areasToVendorMappingDto.setAssignedAreas(areasAssignedToVendor);
        areasToVendorMappingDto.setAvailableAreas(areasAvailableForVendor);
        response.setCode(HttpStatus.OK.value());
        response.setStatus(HttpStatus.OK);
        response.setData(areasToVendorMappingDto);
        response.setMessage("Transaction completed successfully.");
        return response;
    }

    private ServerSideDropDownDto populateVendorDataForArea(Object[] vendorData, List<Long> areaList) {
        ServerSideDropDownDto areaAssignedProduct = new ServerSideDropDownDto();
        BigInteger areaId = (BigInteger) vendorData[0];
        areaAssignedProduct.setId(areaId.toString());
        areaAssignedProduct.setLabel(vendorData[1] + "-" + vendorData[2]);
        areaList.add(areaId.longValue());
        return areaAssignedProduct;
    }

    private ServerSideDropDownDto populateAvailableAreaData(Object[] area) {
        ServerSideDropDownDto availableArea = new ServerSideDropDownDto();
        availableArea.setId(area[0].toString());
        availableArea.setLabel(area[1].toString() + "-" + area[2]);
        return availableArea;
    }

    @Override
    public Response assignAreasToVendor(AreasToVendorMappingDto areasToVendorMappingDto) {
        Response response = new Response();
        UserSession userSession = userCredentialService.getUserSession();
        List<VendorAreaMapping> vendorAreaMappingList = vendorAreaMappingRepository.findByVendorAreaMappingPK_VendorIdAndVendorAreaMappingPK_OrgId(areasToVendorMappingDto.getVendorId(), userSession.getCompany().getCompanyId());
        if (!CollectionUtils.isEmpty(vendorAreaMappingList)) {
            vendorAreaMappingRepository.deleteAll(vendorAreaMappingList);
        }
        areasToVendorMappingDto.getAssignedAreas().forEach(serverSideDropDownDto -> saveNewVendorAreaMapping(serverSideDropDownDto, areasToVendorMappingDto, userSession));
        response.setCode(HttpStatus.OK.value());
        response.setStatus(HttpStatus.OK);
        response.setMessage("Transaction completed successfully.");
        return response;
    }

    @Override
    public Response getVendorsList() {
        Response response = new Response();
        UserSession userSession = userCredentialService.getUserSession();
        List<Object[]> vendorsList = vendorMasterRepository.findAllByOrgId(userSession.getCompany().getCompanyId());
        List<ServerSideDropDownDto> vendors = vendorsList.stream().map(this::populateVendorMasterData).collect(Collectors.toList());
        response.setCode(HttpStatus.OK.value());
        response.setData(vendors);
        response.setStatus(HttpStatus.OK);
        response.setMessage("Transaction completed successfully.");
        return response;
    }

    private ServerSideDropDownDto populateVendorMasterData(Object[] vendors) {
        ServerSideDropDownDto availableArea = new ServerSideDropDownDto();
        availableArea.setId(vendors[0].toString());
        availableArea.setLabel(vendors[1].toString() + "-" + vendors[2]);
        return availableArea;
    }

    void saveNewVendorAreaMapping(ServerSideDropDownDto assignedAreas, AreasToVendorMappingDto areasToVendorMappingDto, UserSession userSession) {
        VendorAreaMapping vendorAreaMapping = new VendorAreaMapping();
        VendorAreaMappingPK vendorAreaMappingPK = new VendorAreaMappingPK();
        vendorAreaMappingPK.setVendorId(areasToVendorMappingDto.getVendorId());
        vendorAreaMappingPK.setAreaId(Long.parseLong(assignedAreas.getId()));
        vendorAreaMappingPK.setOrgId(userSession.getCompany().getCompanyId());
        vendorAreaMapping.setVendorAreaMappingPK(vendorAreaMappingPK);
        vendorAreaMapping.setStatus("Y");
        vendorAreaMapping.setInsertedOn(LocalDateTime.now());
        vendorAreaMapping.setInsertedBy(userSession.getSub());
        vendorAreaMappingRepository.save(vendorAreaMapping);
    }
}
