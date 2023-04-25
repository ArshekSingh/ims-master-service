package com.sas.ims.controller;

import com.sas.ims.dto.ProductsToAreaMappingDto;
import com.sas.ims.exception.BadRequestException;
import com.sas.ims.dto.AreaMasterDto;
import com.sas.ims.exception.InternalServerErrorException;
import com.sas.ims.exception.ObjectNotFoundException;
import com.sas.ims.request.FilterRequest;
import com.sas.ims.service.AreaService;
import com.sas.tokenlib.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/area")
@CrossOrigin(origins = "*")
@Slf4j
public class AreaController {

    @Autowired
    private AreaService areaService;

    @GetMapping(value = "/{areaId}")
    public Response getAreaDetail(@PathVariable Long areaId) throws ObjectNotFoundException {
        return areaService.getAreaDetail(areaId);
    }

    @PostMapping("/list")
    public Response getAreaDetailList(@RequestBody FilterRequest filterRequest) throws BadRequestException, ObjectNotFoundException {
        return areaService.getAreaDetailList(filterRequest);
    }

    @PostMapping("/add")
    public Response addArea(@RequestBody AreaMasterDto areaMasterDto) throws ObjectNotFoundException, BadRequestException {
        log.info("Request received to add area");
        return areaService.addArea(areaMasterDto);
    }

    @PostMapping("/update")
    public Response updateBranch(@RequestBody AreaMasterDto areaMasterDto) throws ObjectNotFoundException, BadRequestException {
        return areaService.updateArea(areaMasterDto);
    }

    @GetMapping("/areaTypeMap")
    public Response getAreaTypeMap() throws ObjectNotFoundException, InternalServerErrorException {
        return areaService.getAreaTypeMap();
    }

    @GetMapping("/parentAreaCodeMap/{areaType}")
    public Response parentAreaCodeMap(@PathVariable String areaType) throws InternalServerErrorException {
        log.info("Request initiated for Parent Area Code Map by area type : {}", areaType);
        return areaService.parentAreaCodeMap(areaType);
    }

    @GetMapping("/areaCodeMap/{areaType}")
    public Response areaCodeMap(@PathVariable String areaType) throws InternalServerErrorException {
        log.info("Request initiated for Area Code Map by area type : {}", areaType);
        return areaService.areaCodeMap(areaType);
    }

    @GetMapping(value = "getProductListAssignedToArea/{areaId}")
    public Response getProductListAssignedToBranch(@PathVariable Long areaId) {
        log.info("getProductListAssignedToBranch() invoked for areaId : {}", areaId);
        return areaService.getProductsAssignedOrAvailableToArea(areaId);
    }

    @PostMapping(value = "assignAreaToProductList")
    public Response assignBranchToProductList(@RequestBody ProductsToAreaMappingDto productsToAreaMappingDto) throws BadRequestException {
        if (productsToAreaMappingDto.isValidRequest(productsToAreaMappingDto)) {
            log.info("assignAreaToProductList() invoked for areaId : {}", productsToAreaMappingDto.getAreaId());
            return areaService.assignProductsToBranch(productsToAreaMappingDto);
        }
        log.error("productsToAreaMappingDto failed validation check at assignAreaToProductList() ,areaId : {}", productsToAreaMappingDto.getAreaId());
        throw new BadRequestException("Pass all required attributes", HttpStatus.BAD_REQUEST);
    }

}
