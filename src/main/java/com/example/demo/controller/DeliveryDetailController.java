package com.example.demo.controller;

import com.example.demo.model.DeliveryDetail;
import com.example.demo.service.DeliveryDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class DeliveryDetailController {

    @Autowired
    private DeliveryDetailService deliveryDetailService;


    @GetMapping("/deliverydetails/order/{orderId}")
    public ResponseEntity<DeliveryDetail> getDeliveryDetailByOrderId(@PathVariable Integer orderId) {
        DeliveryDetail deliveryDetail = deliveryDetailService.getDeliveryDetailByOrderId(orderId);
        if (deliveryDetail != null) {
            return ResponseEntity.ok(deliveryDetail);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/deliverydetails")
    public ResponseEntity<List<DeliveryDetail>> getAllDeliveryDetails() {
        List<DeliveryDetail> deliveryDetails = deliveryDetailService.getAllDeliveryDetails();
        return ResponseEntity.ok(deliveryDetails);
    }

    @GetMapping("/deliverydetails/{deliveryId}")
    public ResponseEntity<DeliveryDetail> getDeliveryDetail(@PathVariable Integer deliveryId) {
        DeliveryDetail deliveryDetail = deliveryDetailService.getDeliveryDetailById(deliveryId);
        if (deliveryDetail != null) {
            return ResponseEntity.ok(deliveryDetail);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/deliverydetails/create")
    public ResponseEntity<DeliveryDetail> createDeliveryDetail(@RequestBody DeliveryDetail deliveryDetail) {
        deliveryDetailService.createDeliveryDetail(deliveryDetail);
        return ResponseEntity.status(HttpStatus.CREATED).body(deliveryDetail);
    }

    @PutMapping("/put/{deliveryId}")
    public ResponseEntity<DeliveryDetail> updateDeliveryDetail(@PathVariable Integer deliveryId, @RequestBody DeliveryDetail deliveryDetail) {
        DeliveryDetail existingDeliveryDetail = deliveryDetailService.getDeliveryDetailById(deliveryId);
        if (existingDeliveryDetail != null) {
            deliveryDetail.setDeliveryID(deliveryId);
            deliveryDetailService.updateDeliveryDetail(deliveryId, deliveryDetail);
            return ResponseEntity.ok(deliveryDetail);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{deliveryId}")
    public ResponseEntity<Void> deleteDeliveryDetail(@PathVariable Integer deliveryId) {
        deliveryDetailService.deleteDeliveryDetail(deliveryId);
        return ResponseEntity.noContent().build();
    }
}