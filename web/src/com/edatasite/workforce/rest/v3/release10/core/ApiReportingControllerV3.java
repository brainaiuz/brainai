package com.edatasite.workforce.rest.v3.release10.core;


import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.SelectListRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
import com.edatasite.workforce.gwt.reportingsystem.server.ReportingSerivceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v3.release10.core.helper.ListingFilterHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.to.ListParamsDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Tag(name = "Reporting", description = "Reporting Public API")
@RestController
@RequestMapping(value = "/report", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ApiReportingControllerV3 {

    private final ReportingService reportingService;
    private final ReportingSerivceLocal reportingSerivceLocal;

    @Autowired
    public ApiReportingControllerV3(ReportingService reportingService, ReportingSerivceLocal reportingSerivceLocal) {
        this.reportingService = reportingService;
        this.reportingSerivceLocal = reportingSerivceLocal;
    }


    @Operation(summary = "Get Report List")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "ReportList"))
    @RequestMapping(path = "/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<ListResult<SelectListRpc>> getListByParentCode(@RequestBody ListParamsDTO params) {
        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(params, ListPanelType.ReportsListPanel);
        fp.setCategoryID(0);
        return ResultTO.success(reportingSerivceLocal.getReports(fp));
    }

    @Operation(summary = "Get report file")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "ReportFile"))
    @RequestMapping(path = "/download_file/{reportId}", method = RequestMethod.GET)
    public byte[] getReportFile(@PathVariable Integer reportId, @RequestParam String fileType, HttpServletResponse response) throws IOException {
        byte[] bytes = reportingSerivceLocal.getReportFile(reportId, fileType);

        ServletOutputStream outputStream = response.getOutputStream();
        switch (fileType) {
            case "PDF" -> response.setContentType("application/pdf");
            case "CSV" -> response.setContentType("text/csv");
            default -> response.setContentType("application/vnd.ms-excel");
        }
        response.setHeader("Content-Disposition", "filename=");
        outputStream.write(bytes, 0, bytes.length);

        outputStream.close();

//        }
        return bytes;
    }

}
