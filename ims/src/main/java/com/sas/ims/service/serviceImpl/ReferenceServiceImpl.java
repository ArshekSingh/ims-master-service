package com.sas.ims.service.serviceImpl;

import com.sas.ims.constant.Constant;
import com.sas.ims.dto.ServerSideDropDownDto;
import com.sas.ims.entity.ReferenceDetail;
import com.sas.ims.entity.ReferenceDetailPK;
import com.sas.ims.exception.ObjectNotFoundException;
import com.sas.ims.repository.ReferenceDetailRepository;
import com.sas.ims.request.ReferenceDetailDto;
import com.sas.ims.request.ReferenceDomainRequest;
import com.sas.ims.request.ReferenceRequest;
import com.sas.ims.service.ReferenceService;
import com.sas.tokenlib.response.Response;
import com.sas.tokenlib.service.UserCredentialService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class ReferenceServiceImpl implements ReferenceService, Constant {

    private final ReferenceDetailRepository referenceDetailRepository;

    private final UserCredentialService userCredentialService;

    @Override
    public Response getReferenceByKey(String referenceKey) {
        Response response = new Response();
        List<ServerSideDropDownDto> serverSideDropDownDtoList = new ArrayList<>();
        if (StringUtils.hasText(referenceKey)) {
            List<String> domainList = referenceDetailRepository.findByReferenceDomain(referenceKey);
            for (String domainName : domainList) {
                ServerSideDropDownDto sideDropDownDto = new ServerSideDropDownDto();
                sideDropDownDto.setId(domainName);
                sideDropDownDto.setLabel(domainName);
                serverSideDropDownDtoList.add(sideDropDownDto);
            }
            response.setCode(HttpStatus.OK.value());
            response.setStatus(HttpStatus.OK);
            response.setData(serverSideDropDownDtoList);
            response.setMessage("Reference Domain List Fetch Successfully");
        } else {
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessage("Invalid Request Parameters / Request Parameters Can't Be Null");
        }
        return response;
    }

    @Override
    public Response findByReferenceDomain(String domain) throws ObjectNotFoundException {
        List<ReferenceDetail> domainList = referenceDetailRepository.findByReferenceDetailPK_ReferenceDomainOrderByValue4(domain).orElseThrow((() -> new ObjectNotFoundException("Data Not Found.", HttpStatus.NOT_FOUND)));
        List<ReferenceDetailDto> referenceDetailDtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(domainList)) {
            // set value in Dto
            setValueInDTO(domainList, referenceDetailDtos);
        }
        return new Response(SUCCESS, referenceDetailDtos, HttpStatus.OK);
    }

    @Override
    public Response addReferenceDetails(ReferenceRequest request) throws ObjectNotFoundException {
        Response response = new Response();
        if (!CollectionUtils.isEmpty(request.getReferenceDetailDto())) {
            List<ReferenceDetail> referenceDetail = referenceDetailRepository.findByReferenceDetailPK_ReferenceDomainOrderByValue4(request.getReferenceDetailDto().get(0).getReferenceDomain()).orElseThrow((() -> new ObjectNotFoundException("Data Not Found.", HttpStatus.NOT_FOUND)));
            if (!CollectionUtils.isEmpty(referenceDetail)) {
                response.setCode(HttpStatus.BAD_REQUEST.value());
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Reference Domain Already Present , Please Use Different Reference Domain");
            } else {
                saveNewReferencesDetails(request.getReferenceDetailDto());
                response.setCode(HttpStatus.OK.value());
                response.setStatus(HttpStatus.OK);
                response.setMessage("Data Save Successfully");
            }
        } else {
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessage("Invalid Request Parameters / Request Parameters Can't Be Null");
        }
        return response;
    }

    private void saveNewReferencesDetails(List<ReferenceDetailDto> referenceDetailDto) {
        List<ReferenceDetail> referenceDetails = new ArrayList<>();
        for (ReferenceDetailDto reference : referenceDetailDto) {
            ReferenceDetail referenceDetail = new ReferenceDetail();
            ReferenceDetailPK referenceDetailPK = new ReferenceDetailPK();
            referenceDetailPK.setReferenceDomain(reference.getReferenceDomain());
            referenceDetailPK.setKeyValue(reference.getKeyValue());
            referenceDetail.setReferenceDetailPK(referenceDetailPK);
            referenceDetail.setDescription(reference.getDescription());
            referenceDetail.setValue1(reference.getValue1());
            referenceDetail.setValue2(reference.getValue2());
            referenceDetail.setValue3(reference.getValue3());
            referenceDetail.setValue4(reference.getValue4());
            referenceDetail.setInsertedBy(userCredentialService.getUserSession().getSub());
            referenceDetail.setInsertedOn(LocalDateTime.now());
            referenceDetails.add(referenceDetail);
        }
        if (!CollectionUtils.isEmpty(referenceDetails)) {
            referenceDetailRepository.saveAll(referenceDetails);
        }
    }

    private void setValueInDTO(List<ReferenceDetail> domainList, List<ReferenceDetailDto> referenceDetailDtos) {
        for (ReferenceDetail referenceDetail : domainList) {
            ReferenceDetailDto referenceDetailDto = new ReferenceDetailDto();
            referenceDetailDto.setReferenceDomain(referenceDetail.getReferenceDetailPK().getReferenceDomain());
            referenceDetailDto.setKeyValue(referenceDetail.getReferenceDetailPK().getKeyValue());
            referenceDetailDto.setDescription(referenceDetail.getDescription());
            referenceDetailDto.setValue1(referenceDetail.getValue1());
            referenceDetailDto.setValue2(referenceDetail.getValue2());
            referenceDetailDto.setValue3(referenceDetail.getValue3());
            referenceDetailDto.setValue4(referenceDetail.getValue4());
            referenceDetailDtos.add(referenceDetailDto);
        }
    }

    @Override
    public Response updateReferenceDetail(ReferenceRequest request) throws ObjectNotFoundException {
        Response response = new Response();
        if (!CollectionUtils.isEmpty(request.getReferenceDetailDto())) {
            log.info("Find Reference Details By Reference Domain {} ", request.getReferenceDetailDto().get(0).getReferenceDomain());
            List<ReferenceDetail> dbReferenceDetailList = referenceDetailRepository.findByReferenceDetailPK_ReferenceDomainOrderByValue4(request.getReferenceDetailDto().get(0).getReferenceDomain()).orElseThrow((() -> new ObjectNotFoundException("Data Not Found.", HttpStatus.NOT_FOUND)));
            if (!CollectionUtils.isEmpty(dbReferenceDetailList)) {
                // logic to update / Add / Delete Reference Domain
                updateReferenceDetails(request.getReferenceDetailDto(), dbReferenceDetailList);
                response.setCode(HttpStatus.OK.value());
                response.setStatus(HttpStatus.OK);
                response.setMessage("Data Update Successfully ");
            } else {
                response.setCode(HttpStatus.BAD_REQUEST.value());
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Invalid Domain Reference ");
            }
        } else {
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessage("Invalid Request Parameters / Request Parameters Can't Be Null");
        }
        return response;
    }

    private void updateReferenceDetails(List<ReferenceDetailDto> referenceDetailDto, List<ReferenceDetail> dbReferenceDetailList) {
        Set<ReferenceDetail> toBeDeleted = new HashSet<>();
        Set<ReferenceDetail> updateEntityList = new HashSet<>();
        List<ReferenceDetailDto> updateListDTO = new ArrayList<>();
        for (ReferenceDetail reference : dbReferenceDetailList) {
            boolean isExist = false;
            for (ReferenceDetailDto reqReferenceDto : referenceDetailDto) {
                if (reference.getReferenceDetailPK().getKeyValue().equals(reqReferenceDto.getKeyValue())) {
                    setReferenceDetailFromDTO(reference, reqReferenceDto);
                    updateEntityList.add(reference);
                    updateListDTO.add(reqReferenceDto);
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                toBeDeleted.add(reference);
            }
        }
        //Remove Common Elements from referenceDetailDto List
        log.info("Remove Common Elements from referenceDetailDto List");
        referenceDetailDto.removeAll(updateListDTO);
        if (!CollectionUtils.isEmpty(updateEntityList)) {
            log.info("Update Existing Reference Details");
            referenceDetailRepository.saveAll(updateEntityList);
        }
        if (!CollectionUtils.isEmpty(toBeDeleted)) {
            log.info("Deleted Reference Details Those Not Found in Reference Details DTO ");
            referenceDetailRepository.deleteAll(toBeDeleted);
        }
        if (!CollectionUtils.isEmpty(referenceDetailDto)) {
            log.info("Create New Reference Details");
            saveNewReferencesDetails(referenceDetailDto);
        }
    }

    private void setReferenceDetailFromDTO(ReferenceDetail reference, ReferenceDetailDto referenceDetailDto) {
        ReferenceDetailPK referenceDetailPK = reference.getReferenceDetailPK();
        referenceDetailPK.setKeyValue(referenceDetailDto.getKeyValue());
        reference.setDescription(referenceDetailDto.getDescription());
        reference.setValue1(referenceDetailDto.getValue1());
        reference.setValue2(referenceDetailDto.getValue2());
        reference.setValue3(referenceDetailDto.getValue3());
        reference.setValue4(referenceDetailDto.getValue4());
        reference.setUpdatedBy(userCredentialService.getUserSession().getSub());
        reference.setUpdatedOn(LocalDateTime.now());

    }

    @Override
    public Response getReferenceDetailsList(ReferenceDomainRequest referenceDomainRequest) {
        List<ReferenceDetail> referenceDetailList = referenceDetailRepository.findByReferenceDetailPK_ReferenceDomainInOrderByValue4(referenceDomainRequest.getReferenceDomain());
        Map<String, List<ReferenceDetailDto>> referenceMap = new HashMap<>();
        for (ReferenceDetail referenceDetail : referenceDetailList) {
            ReferenceDetailDto referenceDetailDto = new ReferenceDetailDto();
            BeanUtils.copyProperties(referenceDetail, referenceDetailDto);
            referenceDetailDto.setReferenceDomain(referenceDetail.getReferenceDetailPK().getReferenceDomain());
            referenceDetailDto.setKeyValue(referenceDetail.getReferenceDetailPK().getKeyValue());
            if (referenceMap.get(referenceDetailDto.getReferenceDomain()) != null) {
                List<ReferenceDetailDto> list = referenceMap.get(referenceDetailDto.getReferenceDomain());
                list.add(referenceDetailDto);
                referenceMap.put(referenceDetailDto.getReferenceDomain(), list);
            } else {
                List<ReferenceDetailDto> list = new ArrayList<>();
                list.add(referenceDetailDto);
                referenceMap.put(referenceDetailDto.getReferenceDomain(), list);
            }
        }
        return new Response(SUCCESS, referenceMap, HttpStatus.OK);
    }
}