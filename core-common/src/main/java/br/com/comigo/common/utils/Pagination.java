package br.com.comigo.common.utils;

import java.util.List;

public class Pagination<T> {
  private List<T> content;
  private int currentPage;
  private int pageSize;
  private long totalItems;
  private int totalPages;

  public Pagination(List<T> content, int currentPage, int pageSize, long totalItems) {
      this.content = content;
      this.currentPage = currentPage;
      this.pageSize = pageSize;
      this.totalItems = totalItems;
      this.totalPages = (int) Math.ceil((double) totalItems / pageSize);
  }

  public List<T> getContent() {
    return this.content;
  }

  public void setContent(List<T> content) {
    this.content = content;
  }

  public int getCurrentPage() {
    return this.currentPage;
  }

  public void setCurrentPage(int currentPage) {
    this.currentPage = currentPage;
  }

  public int getPageSize() {
    return this.pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public long getTotalItems() {
    return this.totalItems;
  }

  public void setTotalItems(long totalItems) {
    this.totalItems = totalItems;
  }

  public int getTotalPages() {
    return this.totalPages;
  }

  public void setTotalPages(int totalPages) {
    this.totalPages = totalPages;
  }

}