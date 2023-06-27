package com.sas.ims.request;

import lombok.Data;

@Data
public class RequestOrderUpdateMessage {
    private Long loanId;
    private Long status;
}
