package com.sts.ims.service.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.sts.ims.converter.ProductConvertor;
import com.sts.ims.dto.ProductMasterDto;
import com.sts.ims.entity.ProductMaster;
import com.sts.ims.repository.ProductMasterRepository;
import com.sts.ims.response.Response;
import com.sts.ims.service.ProductMasterService;
import com.sts.ims.utils.DateTimeUtil;
import com.sts.ims.utils.ObjectMapperUtil;

@Service
public class ProductMasterServiceImpl implements ProductMasterService {

	@Autowired
	ProductMasterRepository productMasterRepository;

	@Autowired
	ObjectMapperUtil objectMapperUtil;
	
	@Autowired
	ProductConvertor productConvertor;

	@Override
	public Response getActiveProductDetails(ProductMasterDto dto) {
		// TODO Auto-generated method stub
		Pageable pageable = PageRequest.of(dto.getStartIndex(), dto.getEndIndex(), Sort.by("productId").descending());
		Optional<List<ProductMaster>> productMasteLIstOptional = productMasterRepository
				.findByOrgIdAndProductCode(dto.getOrgId(), dto.getProductCode(), pageable);
		if (!productMasteLIstOptional.isPresent()) {
			return Response.builder().status(HttpStatus.BAD_REQUEST).code(HttpStatus.BAD_REQUEST.value())
					.message(HttpStatus.BAD_REQUEST.name()).build();
		}
		List<ProductMasterDto> dtoList = productMasteLIstOptional.get().stream().map(this::entityToDto)
				.collect(Collectors.toList());
		return Response.builder().status(HttpStatus.OK).data(dtoList).code(HttpStatus.OK.value())
				.message(HttpStatus.OK.name()).build();
	}

	public ProductMasterDto entityToDto(ProductMaster productMaster) {
		ProductMasterDto labTestDto = new ProductMasterDto();
		objectMapperUtil.map(productMaster, labTestDto);
		return labTestDto;
	}

	@Override
	public Response addProductMaster(ProductMasterDto dto) {
		// TODO Auto-generated method stub
		ProductMaster entity = new ProductMaster();
		objectMapperUtil.map(dto, entity);
		productMasterRepository.save(entity);
		return Response.builder().status(HttpStatus.OK).code(HttpStatus.OK.value()).message(HttpStatus.OK.name())
				.build();

	}

	@Override
	public Response updateProductMaster(ProductMasterDto dto) {
		// TODO Auto-generated method stub
		Optional<ProductMaster> productMasterOptional = productMasterRepository
				.findByProductIdAndOrgId(dto.getProductId(), dto.getOrgId());
		if (!productMasterOptional.isPresent()) {
			return Response.builder().status(HttpStatus.BAD_REQUEST).code(HttpStatus.BAD_REQUEST.value())
					.message(HttpStatus.BAD_REQUEST.name()).build();
		}
		objectMapperUtil.map(dto, productMasterOptional.get());
		productMasterRepository.save(productMasterOptional.get());
		return Response.builder().status(HttpStatus.OK).code(HttpStatus.OK.value()).message(HttpStatus.OK.name())
				.build();
	}

	@Override
	public Response getProductDetailsById(Long productId) {
		// TODO Auto-generated method stub
		Optional<ProductMaster> productMasterOptional = productMasterRepository
				.findByProductIdAndOrgId(productId, 1L);
		if (!productMasterOptional.isPresent()) {
			return Response.builder().status(HttpStatus.BAD_REQUEST).code(HttpStatus.BAD_REQUEST.value())
					.message(HttpStatus.BAD_REQUEST.name()).build();
		}
		ProductMasterDto dto = new ProductMasterDto();
		objectMapperUtil.map(dto, productMasterOptional.get());
		return Response.builder().status(HttpStatus.OK).code(HttpStatus.OK.value()).data(dto)
				.message(HttpStatus.OK.name()).build();
	}

	@Override
	public Response softDeleteProduct(Long productId) {
		// TODO Auto-generated method stub
		Optional<ProductMaster> productMasterOptional = productMasterRepository
				.findByProductIdAndOrgId(productId, 1L);
		if (!productMasterOptional.isPresent()) {
			return Response.builder().status(HttpStatus.BAD_REQUEST).code(HttpStatus.BAD_REQUEST.value())
					.message(HttpStatus.BAD_REQUEST.name()).build();
		}
		productMasterOptional.get().setStatus("N");
		productMasterRepository.save(productMasterOptional.get());
		return Response.builder().status(HttpStatus.OK).code(HttpStatus.OK.value())
				.message(HttpStatus.OK.name()).build();
	}

	@Override
	public Response productUploaders(MultipartFile file) {
		// TODO Auto-generated method stub
		List<ProductMasterDto> dtoList=productConvertor.convertFileToDto(file);
		dtoList.stream().map(this::dtoToEntity)
		.collect(Collectors.toList());
		return null;
	}
	public ProductMasterDto dtoToEntity(ProductMasterDto dto) {
		try {
		ProductMaster entity = new ProductMaster();
		entity.setProductName(dto.getProductName());
		entity.setProductCode(dto.getProductCode());
		entity.setAmount(dto.getAmount());
		entity.setOrgId(dto.getOrgId());
		entity.setPartnerRefferenceCode(dto.getPartnerRefferenceCode());
		if(StringUtils.hasText(dto.getProductClosingDate()))
		entity.setProductClosingDate(DateTimeUtil.stringToDateTime(dto.getProductClosingDate(),DateTimeUtil.D_MMM_YYYY));
		entity.setProductGroupId(dto.getProductGroupId());
		entity.setProductIdentifierCode(dto.getProductIdentifierCode());
		if(StringUtils.hasText(dto.getProductOpeningDate()))
		entity.setProductOpeningDate(DateTimeUtil.stringToDateTime(dto.getProductOpeningDate(),DateTimeUtil.D_MMM_YYYY));
		entity.setQuantity(dto.getQuantity());
		entity.setStatus(dto.getStatus());
		productMasterRepository.save(entity);
		}catch (Exception e) {
			// TODO: handle exception
			dto.setIsProccesble(false);
		}
		return dto;
	}

}
