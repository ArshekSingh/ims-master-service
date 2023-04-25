package com.sas.ims.service.serviceImpl;

import com.sas.ims.constant.Constant;
import com.sas.ims.dto.IfscMasterDto;
import com.sas.ims.entity.IfscMaster;
import com.sas.ims.entity.StateMaster;
import com.sas.ims.exception.BadRequestException;
import com.sas.ims.exception.ObjectNotFoundException;
import com.sas.ims.repository.IfscDao;
import com.sas.ims.repository.IfscMasterRepository;
import com.sas.ims.request.FilterRequest;
import com.sas.ims.service.IfscService;
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
public class IfscServiceImpl implements IfscService, Constant {

    private final IfscMasterRepository ifscMasterRepository;
    private final UserCredentialService userCredentialService;
    private final IfscDao ifscDao;

    @Override
    public Response getIfscDetails(String ifscCode) throws ObjectNotFoundException {
        IfscMaster ifscMaster = ifscMasterRepository.findByIfscCodeContainingIgnoreCase(ifscCode).orElseThrow(() -> new ObjectNotFoundException("Invalid IFSC Code.", HttpStatus.NOT_FOUND));
        IfscMasterDto ifscMasterDto = new IfscMasterDto();
        BeanUtils.copyProperties(ifscMaster, ifscMasterDto);
        ifscMasterDto.setInsertedOn(DateTimeUtil.dateTimeToString(ifscMaster.getInsertedOn()));
        ifscMasterDto.setUpdatedOn(DateTimeUtil.dateTimeToString(ifscMaster.getUpdatedOn()));
        if (ifscMaster.getStateMaster() != null) {
            ifscMasterDto.setStateName(ifscMaster.getStateMaster().getStateName());
        }
        return new Response(SUCCESS, ifscMasterDto, HttpStatus.OK);
    }

    @Override
    public Response getIfscCodeList(String ifscCode) {
        ifscCode = ifscCode.toUpperCase() + "%";
        List<IfscMaster> ifscMasterList = ifscMasterRepository.findAllByIfscMaster(ifscCode);
        List<IfscMasterDto> ifscMasterDtoList = new ArrayList<>();
        for (IfscMaster ifscMaster : ifscMasterList) {
            IfscMasterDto ifscMasterDto = new IfscMasterDto();
            BeanUtils.copyProperties(ifscMaster, ifscMasterDto);
            if (ifscMaster.getStateMaster() != null) {
                ifscMasterDto.setStateName(ifscMaster.getStateMaster().getStateName());
            }
            ifscMasterDtoList.add(ifscMasterDto);
        }
        return new Response(SUCCESS, ifscMasterDtoList, HttpStatus.OK);
    }

    @Override
    public Response addIfscCode(IfscMasterDto ifscMasterDto) {
        Response response = new Response();
        UserSession userSession = userCredentialService.getUserSession();
        Optional<IfscMaster> ifscMaster = ifscMasterRepository.findByIfscCodeContainingIgnoreCase(ifscMasterDto.getIfscCode());
        if (ifscMaster.isPresent()) {
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("This IFSC code already exists.");
            response.setStatus(HttpStatus.BAD_REQUEST);
        } else {
            IfscMaster ifsc = new IfscMaster();
            ifsc.setStatus("A");
            ifsc.setCountryId(1);
            ifsc.setPhoneNo(ifscMasterDto.getPhoneNo());
            ifsc.setState(ifscMasterDto.getState());
            ifsc.setIfscCode(ifscMasterDto.getIfscCode());
            ifsc.setCity(ifscMasterDto.getCity());
            ifsc.setBranchAddress(ifscMasterDto.getBranchAddress());
            ifsc.setBankName(ifscMasterDto.getBankName());
            ifsc.setBankBranch(ifscMasterDto.getBankBranch());
            ifsc.setInsertedBy(userSession.getSub());
            ifsc.setInsertedOn(LocalDateTime.now());
            ifscMasterRepository.save(ifsc);
            response.setCode(HttpStatus.OK.value());
            response.setStatus(HttpStatus.OK);
            response.setMessage("Transaction saved successfully with IFSC code : " + ifsc.getIfscCode());
        }
        return response;
    }

    @Override
    public Response updateIfscCode(IfscMasterDto ifscMasterDto) throws ObjectNotFoundException {
        UserSession userSession = userCredentialService.getUserSession();
        IfscMaster ifscMaster = ifscMasterRepository.findByIfscCodeContainingIgnoreCase(ifscMasterDto.getIfscCode()).orElseThrow(() -> new ObjectNotFoundException("IFSC Code does not exist.", HttpStatus.NOT_FOUND));
        if (ifscMaster.getIfscCode().equals(ifscMasterDto.getIfscCode())) {
            ifscMasterDto.mapDtoToEntityForIfscUpdate(ifscMasterDto, ifscMaster, userSession);
            ifscMasterRepository.save(ifscMaster);
        }
        return new Response("IFSC updated successfully", HttpStatus.OK);
    }

    @Override
    public Response getIfscCodeDetails(String ifscCode) throws ObjectNotFoundException {
        IfscMaster ifscMaster = ifscMasterRepository.findByIfscCodeContainingIgnoreCase(ifscCode).orElseThrow(() -> new ObjectNotFoundException("Invalid IFSC Code.", HttpStatus.NOT_FOUND));
        IfscMasterDto ifscMasterDto = new IfscMasterDto();
        BeanUtils.copyProperties(ifscMaster, ifscMasterDto);
        return new Response(SUCCESS, ifscMasterDto, HttpStatus.OK);
    }

    @Override
    public Response getIfscCodeDetailsList(FilterRequest filterRequest) throws BadRequestException {
        Long count = null;
        if (filterRequest.getStart() == 0) {
            count = ifscDao.findAllIfscCodeCount(filterRequest);
        }
        if ("Y".equalsIgnoreCase(filterRequest.getIsCsv())) {
            filterRequest.setLimit(ifscDao.findAllIfscCodeCount(filterRequest).intValue());
        }
        List<IfscMaster> ifscMasterList = ifscDao.findAllIfscCodeMaster(filterRequest);
        List<IfscMasterDto> ifscMasterDtoList = new ArrayList<>();
        for (IfscMaster ifscMaster : ifscMasterList) {
            IfscMasterDto ifscMasterDto = new IfscMasterDto();
            BeanUtils.copyProperties(ifscMaster, ifscMasterDto);
            StateMaster stateMaster = ifscMaster.getStateMaster();
            if (stateMaster != null) {
                ifscMasterDto.setStateName(stateMaster.getStateName() != null ? stateMaster.getStateName() : "");
            }
            ifscMasterDtoList.add(ifscMasterDto);
        }
        return new Response(SUCCESS, ifscMasterDtoList, HttpStatus.OK);
    }
}