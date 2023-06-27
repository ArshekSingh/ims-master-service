package com.sas.ims.controller;

import com.sas.ims.exception.BadRequestException;
import com.sas.ims.exception.ObjectNotFoundException;
import com.sas.ims.request.FilterRequest;
import com.sas.ims.service.RequestOrderService;
import com.sas.tokenlib.response.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.IOException;

@RestController
@RequestMapping("/api/order/request")
@CrossOrigin("*")
@Slf4j
@AllArgsConstructor
public class RequestOrderController {
    private final RequestOrderService requestOrderService;

    @PostMapping("/upload")
    public void uploadRequestOrderFile(@RequestPart("file") MultipartFile file, HttpServletResponse httpServletResponse) throws BadRequestException, IOException {
        log.info("Request initiated to upload source file for unity with file name {}", file.getOriginalFilename());
        requestOrderService.uploadRequestOrderFile(file, httpServletResponse);
    }

    @PostMapping("/list")
    public Response getRequestOrderList(@RequestBody FilterRequest filterRequest) throws BadRequestException, IOException {
        return requestOrderService.getRequestOrderList(filterRequest);
    }


    @GetMapping("/{id}")
    public Response getRequestOrderById(@PathVariable Long id) throws BadRequestException, IOException, ObjectNotFoundException {
        return requestOrderService.getRequestOrder(id);
    }

}
