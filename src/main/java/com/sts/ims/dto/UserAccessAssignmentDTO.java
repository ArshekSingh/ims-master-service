package com.sas.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class UserAccessAssignmentDTO {
	
	Long userId;
	
	List<AccessGroupDTO> assignedDTOs = new ArrayList<>();

	List<AccessGroupDTO> unAssignedDTOs = new ArrayList<>();
}
