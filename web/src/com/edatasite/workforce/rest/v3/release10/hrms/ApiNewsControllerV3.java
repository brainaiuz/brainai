package com.edatasite.workforce.rest.v3.release10.hrms;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.db.NewsManager;
import com.edatasite.workforce.gwt.news.client.rpc.NewsService;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.helper.ListingFilterHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.to.ListParamsDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.edatasite.workforce.rest.v3.release10.core.to.hrms.NewsDTO;
import com.edatasite.workforce.rest.v3.release10.hrms.service.ApiNewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.edatasite.workforce.rest.base.helpers.ApiConstants.GENERAL_ERROR_MESSAGE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.NOT_FOUND;

/**
 * User : Akhror on 9/02/2021
 */
@Tag(name = "News", description = "News Public API")
@RestController
@RequestMapping(value = "/news", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ApiNewsControllerV3 {

    private static final Logger log = LoggerFactory.getLogger(ApiNewsControllerV3.class);

    @Autowired
    private ApiNewsService apiNewsService;
    @Autowired
    private NewsManager newsManager;
    @Autowired
    private NewsService newsService;

    @Operation(summary = "Get News list")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "News"))
    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<ListResultTO<NewsDTO>> getNews(@RequestBody ListParamsDTO params) {
        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(params, ListPanelType.NewsListPanel);

        return ResultTO.success(apiNewsService.getNewsList(fp));
    }

    @Operation(summary = "Get existing news by id")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "news"))
    @RequestMapping(path = "/{newsId}", method = RequestMethod.GET)
    public ResultTO<NewsDTO> getNewsById(@PathVariable final Integer newsId) throws RestException {

        return ResultTO.success(apiNewsService.getNewsById(newsId));
    }

    @Operation(summary = "Create news")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "News"))
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultTO<Integer> createNews(@RequestBody NewsDTO newsDTO) throws RestException {
        if (newsDTO.getId() != null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "News ID is specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }

        Integer id = apiNewsService.save(newsDTO, true);
        return ResultTO.success(id);
    }

    @Operation(summary = "Edit news")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "News"))
    @RequestMapping(method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<Integer> updateNews(@RequestBody NewsDTO newsDTO) throws RestException {
        if (newsDTO.getId() == null || newsDTO.getId() < 1) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "News ID is not specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }

        Integer id = apiNewsService.save(newsDTO, false);
        return ResultTO.success(id);
    }

    @Operation(summary = "Patch Update existing news")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "News"))
    @RequestMapping(method = RequestMethod.PATCH, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<Integer> patchUpdateNews(@RequestBody NewsDTO newsDTO) throws RestException {
        if (newsDTO.getId() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "News ID is not specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }

        Integer id = apiNewsService.savePatch(newsDTO);
        return ResultTO.success(id);
    }

    @Operation(summary = "Delete existing news by id")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "News"))
    @RequestMapping(path = "/{newsId}", method = RequestMethod.DELETE)
    public Object deleteNews(@PathVariable final Integer newsId) throws RestException {
        Optional.ofNullable(newsManager.get(newsId)).orElseThrow(() -> new RestException(GENERAL_ERROR_MESSAGE, "Event with this id is not found", NOT_FOUND, HttpStatus.BAD_REQUEST));

        newsService.deleteNews(newsId);
        return ResultTO.success();
    }
}
