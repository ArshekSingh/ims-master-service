package com.sts.ims.service.serviceImpl;

import com.sts.ims.constant.Constant;
import com.sts.ims.dto.BranchMasterDto;
import com.sts.ims.entity.BranchMaster;
import com.sts.ims.entity.OrganisationHierarchy;
import com.sts.ims.entity.ProductMaster;
import com.sts.ims.exception.BadRequestException;
import com.sts.ims.exception.ObjectNotFoundException;
import com.sts.ims.repository.BranchMasterRepository;
import com.sts.ims.repository.OrganisationHierarchyRepository;
import com.sts.ims.request.FilterRequest;
import com.sts.ims.response.Response;
import com.sts.ims.service.BranchService;
import com.sts.ims.utils.DateTimeUtil;
import com.sts.ims.utils.ObjectMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Converters;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BranchServiceImpl implements BranchService, Constant {

    private BranchMasterRepository branchMasterRepository;

    private OrganisationHierarchyRepository hierarchyRepository;

    @Override
    public Response getBranchDetail(Integer branchId) {
        Optional<BranchMaster> branchMaster = branchMasterRepository.findByBranchIdAndOrgId(branchId, userSession.getOrganizationId()).orElseThrow(() -> new ObjectNotFoundException("Invalid Branch Id.", HttpStatus.NOT_FOUND));
        BranchMasterDto branchMasterDto = new BranchMasterDto();
        ObjectMapperUtil.map(branchMaster.get(), branchMasterDto);
        branchMasterDto.setCreatedOn(DateTimeUtil.dateTimeToString(branchMaster.get().getCreatedOn()));
        branchMasterDto.setModifiedOn(DateTimeUtil.dateTimeToString(branchMaster.get().getModifiedOn()));
        return new Response(SUCCESS, branchMasterDto, HttpStatus.OK);
    }

    @Override
    public Response getBranchDetailList(FilterRequest filterRequest) throws BadRequestException {
        Pageable pageable = (Pageable) PageRequest.of(filterRequest.getStartIndex(), filterRequest.getEndIndex(), Sort.by("branchId").descending());
        List<BranchMaster> branchMasterList = filteredData(specification(filterRequest), pageable);
        List<BranchMasterDto> branchMasterDtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(branchMasterList)) {
            for (BranchMaster branchMaster : branchMasterList) {
                BranchMasterDto branchMasterDto = new BranchMasterDto();
                ObjectMapperUtil.map(branchMaster, branchMasterDto);
                branchMasterDto.setCreatedOn(DateTimeUtil.dateTimeToString(branchMaster.getCreatedOn()));
                branchMasterDto.setModifiedOn(DateTimeUtil.dateTimeToString(branchMaster.getModifiedOn()));
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
            branch.setCreatedBy("");
            branchMasterDto.setParentId(getParentId(branchMasterDto, parentId, organisationHierarchyList));
            branch.setCreatedOn(LocalDateTime.now());
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
    public Response updateBranch(BranchMasterDto branchMasterDto) {

        BranchMaster branchMaster = branchMasterRepository.findByBranchIdAndOrgId(branchMasterDto.getBranchId(), userSession.getOrganizationId()).orElseThrow(() -> new ObjectNotFoundException("Branch does not exist.", HttpStatus.NOT_FOUND));
//        branchMasterDto.mapDtoToEntityForBranchUpdate(branchMasterDto, branchMaster, userSession);
        branchMasterRepository.save(branchMaster);
        return new Response("Branch updated successfully", HttpStatus.OK);
    }
}
