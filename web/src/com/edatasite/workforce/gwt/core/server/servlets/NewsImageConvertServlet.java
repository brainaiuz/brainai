package com.edatasite.workforce.gwt.core.server.servlets;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.config.datasource.TenantContextHolder;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsNews;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.NewsManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.documents.client.exceptions.DuplicateNameException;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.utils.EdsContextParams;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.F_NEWS;

@Transactional
public class NewsImageConvertServlet implements HttpRequestHandler {

    @Autowired
    private NewsManager newsManager;
    @Autowired
    private CoreService coreService;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Override
    @Transactional
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String schemaType = request.getParameter("schema");
        if (schemaType.equals("free")) {
            ServerSecurityContext.getInstance().setDatabase(TenantContextHolder.FREE_DB);
        } else if (schemaType.equals("paid")) {
            ServerSecurityContext.getInstance().setDatabase(TenantContextHolder.PAID_DB);
        }
        CompanyManager companyManager = (CompanyManager) ApplicationContextProvider.applicationContext.getBean("companyManager");
        List<EdsCompany> companies = companyManager.getCompanies();
        for (EdsCompany company : companies) {
            ServerSecurityContext.getInstance().setCompanyId(company.getObjectID());
            List<EdsNews> newsList = newsManager.getCompanyNews(new ListingFilterParameter());

            if (newsList != null && newsList.size() > 0) {
                List<Integer> ids = newsList.stream().map(doc -> Objects.requireNonNull(doc.getObjectID())).collect(Collectors.toList());
                if (ids.size() > 0) {
                    for (Integer id : ids) {
                        EdsNews news = newsManager.get(id);
                        if (news.getImage() != null) {
                            ArrayList<FileResource> files = new ArrayList<>();
                            FolderResource folder = coreService.getFolderResource(F_NEWS, news.getObjectID());
                            if (folder == null) {
                                Locale locale = ServerUtils.getUserLocale();
                                String systemFolderName = messageSource.getMessage("createSystemFolders.hrmsNewsName", null, "System Folder", locale);
                                try {
                                    folder = coreService.createFolder(null, systemFolderName);
                                } catch (InsufficientPermissionsException e) {
                                    throw new RuntimeException(e);
                                } catch (DuplicateNameException e) {
                                    throw new RuntimeException(e);
                                } catch (ObjectNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            FileResource file = new FileResource();
                            file.setUploadType(EdsContextParams.getUploadType());
                            file.setName(news.getImage().getOriginalName());
                            file.setEntityID(news.getImage().getObjectID());
                            files.add(file);
                            coreService.saveXhrFile(files, folder, "");

                            FileItem[] items = new FileItem[1];
                            FileItem item = new FileItem();
                            item.setUploadType(EdsContextParams.getUploadType());
                            item.setFileName(news.getImage().getOriginalName());
                            item.setId(news.getImage().getObjectID());
                            item.setDescription("");
                            items[0] = item;
                            attachmentUtilsManager.saveAttachments(F_NEWS, id, id, items);

                            System.out.println("<---------------------Successfully converted news images in company: " + company.getObjectID() + "----------------------------------->");
                        }
                    }
                }
            }
        }
    }
}
