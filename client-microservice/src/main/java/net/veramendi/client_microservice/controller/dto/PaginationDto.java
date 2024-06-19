package net.veramendi.client_microservice.controller.dto;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaginationDto {

    private PageDto page;

    private List content;
}
