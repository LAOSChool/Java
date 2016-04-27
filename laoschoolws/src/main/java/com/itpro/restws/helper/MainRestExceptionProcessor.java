package com.itpro.restws.helper;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MainRestExceptionProcessor {

//	@Autowired
//	private ResourceBundleMessageSource messageSource;

	Locale msgLocate = Locale.US;

	// Hand all UnknownMatchException
	@ExceptionHandler(ESchoolException.class)
	public ResponseEntity<RespInfo> handleUnknowException(HttpServletRequest req, ESchoolException ex) {

		String url = req.getRequestURL().toString();
		HttpStatus httpSts = ex.getHttpSts();
		RespInfo errorInfo = new RespInfo(httpSts.value(), httpSts.getReasonPhrase(), url,ex.getError_msg());
		ResponseEntity<RespInfo> response = new ResponseEntity<RespInfo>(errorInfo, httpSts);

		return response;
	}

	// // Hand all UnknownMatchException
	 @ExceptionHandler(RuntimeException.class)
		public ResponseEntity<RespInfo> unknownErrorHandle(HttpServletRequest req, RuntimeException ex) {
		 //String devMsg =  messageSource.getMessage("error.unknow.error", null, msgLocate);
		String url = req.getRequestURL().toString();
		HttpStatus httpSts = HttpStatus.INTERNAL_SERVER_ERROR;
		RespInfo errorInfo = new RespInfo(httpSts.value(), httpSts.getReasonPhrase(), url,ex.getMessage());
		
		ResponseEntity<RespInfo> response = new ResponseEntity<RespInfo>(errorInfo, httpSts);

		return response;
	 }

}
