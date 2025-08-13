package com.project.E_Commerce.Service.Return;


public interface ReturnService {
     String  requestReturn(Integer orderId, String reason);
     String approveReturnByDeliveryPerson(Integer returnRequestId);
     String markPickedUp(Integer returnRequestId);
}
