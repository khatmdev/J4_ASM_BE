package org.example.java4_asm_backend.dto;

import java.util.List;

public class PaginatedResponse<T> {
    private List<T> list;
    private int currentPage;
    private int totalPages;

    public PaginatedResponse(List<T> list, int currentPage, int totalPages) {
        this.list = list;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }

    // Getters and setters
    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
