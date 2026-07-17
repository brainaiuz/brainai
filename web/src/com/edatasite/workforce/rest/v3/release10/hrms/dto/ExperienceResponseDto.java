package com.edatasite.workforce.rest.v3.release10.hrms.dto;

import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;

public class ExperienceResponseDto {
    private ResultTO<ResultDataDto> result;

    public ResultTO<ResultDataDto> getResult() {
        return result;
    }

    public void setResult(ResultTO<ResultDataDto> result) {
        this.result = result;
    }
}
