package com.sas.ims.service.serviceImpl;

import com.sas.ims.constant.Constant;
import com.sas.ims.dao.DistrictDao;
import com.sas.ims.dto.DistrictMasterDto;
import com.sas.ims.entity.DistrictMaster;
import com.sas.ims.entity.StateMaster;
import com.sas.ims.exception.BadRequestException;
import com.sas.ims.exception.ObjectNotFoundException;
import com.sas.ims.repository.DistrictRepository;
import com.sas.ims.request.FilterRequest;
import com.sas.ims.service.DistrictService;
import com.sas.ims.utils.DateTimeUtil;
import com.sas.tokenlib.response.Response;
import com.sas.tokenlib.service.UserCredentialService;
import com.sas.tokenlib.utils.UserSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DistrictServiceImpl implements DistrictService, Constant {

    private final DistrictRepository districtRepository;

    private final UserCredentialService userCredentialService;

    private final DistrictDao districtDao;

    @Override
    public Response getDistrictDetails(Integer districtId) throws ObjectNotFoundException {
        DistrictMaster districtMaster = districtRepository.findByDistrictId(districtId).orElseThrow(() -> new ObjectNotFoundException("Invalid districtId.", HttpStatus.NOT_FOUND));
        DistrictMasterDto districtMasterDto = new DistrictMasterDto();
        BeanUtils.copyProperties(districtMaster, districtMasterDto);
        StateMaster stateMaster = districtMaster.getStateMaster();
        districtMasterDto.setStateName(stateMaster.getStateName());
        return new Response(SUCCESS, districtMasterDto, HttpStatus.OK);
    }

    @Override
    public Response getDistrictDetailList(FilterRequest filterRequest) throws BadRequestException {
        Long count = null;
        if (filterRequest.getStart() == 0) {
            count = districtDao.findAllDistrictMasterCount(filterRequest);
        }
        if ("Y".equalsIgnoreCase(filterRequest.getIsCsv())) {
            filterRequest.setLimit(districtDao.findAllDistrictMasterCount(filterRequest).intValue());
        }
        List<DistrictMaster> districtMasterList = districtDao.findAllDistrictMaster(filterRequest);
        List<DistrictMasterDto> districtMasterDtoList = new ArrayList<>();
        for (DistrictMaster districtMaster : districtMasterList) {
            DistrictMasterDto districtMasterDto = new DistrictMasterDto();
            BeanUtils.copyProperties(districtMaster, districtMasterDto);
            districtMasterDto.setInsertedOn(DateTimeUtil.dateTimeToString(districtMaster.getInsertedOn()));
            districtMasterDto.setUpdatedOn(DateTimeUtil.dateTimeToString(districtMaster.getUpdatedOn()));
            districtMasterDto.setStateCode(districtMaster.getStateMaster().getStateCode());
            districtMasterDto.setStateName(districtMaster.getStateMaster().getStateName());
            districtMasterDto.setStateId(districtMaster.getStateMaster().getStateId());
            districtMasterDtoList.add(districtMasterDto);
        }
        return new Response(SUCCESS, districtMasterDtoList, HttpStatus.OK);
    }

    @Override
    public Response addDistrict(DistrictMasterDto districtMasterDto) {
        Response response = new Response();
        UserSession userSession = userCredentialService.getUserSession();
        Optional<DistrictMaster> districtMaster = districtRepository.findByDistrictId(districtMasterDto.getDistrictId());
        if (districtMaster.isPresent()) {
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("This District already exists.");
            response.setStatus(HttpStatus.BAD_REQUEST);
        } else {
            DistrictMaster district = new DistrictMaster();
            BeanUtils.copyProperties(districtMasterDto, district);
            district.setInsertedBy(userSession.getSub());
            district.setInsertedOn(LocalDateTime.now());
            district.setActive("Y");
            districtRepository.save(district);
            response.setCode(HttpStatus.OK.value());
            response.setData(district);
            response.setStatus(HttpStatus.OK);
            response.setMessage("District saved successfully with District Id :" + " " + district.getDistrictId());
        }
        return response;
    }

    @Override
    public Response updateDistrict(DistrictMasterDto districtMasterDto) throws ObjectNotFoundException {
        UserSession userSession = userCredentialService.getUserSession();
        DistrictMaster districtMaster = districtRepository.findByDistrictId(districtMasterDto.getDistrictId()).orElseThrow(() -> new ObjectNotFoundException("District does not exist.", HttpStatus.NOT_FOUND));
        if (districtMaster.getDistrictId().equals(districtMasterDto.getDistrictId())) {
            districtMasterDto.mapDtoToEntityForDistrictUpdate(districtMasterDto, districtMaster, userSession);
            districtRepository.save(districtMaster);
        }
        return new Response(SUCCESS, HttpStatus.OK);
    }
}