package com.sas.ims.service.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sas.ims.assembler.ProductGroupAssembler;
import com.sas.ims.exception.BadRequestException;
import com.sas.ims.repository.ProductGroupRepository;
import com.sas.ims.service.ProductGroupService;
import com.sas.ims.utils.DateTimeUtil;
import com.sas.tokenlib.response.Response;
import com.sas.tokenlib.service.UserCredentialService;
import com.sas.tokenlib.utils.UserSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sas.ims.constant.Constant;
import com.sas.ims.dto.ProductGroupDto;
import com.sas.ims.entity.ProductGroup;
import com.sas.ims.utils.ObjectMapperUtil;

@Service
@Slf4j
@AllArgsConstructor
public class ProductGroupServiceImpl implements ProductGroupService, Constant {

    private final ProductGroupRepository productGroupRepository;

	private final ObjectMapperUtil objectMapperUtil;

	private final UserCredentialService userCredentialService;

	private final ProductGroupAssembler productGroupAssembler;

	@Override
	public Response getActiveProductGroupDetails(ProductGroupDto dto) throws BadRequestException {
		log.info("Request initiated to get active product group details");
		try {
			UserSession userSession = userCredentialService.getUserSession();
			Pageable pageable = PageRequest.of(dto.getStartIndex(), dto.getEndIndex(), Sort.by("productGroupId").descending());
			Optional<List<ProductGroup>> productMasteLIstOptional = productGroupRepository
					.findByOrgIdAndGroupCode(userSession.getCompany().getCompanyId(), dto.getGroupCode(), pageable);
			if (productMasteLIstOptional.isEmpty()) {
				return Response.builder().status(HttpStatus.BAD_REQUEST).code(HttpStatus.BAD_REQUEST.value())
						.message(HttpStatus.BAD_REQUEST.name()).build();
			}
			List<ProductGroupDto> dtoList = productMasteLIstOptional.get().stream().map(this::entityToDto)
					.collect(Collectors.toList());
			return Response.builder().status(HttpStatus.OK).data(dtoList).code(HttpStatus.OK.value())
					.message(HttpStatus.OK.name()).build();
		} catch (Exception e) {
			log.info("Exception occurred due to {}", e.getMessage());
			throw new BadRequestException(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	public ProductGroupDto entityToDto(ProductGroup productMaster) {
		ProductGroupDto dto = new ProductGroupDto();
		objectMapperUtil.map(productMaster, dto);
		dto.setUpdatedBy(productMaster.getUpdatedBy());
		dto.setUpdatedOn(DateTimeUtil.dateToString(productMaster.getUpdatedOn() != null ? productMaster.getUpdatedOn().toLocalDate() : null));
		dto.setInsertedOn(DateTimeUtil.dateToString(productMaster.getInsertedOn() != null ? productMaster.getInsertedOn().toLocalDate() : null));
		return dto;
	}

	@Override
	public Response addProductGroup(ProductGroupDto dto) {
		log.info("Request initiated to add product group");
		UserSession userSession = userCredentialService.getUserSession();
		ProductGroup productGroup = new ProductGroup();
		objectMapperUtil.map(dto, productGroup);
		productGroup.setOrgId(userSession.getCompany().getCompanyId());
		productGroup.setInsertedBy(userSession.getSub());
		productGroup.setInsertedOn(LocalDateTime.now());
		productGroupRepository.save(productGroup);
		return Response.builder().status(HttpStatus.OK).code(HttpStatus.OK.value()).message(HttpStatus.OK.name())
				.build();

	}

	@Override
	public Response updateProductGroup(ProductGroupDto dto) throws BadRequestException {
		try {
			UserSession userSession = userCredentialService.getUserSession();
			Optional<ProductGroup> productMasterOptional = productGroupRepository
					.findByOrgIdAndProductGroupId(userSession.getCompany().getCompanyId(), dto.getProductGroupId());
			if (productMasterOptional.isEmpty()) {
				return Response.builder().status(HttpStatus.BAD_REQUEST).code(HttpStatus.BAD_REQUEST.value())
						.message(HttpStatus.BAD_REQUEST.name()).build();
			}
			productGroupAssembler.dtoToEntity(dto, productMasterOptional.get());
			productMasterOptional.get().setUpdatedBy(userSession.getSub());
			productMasterOptional.get().setUpdatedOn(LocalDateTime.now());
			productGroupRepository.save(productMasterOptional.get());
			return Response.builder().status(HttpStatus.OK).code(HttpStatus.OK.value()).message(HttpStatus.OK.name())
					.build();
		} catch(Exception e) {
			log.error("Exception occurred due to {}", e.getMessage());
			throw new BadRequestException(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public Response getProductGroupDetailsById(Long productGroupId) throws BadRequestException {
		UserSession userSession = userCredentialService.getUserSession();
		try {
			Optional<ProductGroup> productMasterOptional = productGroupRepository.findByOrgIdAndProductGroupId(userSession.getCompany().getCompanyId(), productGroupId);
			if (productMasterOptional.isEmpty()) {
				return Response.builder().status(HttpStatus.BAD_REQUEST).code(HttpStatus.BAD_REQUEST.value())
						.message(HttpStatus.BAD_REQUEST.name()).build();
			}
			ProductGroupDto dto = new ProductGroupDto();
			objectMapperUtil.map(productMasterOptional.get(), dto);
			dto.setInsertedOn(DateTimeUtil.dateToString(productMasterOptional.get().getInsertedOn() != null ? productMasterOptional.get().getInsertedOn().toLocalDate() : null));
			dto.setUpdatedOn(DateTimeUtil.dateToString(productMasterOptional.get().getUpdatedOn() != null ? productMasterOptional.get().getUpdatedOn().toLocalDate() : null));
			return Response.builder().status(HttpStatus.OK).code(HttpStatus.OK.value()).data(dto)
					.message(HttpStatus.OK.name()).build();
		} catch (Exception e) {
			log.error("Exception occurred due to {}", e.getMessage());
			throw new BadRequestException(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public Response softDeleteProductGroup(Long productId) {
		UserSession userSession = userCredentialService.getUserSession();
		Optional<ProductGroup> productMasterOptional = productGroupRepository.findByOrgIdAndProductGroupId(userSession.getCompany().getCompanyId(), productId);
		if (!productMasterOptional.isPresent()) {
			return Response.builder().status(HttpStatus.BAD_REQUEST).code(HttpStatus.BAD_REQUEST.value())
					.message(HttpStatus.BAD_REQUEST.name()).build();
		}
		productMasterOptional.get().setStatus("N");
		productGroupRepository.save(productMasterOptional.get());
		return Response.builder().status(HttpStatus.OK).code(HttpStatus.OK.value()).message(HttpStatus.OK.name())
				.build();
	}

	@Override
	public Response getParentGroup() throws BadRequestException {
		log.info("Request initiated to fetch partner group");
		UserSession userSession = userCredentialService.getUserSession();
		Optional<List<ProductGroup>> groupListOptional=productGroupRepository.findByOrgIdAndStatusAndGroupType(userSession.getCompany().getCompanyId(), ACTIVE_STATUS, PARTNER_GROUP_TYPE);
		if(groupListOptional.isEmpty()) {
			throw new BadRequestException("No record found", HttpStatus.BAD_REQUEST);
		}
		List<ProductGroupDto> dtoList = groupListOptional.get().stream().map(this::entityToDto)
				.collect(Collectors.toList());
		return Response.builder().status(HttpStatus.OK).data(dtoList).code(HttpStatus.OK.value())
				.message(HttpStatus.OK.name()).build();
	}
}
