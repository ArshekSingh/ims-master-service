package com.sas.ims.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReferenceRequest {

    private List<ReferenceDetailDto> referenceDetailDto;
}
