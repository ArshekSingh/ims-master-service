package com.sas.ims.assembler;

import com.sas.ims.dto.ProductGroupDto;
import com.sas.ims.entity.ProductGroup;
import org.springframework.stereotype.Component;

@Component
public class ProductGroupAssembler {
    public ProductGroup dtoToEntity(ProductGroupDto dto, ProductGroup productGroup) {
        productGroup.setPartnerGroupId(dto.getPartnerGroupId());
        productGroup.setGroupCode(dto.getGroupCode());
        productGroup.setGroupName(dto.getGroupName());
        productGroup.setGroupType(dto.getGroupType());
        productGroup.setStatus(dto.getStatus());
        return productGroup;

    }
}
