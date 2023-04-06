package com.sts.ims.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilterRequest {
    private Long branchId;
    private Long orgId;
    private Integer startIndex;
    private Integer endIndex;
}