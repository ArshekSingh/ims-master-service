package com.sas.ims.assembler;

import com.sas.ims.response.RequestOrderXlsResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RequestOrderAssembler {

    public List<List<String>> prepareRequestOrderErrorData(List<RequestOrderXlsResponse> data) {
        return data.stream().map(this::constructData).collect(Collectors.toList());
    }

    private List<String> constructData(RequestOrderXlsResponse uploader) {
        List<String> data = new ArrayList<>();
        data.add(uploader.getLoanId() != null ? String.valueOf(uploader.getLoanId()) : "");
        data.add(StringUtils.hasText(uploader.getLoanCode()) ? uploader.getLoanCode() : "");
        data.add(uploader.getClientId() != null ? String.valueOf(uploader.getClientId()) : "");
        data.add(uploader.getProductId() != null ? String.valueOf(uploader.getProductId()) : "");
        data.add(StringUtils.hasText(uploader.getProductCode()) ? uploader.getProductCode() : "");
        data.add(uploader.getBranchId() != null ? String.valueOf(uploader.getBranchId()) : "");
        data.add(StringUtils.hasText(uploader.getBranchCode()) ? uploader.getBranchCode() : "");
        data.add(uploader.getBranchAddress() != null ? String.valueOf(uploader.getBranchAddress()) : "");
        data.add(StringUtils.hasText(uploader.getCustomerSupportNumber()) ? uploader.getCustomerSupportNumber() : "");
        data.add(StringUtils.hasText(uploader.getBatchId()) ? uploader.getBatchId() : "");
        data.add(StringUtils.hasText(uploader.getErrorMessage()) ? uploader.getErrorMessage() : "");
        return data;
    }
}
