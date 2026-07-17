package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.List;

public class MappingResponseDto implements IsSerializable {
    List<MappingDto> mappings = new ArrayList<>();

    public List<MappingDto> getMappings() {
        return mappings;
    }
}
