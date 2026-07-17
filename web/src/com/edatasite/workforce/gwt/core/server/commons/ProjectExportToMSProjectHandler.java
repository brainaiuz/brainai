package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.workforce.gwt.project.server.actions.ProjectServiceLocal;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.mpx.MPXWriter;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 6/4/12
 * Time: 4:14 PM
 * To change this template use File | Settings | File Templates.
 */

public class ProjectExportToMSProjectHandler implements HttpRequestHandler {

	@Autowired
	private ProjectServiceLocal projectService;

	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("pid") != null && !"".equals(request.getParameter("pid"))) {
			Integer projectID = Integer.valueOf(request.getParameter("pid"));
			ProjectFile projectFile = projectService.exportToMSProject(projectID);
			String fileName = projectFile.getProjectHeader().getName() + ".mpx";
//			ProjectWriter writer = new MSPDIWriter();  //for xml format
            MPXWriter writer = new MPXWriter();// for mpx format
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			writer.write(projectFile, bos);
			byte[] bytes = bos.toByteArray();

			if (fileName.contains(" ")) {
				fileName = fileName.replace(" ", "_");
			}
			if (fileName.contains("/")) {
				fileName = fileName.replace("\\/", "_");
			}
			response.setHeader("content-disposition", "attachment; filename=\"" + fileName + "\"");
            response.setContentType("application/octet-stream");
			response.setCharacterEncoding("UTF8");
			response.setContentLength(bos.size());
			response.getOutputStream().write(bytes);
		}
	}
}
