package com.patiun.libraryspring.order;

import com.patiun.libraryspring.exception.ServiceException;

import java.util.List;

public interface BookOrderService {

    void createOrder(Integer bookId, Integer userId, RentalType type, Integer days) throws ServiceException;

    List<BookOrder> getAllOrders();

    List<BookOrder> getOrdersOfUser(Integer userId);

    BookOrder getOrderById(Integer id);

    void approveOrderById(Integer id) throws ServiceException;

    void declineOrderById(Integer id) throws ServiceException;

    void collectOrderById(Integer id) throws ServiceException;

    void returnOrderById(Integer id) throws ServiceException;
}
