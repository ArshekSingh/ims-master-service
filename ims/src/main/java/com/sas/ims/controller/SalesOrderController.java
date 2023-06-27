package com.sas.ims.controller;

import com.sas.ims.exception.BadRequestException;
import com.sas.ims.exception.ObjectNotFoundException;
import com.sas.ims.request.FilterRequest;
import com.sas.ims.request.SalesOrderRequest;
import com.sas.ims.service.SalesOrderService;
import com.sas.tokenlib.response.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/order/sales")
@CrossOrigin("*")
@Slf4j
@AllArgsConstructor
public class SalesOrderController {
    private final SalesOrderService salesOrderService;

    @PostMapping("/upload")
    public void uploadSalesOrderFile(@RequestPart("file") MultipartFile file, HttpServletResponse httpServletResponse) throws BadRequestException, IOException {
        log.info("Request initiated to upload source file for unity with file name {}", file.getOriginalFilename());
        salesOrderService.uploadSalesOrderFile(file, httpServletResponse);
    }

    @PostMapping("/list")
    public Response getSalesOrderList(@RequestBody FilterRequest filterRequest) throws BadRequestException, IOException {
        return salesOrderService.getSalesOrderList(filterRequest);
    }

    @PostMapping("/create")
    public Response createSalesOrder(@RequestBody SalesOrderRequest salesOrderRequest) throws BadRequestException, ObjectNotFoundException {
        return salesOrderService.createSalesOrder(salesOrderRequest);
    }

    @GetMapping("/{id}")
    public Response getSalesOrderById(@PathVariable Long id) throws BadRequestException, IOException, ObjectNotFoundException {
        return salesOrderService.getSalesOrder(id);
    }

    @PostMapping("/items")
    public Response getSalesOrderItemById(@RequestBody FilterRequest filterRequest) throws BadRequestException, ObjectNotFoundException {
        return salesOrderService.getSalesOrderItems(filterRequest);
    }

    @PostMapping("/download")
    public void downloadSalesOrderItem(@RequestBody FilterRequest request, HttpServletResponse httpServletResponse) throws BadRequestException, IOException {
        log.info("Request initiated to download sales order item details");
        salesOrderService.downloadSalesOrderFile(Long.valueOf(request.getSalesOrderId()), httpServletResponse);
    }
}
