package com.sas.ims.dto;

import com.sas.ims.entity.ProductMaster;
import com.sas.ims.entity.RequestOrder;
import lombok.Data;

import java.util.List;

@Data
public class RequestProductMapping {
    private RequestOrder request;
    private List<ProductMaster> products;
}
