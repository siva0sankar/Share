package com.share;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.net.aso.b;

public class FileWalker {

	public PrintWriter out;

	public HttpServletRequest request;

	public HttpServletResponse response;
	
	public ServletContext context;

	public FileWalker(PrintWriter out, HttpServletRequest request, HttpServletResponse response, ServletContext context) {
		this.request = request;
		this.response = response;
		this.out = out;
		this.context = context;
	}

	public void generateLink() throws IOException {
		String path = request.getRequestURL().toString().substring(request.getRequestURL().indexOf("/SankarShare"));
		String nonRootPath = path.substring("/SankarShare".length());
		String decodePath = URLDecoder.decode(nonRootPath,"UTF-8");
		if(nonRootPath.length() > 0){
			File file = new File("d:/" + decodePath);
			if(file.isFile()){
				String mimeType = context.getMimeType(file.getAbsolutePath());
				if(mimeType == null){
					mimeType = "application/octet-stream";
				}
				response.setContentType(mimeType);
				response.setHeader("Content-Disposition", "attachment;filename=\""+file.getName()+"\"");
				downloadFile("d:" + decodePath);
			}else{
//				out = response.getWriter();
				filePath("d:/" + decodePath);
			}
		}else{
//			out = response.getWriter();
			filePath("d:/");
		}

	}

	public void filePath(String path){
		File file = new File(path);
		File[] list = file.listFiles();

		if(list == null){
			return;
		}
		out.write("<html><head><title>Sankar Share</title></head><body><ul>");
		for (File f : list) {
//			if(f.isDirectory()){
				out.write("<li><a href='" + request.getRequestURL().toString() +"/"+ f.getName()+"'>" + f.getName() + "</a></li>");
//			}else{
				
//			}

		}
		out.write("</ul></body></html>");
	}

	public void downloadFile(String path) throws IOException{
		
		FileInputStream fileInputStream = new FileInputStream(path);  
		int i;   
		while ((i=fileInputStream.read()) > 0) {  
			out.write(i);   
		}   
		fileInputStream.close();
		out.flush();
	}
}
