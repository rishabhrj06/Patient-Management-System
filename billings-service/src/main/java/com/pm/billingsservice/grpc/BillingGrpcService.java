package com.pm.billingsservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class BillingGrpcService extends BillingServiceGrpc.BillingServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);

    @Override
    public void createBillingAccount(BillingRequest request, StreamObserver<BillingResponse> responseObserver) {

        log.info("Create Billing Account Request Recieved {}", request.toString());

        BillingResponse response = BillingResponse
                .newBuilder()
                .setAccountId("101")
                .setStatus("Active")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
