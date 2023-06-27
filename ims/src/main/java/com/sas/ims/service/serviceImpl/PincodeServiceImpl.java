package com.sas.ims.service.serviceImpl;

import com.sas.ims.constant.Constant;
import com.sas.ims.dao.PincodeDao;
import com.sas.ims.dto.DistrictMasterDto;
import com.sas.ims.dto.PincodeMasterDto;
import com.sas.ims.dto.StateMasterDto;
import com.sas.ims.entity.DistrictMaster;
import com.sas.ims.entity.PincodeMaster;
import com.sas.ims.entity.PincodeMasterPK;
import com.sas.ims.entity.StateMaster;
import com.sas.ims.exception.BadRequestException;
import com.sas.ims.exception.ObjectNotFoundException;
import com.sas.ims.repository.PincodeRepository;
import com.sas.ims.request.FilterRequest;
import com.sas.ims.service.PincodeService;
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
public class PincodeServiceImpl implements PincodeService, Constant {

    private final PincodeRepository pincodeRepository;

    private final UserCredentialService userCredentialService;

    private final PincodeDao pincodeDao;

    @Override
    public Response getPincodeList(Integer pincode) {
        List<PincodeMaster> pincodeMasterList = pincodeRepository.findAllByPincodeMaster(String.valueOf(pincode));
        List<PincodeMasterDto> pincodeMasterDtoList = new ArrayList<>();
        for (PincodeMaster pincodeMaster : pincodeMasterList) {
            //Pincode List with state, district and village
            PincodeMasterDto pincodeMasterDto = new PincodeMasterDto();
            pincodeMasterDto.setPincode(pincodeMaster.getPincodeMasterPK().getPincode());

            StateMaster stateMaster = pincodeMaster.getStateMaster();
            StateMasterDto stateMasterDto = new StateMasterDto();
            stateMasterDto.setStateId(stateMaster.getStateId());
            stateMasterDto.setStateCode(stateMaster.getStateCode());
            stateMasterDto.setStateName(stateMaster.getStateName());
            pincodeMasterDto.setState(stateMasterDto);

            DistrictMaster districtMaster = pincodeMaster.getDistrictMaster();
            DistrictMasterDto districtMasterDto = new DistrictMasterDto();
            districtMasterDto.setDistrictId(districtMaster.getDistrictId());
            districtMasterDto.setDistrictCode(districtMaster.getDistrictCode());
            districtMasterDto.setDistrictName(districtMaster.getDistrictName());
            pincodeMasterDto.setDistrict(districtMasterDto);

            pincodeMasterDtoList.add(pincodeMasterDto);
        }
        return new Response(SUCCESS, pincodeMasterDtoList, HttpStatus.OK);
    }

    @Override
    public Response addPincode(PincodeMasterDto pincodeMasterDto) {
        Response response = new Response();
        UserSession userSession = userCredentialService.getUserSession();
        Optional<PincodeMaster> pincodeMaster = pincodeRepository.findByPincodeMasterPK_Pincode(pincodeMasterDto.getPincode());
        if (pincodeMaster.isPresent()) {
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("This Pincode already exists.");
            response.setStatus(HttpStatus.BAD_REQUEST);
        } else {
            PincodeMaster pincode = new PincodeMaster();
            PincodeMasterPK pincodeMasterPK = new PincodeMasterPK();
            pincodeMasterPK.setPincode(pincodeMasterDto.getPincode());
            pincodeMasterPK.setCountryId(1);
            pincode.setPincodeMasterPK(pincodeMasterPK);
            pincode.setStateId(pincodeMasterDto.getStateId());
            pincode.setActive("Y");
            pincode.setInsertedBy(String.valueOf(userSession.getCompany().getCompanyId()));
            pincode.setInsertedOn(LocalDateTime.now());
            pincodeRepository.save(pincode);
            response.setCode(HttpStatus.OK.value());
            response.setStatus(HttpStatus.OK);
            response.setMessage("Transaction saved successfully with Pincode : " + pincode.getPincodeMasterPK().getPincode());
        }
        return response;
    }

    @Override
    public Response updatePincode(PincodeMasterDto pincodeMasterDto) throws ObjectNotFoundException {
        UserSession userSession = userCredentialService.getUserSession();
        PincodeMaster pincodeMaster = pincodeRepository.findByPincodeMasterPK_Pincode(pincodeMasterDto.getPincode()).orElseThrow(() -> new ObjectNotFoundException("Pincode does not exist.", HttpStatus.NOT_FOUND));
        if (pincodeMaster.getPincodeMasterPK().getPincode().equals(pincodeMasterDto.getPincode())) {
            pincodeMasterDto.mapDtoToEntityForPincodeUpdate(pincodeMasterDto, pincodeMaster, userSession);
            pincodeRepository.save(pincodeMaster);
        }
        return new Response("Pincode updated successfully", HttpStatus.OK);
    }

    @Override
    public Response getPincodeDetails(Integer pincode) throws ObjectNotFoundException {
        PincodeMaster pincodeMaster = pincodeRepository.findByPincodeMasterPK_Pincode(pincode).orElseThrow(() -> new ObjectNotFoundException("Invalid Pincode.", HttpStatus.NOT_FOUND));
        PincodeMasterDto pincodeMasterDto = new PincodeMasterDto();
        BeanUtils.copyProperties(pincodeMaster, pincodeMasterDto);
        pincodeMasterDto.setPincode(pincodeMaster.getPincodeMasterPK().getPincode());
        return new Response(SUCCESS, pincodeMasterDto, HttpStatus.OK);
    }

    @Override
    public Response getPincodeDetailsList(FilterRequest filterRequest) throws BadRequestException {
        Long count = null;
        if (filterRequest.getStart() == 0) {
            count = pincodeDao.findAllPincodeMasterCount(filterRequest);
        }
        if ("Y".equalsIgnoreCase(filterRequest.getIsCsv())) {
            filterRequest.setLimit(pincodeDao.findAllPincodeMasterCount(filterRequest).intValue());
        }
        List<PincodeMaster> pincodeMasterList = pincodeDao.findAllPincodeMaster(filterRequest);
        List<PincodeMasterDto> pincodeMasterDtoList = new ArrayList<>();
        for (PincodeMaster pincodeMaster : pincodeMasterList) {
            PincodeMasterDto pincodeMasterDto = new PincodeMasterDto();
            pincodeMasterDto.setDistrictName(pincodeMaster.getDistrictMaster().getDistrictName());
            pincodeMasterDto.setDistrictId(pincodeMaster.getDistrictId());
            pincodeMasterDto.setStateId(pincodeMaster.getStateId());
            pincodeMasterDto.setStateName(pincodeMaster.getStateMaster().getStateName());
            pincodeMasterDto.setActive(pincodeMaster.getActive());
            pincodeMasterDto.setPincode(pincodeMaster.getPincodeMasterPK().getPincode());
            pincodeMasterDto.setCountryId(pincodeMaster.getPincodeMasterPK().getCountryId());
            pincodeMasterDtoList.add(pincodeMasterDto);
        }
        return new Response(SUCCESS, pincodeMasterDtoList, count, HttpStatus.OK);
    }
}