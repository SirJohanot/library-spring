package com.patiun.libraryspring.utility;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Paginator<T> {

    public int getNumberOfPagesToContainEntities(List<T> entitiesList, int entitiesPerPage) {
        int entitiesNumber = entitiesList.size();
        return entitiesNumber / entitiesPerPage + (entitiesNumber % entitiesPerPage != 0 ? 1 : 0);
    }

    public List<T> getEntitiesOfPage(List<T> entitiesList, int targetPage, int entitiesPerPage) {
        if (entitiesList.isEmpty()) {
            return entitiesList;
        }

        int acceptableTargetPage = getClosestAcceptableTargetPage(entitiesList, targetPage, entitiesPerPage);
        int entitiesNumber = entitiesList.size();

        int firstEntityOfPageIndex = entitiesPerPage * (acceptableTargetPage - 1);

        int lastEntityOfPageIndex = firstEntityOfPageIndex + entitiesPerPage;
        if (lastEntityOfPageIndex > entitiesNumber) {
            lastEntityOfPageIndex = entitiesNumber;
        }

        return entitiesList.subList(firstEntityOfPageIndex, lastEntityOfPageIndex);
    }

    public int getClosestAcceptableTargetPage(List<T> entitiesList, int targetPage, int entitiesPerPage) {
        int maxPage = getNumberOfPagesToContainEntities(entitiesList, entitiesPerPage);
        if (targetPage < 1) {
            return 1;
        } else if (targetPage > maxPage) {
            return maxPage;
        }
        return targetPage;
    }
}
