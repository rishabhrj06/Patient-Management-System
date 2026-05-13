package com.pm.patientservice.kafka;

import com.pm.patientservice.model.Patient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public void sendEvent(Patient patient){
        PatientEvent patientEvent = PatientEvent.newBuilder()
                .setPatientId(patient.getId().toString())
                .setEmail(patient.getEmail())
                .setName(patient.getName())
                .setEventType("PATIENT_CREATED")
                .build();

        try{
            kafkaTemplate.send("patient", patientEvent.toByteArray())
                    .whenComplete((result, ex) -> {
                        if (ex == null){
                            log.info("message sent successfully");
                        }
                        else{
                            log.error("message sent failed: {}", ex.getMessage());
                        }
                    });
        }catch (RuntimeException e){
            log.error("Error sending event to kafka: {}", patientEvent);
        }
    }
}
