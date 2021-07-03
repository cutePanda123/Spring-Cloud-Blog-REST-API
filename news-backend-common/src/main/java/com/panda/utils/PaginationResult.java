package com.panda.utils;

import lombok.Data;

import java.util.List;

@Data
public class PaginationResult {
    private int page; // current page index
    private long total; // total page number
    private long records; // total records number
    private List<?> rows; // records data content
}
