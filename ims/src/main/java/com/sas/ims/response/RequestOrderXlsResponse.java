package com.sas.ims.response;

import com.poiji.annotation.ExcelCell;
import lombok.Data;

@Data
public class RequestOrderXlsResponse {
    @ExcelCell(0)
    private Long loanId;
    @ExcelCell(1)
    private String loanCode;
    @ExcelCell(2)
    private Long clientId;
    @ExcelCell(3)
    private Long productId;
    @ExcelCell(4)
    private String productCode;
    @ExcelCell(5)
    private Integer branchId;
    @ExcelCell(6)
    private String branchCode;
    @ExcelCell(7)
    private String branchAddress;
    @ExcelCell(8)
    private String customerSupportNumber;
    @ExcelCell(9)
    private String batchId;
    @ExcelCell(10)
    private String errorMessage;
}
