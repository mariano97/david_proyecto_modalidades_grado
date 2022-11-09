package com.usco.ingenieria.software.pagina.modalidades.grado.service.impl;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.TablaMaestra;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.TablaMaestraRepository;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.TablaMaestraService;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.TablaMaestraDTO;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.mapper.TablaMaestraMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link TablaMaestra}.
 */
@Service
@Transactional
public class TablaMaestraServiceImpl implements TablaMaestraService {

    private final Logger log = LoggerFactory.getLogger(TablaMaestraServiceImpl.class);

    private final TablaMaestraRepository tablaMaestraRepository;

    private final TablaMaestraMapper tablaMaestraMapper;

    public TablaMaestraServiceImpl(TablaMaestraRepository tablaMaestraRepository, TablaMaestraMapper tablaMaestraMapper) {
        this.tablaMaestraRepository = tablaMaestraRepository;
        this.tablaMaestraMapper = tablaMaestraMapper;
    }

    @Override
    public Mono<TablaMaestraDTO> save(TablaMaestraDTO tablaMaestraDTO) {
        log.debug("Request to save TablaMaestra : {}", tablaMaestraDTO);
        return tablaMaestraRepository.save(tablaMaestraMapper.toEntity(tablaMaestraDTO)).map(tablaMaestraMapper::toDto);
    }

    @Override
    public Mono<TablaMaestraDTO> update(TablaMaestraDTO tablaMaestraDTO) {
        log.debug("Request to save TablaMaestra : {}", tablaMaestraDTO);
        return tablaMaestraRepository.save(tablaMaestraMapper.toEntity(tablaMaestraDTO)).map(tablaMaestraMapper::toDto);
    }

    @Override
    public Mono<TablaMaestraDTO> partialUpdate(TablaMaestraDTO tablaMaestraDTO) {
        log.debug("Request to partially update TablaMaestra : {}", tablaMaestraDTO);

        return tablaMaestraRepository
            .findById(tablaMaestraDTO.getId())
            .map(existingTablaMaestra -> {
                tablaMaestraMapper.partialUpdate(existingTablaMaestra, tablaMaestraDTO);

                return existingTablaMaestra;
            })
            .flatMap(tablaMaestraRepository::save)
            .map(tablaMaestraMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<TablaMaestraDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TablaMaestras");
        return tablaMaestraRepository.findAllBy(pageable).map(tablaMaestraMapper::toDto);
    }

    public Mono<Long> countAll() {
        return tablaMaestraRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<TablaMaestraDTO> findOne(Long id) {
        log.debug("Request to get TablaMaestra : {}", id);
        return tablaMaestraRepository.findById(id).map(tablaMaestraMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete TablaMaestra : {}", id);
        return tablaMaestraRepository.deleteById(id);
    }
}
