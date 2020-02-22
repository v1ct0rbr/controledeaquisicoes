/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.pb.der.filtros;

/**
 *
 * @author Victor_2
 */
public abstract class AbstractFilter {

    public static final int NEXT = 1;
    public static final int PREVIOUS = 2;
    public static final int FIRST = 3;
    public static final int LAST = 4;

    private long maxResults;
    private long firstResult;
    private long totalRegistros;
    private int currentPage;
    private int numPages;

    public long getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(long maxResults) {
        this.maxResults = maxResults;
    }

    public long getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(long firstResult) {
        this.firstResult = firstResult;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getNumPages() {
        return numPages;
    }

    public void setNumPages(int numPages) {
        this.numPages = numPages;
    }

    public long getTotalRegistros() {
        return totalRegistros;
    }

    public void setTotalRegistros(long totalRegistros) {
        this.totalRegistros = totalRegistros;
    }

    public void changePage(int maxResults, int operacao) {
        this.setMaxResults(maxResults);
        switch (operacao) {
            case NEXT:
                setCurrentPage(currentPage + 1);
                break;
            case PREVIOUS:
                setCurrentPage(currentPage - 1);
                break;
            case FIRST:
                setCurrentPage(0);
                break;
            case LAST:
                setCurrentPage(numPages - 1);
                break;
        }
        setFirstResult(getMaxResults() * (getCurrentPage()));
    }

    @Override
    public String toString() {
        return "AbstractFilter{" + "maxResults=" + maxResults + ", firstResult=" + firstResult + ", totalRegistros=" + totalRegistros + ", currentPage=" + currentPage + ", numPages=" + numPages + '}';
    }

}
