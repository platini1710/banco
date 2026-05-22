package com.bci.tareas.dto;

import com.bci.tareas.helper.ErrorResp;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Getter
@Setter
@EqualsAndHashCode()
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespuestaDTO {

	private Long id;
	private String msg;
	private String name;
	private String password;
	private String email;
	private String created;
	private ErrorResp errorResp;
	private String lastLogin;
	private String token;
	private String isActive;
	private PhoneDTO phone;

}