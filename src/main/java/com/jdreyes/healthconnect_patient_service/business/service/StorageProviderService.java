package com.jdreyes.healthconnect_patient_service.business.service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jdreyes.healthconnect_patient_service.business.dto.StorageProviderDto;
import com.jdreyes.healthconnect_patient_service.model.entity.StorageProvider;
import com.jdreyes.healthconnect_patient_service.repository.StorageRepository;

@Service
public class StorageProviderService {
    private final StorageRepository repository;

    @Autowired
    public StorageProviderService(StorageRepository repository) {
        this.repository = repository;
    }


    public Optional<StorageProvider> save(StorageProviderDto dto){
        StorageProvider provider = new StorageProvider();
        provider.setName(dto.getName());
        provider.setPath(dto.getPath());
        provider.setIsDefault(dto.getIsDefault());
        provider.setCredentials(dto.getCredentials());
        provider.setActive(true);

        if (dto.getIsDefault() && repository.existsByIsDefaultAndActive(true, true)){
            List<StorageProvider> list = repository.findByIsDefaultAndActive(true, true);
            list.forEach(e -> e.setIsDefault(false));
            repository.saveAll(list);
        }
        return Optional.of(repository.save(provider));
    }

    public Optional<StorageProvider> update(String uuid, StorageProviderDto dto){
        var provider = repository.findById(UUID.fromString(uuid)).orElseThrow(() -> new InvalidParameterException("Provider storage uuid was not found"));
        provider.setName(dto.getName());
        provider.setPath(dto.getPath());
        provider.setIsDefault(dto.getIsDefault());
        provider.setCredentials(dto.getCredentials());
        provider.setActive(true);

        if (dto.getIsDefault() && repository.existsByIsDefaultAndActive(true, true)){
            List<StorageProvider> list = repository.findByIsDefaultAndActive(true, true);
            list.forEach(e -> e.setIsDefault(false));
            repository.saveAll(list);
        }
        return Optional.of(repository.save(provider));
    }

    public List<StorageProvider> findAll(){
        return repository.findByActive(true);
    }
}
