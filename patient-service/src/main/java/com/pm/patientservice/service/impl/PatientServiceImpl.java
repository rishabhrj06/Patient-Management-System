package com.pm.patientservice.service.impl;

import billing.BillingResponse;
import com.pm.patientservice.dto.PatientRequestDto;
import com.pm.patientservice.dto.PatientResponseDto;
import com.pm.patientservice.exception.EmailAlreadyExistsException;
import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.grpc.BillingGrpcServiceClient;
//import com.pm.patientservice.kafka.KafkaProducer;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import com.pm.patientservice.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    private final BillingGrpcServiceClient billingGrpcServiceClient;

//    private final KafkaProducer kafkaProducer;

    public List<PatientResponseDto> getPatient(){
        List<Patient> patientList = patientRepository.findAll();
        return patientList.
                stream()
                .map(PatientMapper::toDto)
                .toList();
    }

    @Override
    public PatientResponseDto createPatient(PatientRequestDto patientRequestDto) {
        boolean exist = patientRepository.existsByEmail(patientRequestDto.getEmail());
        log.info("exist {}", exist);
        if(exist){
            log.warn("Email already exists: {}, ye bhi chal raha", patientRequestDto.getEmail());
            throw new EmailAlreadyExistsException("Email already exists: "+ patientRequestDto.getEmail());
        }
            log.info("ye bhi cha raha");
            Patient patient = patientRepository.save(PatientMapper.toPatient(patientRequestDto));

            billingGrpcServiceClient.createBillingAccount(String.valueOf(patient.getId()), patient.getName(), patient.getEmail());
//        kafkaProducer.sendMessage(patient);
            return PatientMapper.toDto(patient);

    }

    @Override
    public PatientResponseDto updatePatient(UUID id, PatientRequestDto patientRequestDto) {
        Patient patient  = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with id : " + id));

        patient.setName(patientRequestDto.getName());
        patient.setAddress(patientRequestDto.getAddress());
        patient.setEmail(patientRequestDto.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDto.getDateOfBirth()));

        patientRepository.save(patient);

        return PatientMapper.toDto(patient);
    }

    @Override
    public void deletePatient(UUID id) {
        if (!patientRepository.existsById(id)) {
            throw new PatientNotFoundException("Patient not found with id : " + id);

        }
        patientRepository.deleteById(id);
    }
}
