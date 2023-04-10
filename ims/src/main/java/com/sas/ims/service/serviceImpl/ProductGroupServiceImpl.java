package com.sas.ims.service.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sas.ims.repository.ProductGroupRepository;
import com.sas.ims.service.ProductGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sas.ims.constant.Constant;
import com.sas.ims.dto.ProductGroupDto;
import com.sas.ims.entity.ProductGroup;
import com.sas.ims.response.Response;
import com.sas.ims.utils.ObjectMapperUtil;

@Service
public class ProductGroupServiceImpl implements ProductGroupService, Constant {

	@Autowired
    ProductGroupRepository productGroupRepository;

	@Autowired
	ObjectMapperUtil objectMapperUtil;

	@Override
	public Response getActiveProductGroupDetails(ProductGroupDto dto) {
		// TODO Auto-generated method stub
		Pageable pageable = PageRequest.of(dto.getStartIndex(), dto.getEndIndex(), Sort.by("id").descending());
		Optional<List<ProductGroup>> productMasteLIstOptional = productGroupRepository
				.findByOrgIdAndGroupCode(dto.getOrgId(), dto.getGroupCode(), pageable);
		if (!productMasteLIstOptional.isPresent()) {
			return Response.builder().status(HttpStatus.BAD_REQUEST).code(HttpStatus.BAD_REQUEST.value())
					.message(HttpStatus.BAD_REQUEST.name()).build();
		}
		List<ProductGroupDto> dtoList = productMasteLIstOptional.get().stream().map(this::entityToDto)
				.collect(Collectors.toList());
		return Response.builder().status(HttpStatus.OK).data(dtoList).code(HttpStatus.OK.value())
				.message(HttpStatus.OK.name()).build();
	}

	public ProductGroupDto entityToDto(ProductGroup productMaster) {
		ProductGroupDto labTestDto = new ProductGroupDto();
		objectMapperUtil.map(productMaster, labTestDto);
		return labTestDto;
	}

	@Override
	public Response addProductGroup(ProductGroupDto dto) {
		// TODO Auto-generated method stub
		ProductGroup entity = new ProductGroup();
		objectMapperUtil.map(dto, entity);
		productGroupRepository.save(entity);
		return Response.builder().status(HttpStatus.OK).code(HttpStatus.OK.value()).message(HttpStatus.OK.name())
				.build();

	}

	@Override
	public Response updateProductGroup(ProductGroupDto dto) {
		// TODO Auto-generated method stub
		Optional<ProductGroup> productMasterOptional = productGroupRepository
				.findByProductGroupIdAndOrgId(dto.getParentGroupId(), dto.getOrgId());
		if (!productMasterOptional.isPresent()) {
			return Response.builder().status(HttpStatus.BAD_REQUEST).code(HttpStatus.BAD_REQUEST.value())
					.message(HttpStatus.BAD_REQUEST.name()).build();
		}
		objectMapperUtil.map(dto, productMasterOptional.get());
		productGroupRepository.save(productMasterOptional.get());
		return Response.builder().status(HttpStatus.OK).code(HttpStatus.OK.value()).message(HttpStatus.OK.name())
				.build();
	}

	@Override
	public Response getProductGroupDetailsById(Long productId) {
		// TODO Auto-generated method stub
		Optional<ProductGroup> productMasterOptional = productGroupRepository.findByProductGroupIdAndOrgId(productId, 1L);
		if (!productMasterOptional.isPresent()) {
			return Response.builder().status(HttpStatus.BAD_REQUEST).code(HttpStatus.BAD_REQUEST.value())
					.message(HttpStatus.BAD_REQUEST.name()).build();
		}
		ProductGroupDto dto = new ProductGroupDto();
		objectMapperUtil.map(dto, productMasterOptional.get());
		return Response.builder().status(HttpStatus.OK).code(HttpStatus.OK.value()).data(dto)
				.message(HttpStatus.OK.name()).build();
	}

	@Override
	public Response softDeleteProductGroup(Long productId) {
		// TODO Auto-generated method stub
		Optional<ProductGroup> productMasterOptional = productGroupRepository.findByProductGroupIdAndOrgId(productId, 1L);
		if (!productMasterOptional.isPresent()) {
			return Response.builder().status(HttpStatus.BAD_REQUEST).code(HttpStatus.BAD_REQUEST.value())
					.message(HttpStatus.BAD_REQUEST.name()).build();
		}
		productMasterOptional.get().setActive("N");
		productGroupRepository.save(productMasterOptional.get());
		return Response.builder().status(HttpStatus.OK).code(HttpStatus.OK.value()).message(HttpStatus.OK.name())
				.build();
	}

	@Override
	public Response getParentGroup() {
		// TODO Auto-generated method stub
		Optional<List<ProductGroup>> groupListOptional=productGroupRepository.findByOrgIdAndActiveAndGroupType(1L, ACTIVE_STATUS,PARENT_GROUP_TYPE);
		List<ProductGroupDto> dtoList = groupListOptional.get().stream().map(this::entityToDto)
				.collect(Collectors.toList());
		return Response.builder().status(HttpStatus.OK).data(dtoList).code(HttpStatus.OK.value())
				.message(HttpStatus.OK.name()).build();
	}
}
