package com.sas.ims.service.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sas.ims.converter.ProductConvertor;
import com.sas.ims.exception.BadRequestException;
import com.sas.ims.repository.ProductMasterRepository;
import com.sas.ims.service.ProductMasterService;
import com.sas.ims.utils.ValidationUtil;
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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.sas.ims.dto.ProductMasterDto;
import com.sas.ims.entity.ProductMaster;
import com.sas.ims.utils.DateTimeUtil;
import com.sas.ims.utils.ObjectMapperUtil;

@Service
@AllArgsConstructor
@Slf4j
public class ProductMasterServiceImpl implements ProductMasterService {

    private final ProductMasterRepository productMasterRepository;

    private final ObjectMapperUtil objectMapperUtil;

    private final ProductConvertor productConvertor;

    private final UserCredentialService userCredentialService;

    @Override
    public Response getActiveProductDetails(ProductMasterDto dto) {
        log.info("request initiated to call method getActiveProductDetails ");
        UserSession userSession = userCredentialService.getUserSession();
        Pageable pageable = PageRequest.of(dto.getStartIndex(), dto.getEndIndex(), Sort.by("productId").descending());
        List<ProductMaster> productMasteLIstOptional = productMasterRepository
                .findByOrgIdAndProductCode(userSession.getCompany().getCompanyId(), dto.getProductCode(), pageable).getContent();
        if (productMasteLIstOptional.isEmpty()) {
            return Response.builder().status(HttpStatus.BAD_REQUEST).code(HttpStatus.OK.value())
                    .message("No data found").build();
        }
        List<ProductMasterDto> dtoList = productMasteLIstOptional.stream().map(this::entityToDto)
                .collect(Collectors.toList());
        return Response.builder().status(HttpStatus.OK).data(dtoList).code(HttpStatus.OK.value())
                .message(HttpStatus.OK.name()).build();
    }

    public ProductMasterDto entityToDto(ProductMaster productMaster) {
        ProductMasterDto dto = new ProductMasterDto();
        objectMapperUtil.map(productMaster, dto);
        dto.setProductOpeningDate(DateTimeUtil.dateToString(productMaster.getProductOpeningDate() != null ? productMaster.getProductOpeningDate().toLocalDate() : null));
        dto.setProductClosingDate(DateTimeUtil.dateToString(productMaster.getProductClosingDate() != null ? productMaster.getProductClosingDate().toLocalDate() : null));
        dto.setInsertedOn(DateTimeUtil.dateToString(productMaster.getInsertedOn() != null ? productMaster.getInsertedOn().toLocalDate() : null));
        dto.setUpdatedOn(DateTimeUtil.dateToString(productMaster.getUpdatedOn() != null ? productMaster.getUpdatedOn().toLocalDate() : null));
        return dto;
    }

    @Override
    public Response addProductMaster(ProductMasterDto dto) throws BadRequestException {
        UserSession userSession = userCredentialService.getUserSession();
        ValidationUtil.validateAddProductRequest(dto);
        ProductMaster productMaster = new ProductMaster();
        objectMapperUtil.map(dto, productMaster);
        productMaster.setOrgId(userSession.getCompany().getCompanyId());
        productMaster.setProductOpeningDate(DateTimeUtil.stringToLocalDateTime(dto.getProductOpeningDate(), DateTimeUtil.DDMMYYYY));
        productMaster.setProductClosingDate(DateTimeUtil.stringToLocalDateTime(dto.getProductClosingDate(), DateTimeUtil.DDMMYYYY));
        productMaster.setInsertedBy(userSession.getSub());
        productMaster.setInsertedOn(LocalDateTime.now());
        productMasterRepository.save(productMaster);
        return Response.builder().status(HttpStatus.OK).code(HttpStatus.OK.value()).message(HttpStatus.OK.name())
                .build();

    }

    @Override
    public Response updateProductMaster(ProductMasterDto dto) throws BadRequestException {
        if (dto.getProductId() == null) {
            throw new BadRequestException("Product id cannot be null", HttpStatus.BAD_REQUEST);
        }
        UserSession userSession = userCredentialService.getUserSession();
        log.info("Request initiated to update product for product Id {}", dto.getProductId());
        Optional<ProductMaster> productMasterOptional = productMasterRepository
                .findByOrgIdAndProductId(userSession.getCompany().getCompanyId(), dto.getProductId());
        if (productMasterOptional.isEmpty()) {
            return Response.builder().status(HttpStatus.BAD_REQUEST).code(HttpStatus.BAD_REQUEST.value())
                    .message("No detail found for the given product id").build();
        }
        objectMapperUtil.map(dto, productMasterOptional.get());
        productMasterOptional.get().setOrgId(userSession.getCompany().getCompanyId());
        productMasterOptional.get().setProductOpeningDate(DateTimeUtil.stringToLocalDateTime(dto.getProductOpeningDate(), DateTimeUtil.DDMMYYYY));
        productMasterOptional.get().setProductClosingDate(DateTimeUtil.stringToLocalDateTime(dto.getProductClosingDate(), DateTimeUtil.DDMMYYYY));
        productMasterOptional.get().setUpdatedBy(userSession.getSub());
        productMasterOptional.get().setUpdatedOn(LocalDateTime.now());
        productMasterRepository.save(productMasterOptional.get());
        return Response.builder().status(HttpStatus.OK).code(HttpStatus.OK.value()).message(HttpStatus.OK.name())
                .build();
    }

    @Override
    public Response getProductDetailsById(Long productId) {
        UserSession userSession = userCredentialService.getUserSession();
        Optional<ProductMaster> productMasterOptional = productMasterRepository
                .findByOrgIdAndProductId(userSession.getCompany().getCompanyId(), productId);
        if (productMasterOptional.isEmpty()) {
            return Response.builder().status(HttpStatus.BAD_REQUEST).code(HttpStatus.BAD_REQUEST.value())
                    .message("No data found").build();
        }
        ProductMasterDto dto = new ProductMasterDto();
        objectMapperUtil.map(productMasterOptional.get(), dto);
        dto.setProductOpeningDate(DateTimeUtil.dateToString(productMasterOptional.get().getProductOpeningDate() != null ? productMasterOptional.get().getProductOpeningDate().toLocalDate() : null));
        dto.setProductClosingDate(DateTimeUtil.dateToString(productMasterOptional.get().getProductClosingDate() != null ? productMasterOptional.get().getProductClosingDate().toLocalDate() : null));
        dto.setInsertedOn(DateTimeUtil.dateToString(productMasterOptional.get().getInsertedOn() != null ? productMasterOptional.get().getInsertedOn().toLocalDate() : null));
        dto.setUpdatedOn(DateTimeUtil.dateToString(productMasterOptional.get().getUpdatedOn() != null ? productMasterOptional.get().getUpdatedOn().toLocalDate() : null));
        return Response.builder().status(HttpStatus.OK).code(HttpStatus.OK.value()).data(dto)
                .message(HttpStatus.OK.name()).build();
    }

    @Override
    public Response softDeleteProduct(Long productId) {
        log.info("Request initiated to soft delete product id {}", productId);
        UserSession userSession = userCredentialService.getUserSession();
        Optional<ProductMaster> productMasterOptional = productMasterRepository
                .findByOrgIdAndProductId(userSession.getCompany().getCompanyId(), productId);
        if (productMasterOptional.isEmpty()) {
            return Response.builder().status(HttpStatus.BAD_REQUEST).code(HttpStatus.BAD_REQUEST.value())
                    .message("No data found").build();
        }
        productMasterOptional.get().setStatus("N");
        productMasterRepository.save(productMasterOptional.get());
        return Response.builder().status(HttpStatus.OK).code(HttpStatus.OK.value())
                .message(HttpStatus.OK.name()).build();
    }

    @Override
    public Response productUploaders(MultipartFile file) {
        log.info("Request initiated to upload products");
        UserSession userSession = userCredentialService.getUserSession();
        List<ProductMasterDto> dtoList = productConvertor.convertFileToDto(file);
        dtoList.stream().map(o -> dtoToEntity(o, userSession)).collect(Collectors.toList());
        return Response.builder().status(HttpStatus.OK).code(HttpStatus.OK.value()).message("Upload successful").build();
    }

    public ProductMasterDto dtoToEntity(ProductMasterDto dto, UserSession userSession) {
        try {
            ProductMaster productMaster = new ProductMaster();
            productMaster.setProductName(dto.getProductName());
            productMaster.setProductCode(dto.getProductCode());
            productMaster.setAmount(dto.getAmount());
            productMaster.setOrgId(userSession.getCompany().getCompanyId());
            productMaster.setPartnerReferenceCode(dto.getPartnerRefferenceCode());
            if (StringUtils.hasText(dto.getProductClosingDate()))
                productMaster.setProductClosingDate(DateTimeUtil.stringToDateTime(dto.getProductClosingDate(), DateTimeUtil.D_MMM_YYYY));
            productMaster.setProductGroupId(dto.getProductGroupId());
            productMaster.setProductIdentifierCode(dto.getProductIdentifierCode());
            if (StringUtils.hasText(dto.getProductOpeningDate()))
                productMaster.setProductOpeningDate(DateTimeUtil.stringToDateTime(dto.getProductOpeningDate(), DateTimeUtil.D_MMM_YYYY));
            productMaster.setQuantity(dto.getQuantity());
            productMaster.setStatus(dto.getStatus());
            productMaster.setInsertedOn(LocalDateTime.now());
            productMaster.setInsertedBy(userSession.getSub());
            productMasterRepository.save(productMaster);
        } catch (Exception e) {
            log.error("Exception occurred due to {}", e.getMessage());
            dto.setIsProccesble(false);
        }
        return dto;
    }

}
