package okis.mmpi.lb1.controllers;

/**
* Author: OKis
* Group:  SCS-23m
* Organization: DSTU
* Language: Java
* Language version: 17 (maven project)
* Frameworks: Spring
* Date: 05.01.2024
* Revision:  
*/

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class FilesController {

	@RequestMapping(value = "/files/{file_name}", method = RequestMethod.GET)
	public void getFile(
	    @PathVariable("file_name") String fileName, 
	    HttpServletResponse response) {
	    try {
	      // работа с файлом в виде InputStream
	      InputStream is = new FileInputStream(new File("result.xlsx"));
	      org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
	      response.flushBuffer();
	    } catch (IOException ex) {
	      System.out.println(ex.getMessage());
	    }

	}
}
