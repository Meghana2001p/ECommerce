package com.project.E_Commerce.ServiceImplementation.Return;
import com.project.E_Commerce.Entity.Order.Order;
import com.project.E_Commerce.Entity.Order.OrderItem;
import com.project.E_Commerce.Entity.Product.Inventory;
import com.project.E_Commerce.Entity.Return.ReturnRequest;
import com.project.E_Commerce.Repository.Order.OrderItemRepo;
import com.project.E_Commerce.Repository.Order.OrderRepo;
import com.project.E_Commerce.Repository.Product.InventoryRepo;
import com.project.E_Commerce.Repository.Return.ReturnRequestRepo;
import com.project.E_Commerce.Service.Return.ReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReturnServiceImpl implements ReturnService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ReturnRequestRepo returnRequestRepo;

    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private InventoryRepo inventoryRepo;

    public String  requestReturn(Integer orderId, String reason)
    {

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));


        if (order.getOrderStatus() != Order.OrderStatus.DELIVERED) {
            return "Return not allowed. Order is not delivered yet.";
        }
        ReturnRequest returnRequest = new ReturnRequest();
        returnRequest.setOrderItem(order);
        returnRequest.setReason(reason);
        returnRequest.setStatus(ReturnRequest.ReturnStatus.REQUESTED);
        returnRequestRepo.save(returnRequest);

        return "Return request created successfully for Order ID: " + orderId;

    }


    public String approveReturnByDeliveryPerson(Integer returnRequestId) {
        ReturnRequest returnRequest = returnRequestRepo.findById(returnRequestId)
                .orElseThrow(() -> new RuntimeException("Return request not found"));

        if (returnRequest.getStatus() != ReturnRequest.ReturnStatus.REQUESTED) {
            return "Return request cannot be approved. Current status: " + returnRequest.getStatus();
        }
        returnRequest.setStatus(ReturnRequest.ReturnStatus.APPROVED);
        returnRequestRepo.save(returnRequest);

        return "Return request approved by delivery person for ReturnRequest ID: " + returnRequestId;
    }

    public String markPickedUp(Integer returnRequestId) {
        ReturnRequest returnRequest = returnRequestRepo.findById(returnRequestId)
                .orElseThrow(() -> new RuntimeException("Return request not found"));

        if (returnRequest.getStatus() != ReturnRequest.ReturnStatus.APPROVED) {
            return "Return request cannot be marked as picked up. Current status: " + returnRequest.getStatus();
        }

        returnRequest.setStatus(ReturnRequest.ReturnStatus.PICKED_UP);
        returnRequestRepo.save(returnRequest);

        List<OrderItem> orderItems = orderItemRepo.findByOrderId(returnRequest.getOrderItem().getId());

        for (OrderItem item : orderItems) {
            Inventory inventory = inventoryRepo.findByProductId(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Inventory not found for product ID: " + item.getProduct().getId()));

            inventory.setStockQuantity(inventory.getStockQuantity() + item.getQuantity());
            inventoryRepo.save(inventory);
        }

        return "Return request marked as PICKED_UP and inventory updated for ReturnRequest ID: " + returnRequestId;
    }




}
