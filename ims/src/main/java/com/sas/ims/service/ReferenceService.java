package com.sas.ims.service;


import com.sas.ims.exception.ObjectNotFoundException;
import com.sas.ims.request.ReferenceDomainRequest;
import com.sas.ims.request.ReferenceRequest;
import com.sas.tokenlib.response.Response;

public interface ReferenceService {

    Response getReferenceByKey(String referenceKey);

    Response findByReferenceDomain(String domain) throws ObjectNotFoundException;

    Response addReferenceDetails(ReferenceRequest request) throws ObjectNotFoundException;

    Response updateReferenceDetail(ReferenceRequest request) throws ObjectNotFoundException;

    Response getReferenceDetailsList(ReferenceDomainRequest referenceDomain);
}