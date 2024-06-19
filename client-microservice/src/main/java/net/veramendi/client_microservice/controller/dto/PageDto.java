package net.veramendi.client_microservice.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageDto {

    private Integer size;

    private Long totalElements;

    private Integer totalPages;

    private Integer pageNumber;

    private String orderBy;

    private String orderDir;

    private String keyword;
}
