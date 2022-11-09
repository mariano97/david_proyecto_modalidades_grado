package com.usco.ingenieria.software.pagina.modalidades.grado.service.impl;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Observaciones;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.ObservacionesRepository;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.ObservacionesService;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.ObservacionesDTO;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.mapper.ObservacionesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Observaciones}.
 */
@Service
@Transactional
public class ObservacionesServiceImpl implements ObservacionesService {

    private final Logger log = LoggerFactory.getLogger(ObservacionesServiceImpl.class);

    private final ObservacionesRepository observacionesRepository;

    private final ObservacionesMapper observacionesMapper;

    public ObservacionesServiceImpl(ObservacionesRepository observacionesRepository, ObservacionesMapper observacionesMapper) {
        this.observacionesRepository = observacionesRepository;
        this.observacionesMapper = observacionesMapper;
    }

    @Override
    public Mono<ObservacionesDTO> save(ObservacionesDTO observacionesDTO) {
        log.debug("Request to save Observaciones : {}", observacionesDTO);
        return observacionesRepository.save(observacionesMapper.toEntity(observacionesDTO)).map(observacionesMapper::toDto);
    }

    @Override
    public Mono<ObservacionesDTO> update(ObservacionesDTO observacionesDTO) {
        log.debug("Request to save Observaciones : {}", observacionesDTO);
        return observacionesRepository.save(observacionesMapper.toEntity(observacionesDTO)).map(observacionesMapper::toDto);
    }

    @Override
    public Mono<ObservacionesDTO> partialUpdate(ObservacionesDTO observacionesDTO) {
        log.debug("Request to partially update Observaciones : {}", observacionesDTO);

        return observacionesRepository
            .findById(observacionesDTO.getId())
            .map(existingObservaciones -> {
                observacionesMapper.partialUpdate(existingObservaciones, observacionesDTO);

                return existingObservaciones;
            })
            .flatMap(observacionesRepository::save)
            .map(observacionesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ObservacionesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Observaciones");
        return observacionesRepository.findAllBy(pageable).map(observacionesMapper::toDto);
    }

    public Flux<ObservacionesDTO> findAllWithEagerRelationships(Pageable pageable) {
        return observacionesRepository.findAllWithEagerRelationships(pageable).map(observacionesMapper::toDto);
    }

    public Mono<Long> countAll() {
        return observacionesRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ObservacionesDTO> findOne(Long id) {
        log.debug("Request to get Observaciones : {}", id);
        return observacionesRepository.findOneWithEagerRelationships(id).map(observacionesMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Observaciones : {}", id);
        return observacionesRepository.deleteById(id);
    }
}
