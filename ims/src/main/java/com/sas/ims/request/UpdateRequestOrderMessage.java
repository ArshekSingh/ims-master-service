package com.sas.ims.request;

import lombok.Data;

@Data
public class UpdateRequestOrderMessage {
    private Long loanId;
    private Long status;
    private Long xSellId;
}