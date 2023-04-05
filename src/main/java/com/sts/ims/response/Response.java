package com.sts.ims.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Response {

	private int code;
	private String message;
	private HttpStatus status;
	private Object data;
	private Long count;

	public Response(String message, HttpStatus status) {
		this.message = message;
		this.status = status;
		this.code = status.value();
	}

	public Response(String message, Object data, HttpStatus status) {
		this.message = message;
		this.status = status;
		this.code = status.value();
		this.data = data;
	}
	public Response(String message, Object data, HttpStatus status, Long count) {
		this.message = message;
		this.status = status;
		this.code = status.value();
		this.data = data;
		this.count = count;
	}

}
