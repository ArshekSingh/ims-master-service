package com.sas.ims.entity;

import com.sas.ims.exception.ObjectNotFoundException;
import com.sas.ims.request.ReferenceDomainRequest;
import com.sas.ims.request.ReferenceRequest;
import com.sas.ims.service.ReferenceService;
import com.sas.tokenlib.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/reference")
public class ReferenceController {

    private final ReferenceService referenceService;

    @Autowired
    public ReferenceController(ReferenceService referenceService) {
        this.referenceService = referenceService;
    }

    @GetMapping(value = "/{referenceKey}")
    public Response getReferenceByKey(@PathVariable String referenceKey) {
        return referenceService.getReferenceByKey(referenceKey);
    }

    @GetMapping(value = "/referenceDomain/{domain}")
    public Response findByReferenceDomain(@PathVariable String domain) throws ObjectNotFoundException {
        return referenceService.findByReferenceDomain(domain);
    }

    @PostMapping
    public Response addReferenceDetails(@RequestBody ReferenceRequest request) throws ObjectNotFoundException {
        return referenceService.addReferenceDetails(request);
    }

    @PostMapping(value = "/update")
    public Response updateReferenceDetails(@RequestBody ReferenceRequest request) throws ObjectNotFoundException {
        return referenceService.updateReferenceDetail(request);
    }

    @PostMapping("/list")
    public Response getReferenceDetailsList(@RequestBody ReferenceDomainRequest referenceDomainRequest) {
        return referenceService.getReferenceDetailsList(referenceDomainRequest);
    }
}
