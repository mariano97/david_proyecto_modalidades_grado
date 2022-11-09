package com.usco.ingenieria.software.pagina.modalidades.grado.service.impl;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Proyecto;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.ProyectoRepository;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.ProyectoService;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.ProyectoDTO;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.mapper.ProyectoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Proyecto}.
 */
@Service
@Transactional
public class ProyectoServiceImpl implements ProyectoService {

    private final Logger log = LoggerFactory.getLogger(ProyectoServiceImpl.class);

    private final ProyectoRepository proyectoRepository;

    private final ProyectoMapper proyectoMapper;

    public ProyectoServiceImpl(ProyectoRepository proyectoRepository, ProyectoMapper proyectoMapper) {
        this.proyectoRepository = proyectoRepository;
        this.proyectoMapper = proyectoMapper;
    }

    @Override
    public Mono<ProyectoDTO> save(ProyectoDTO proyectoDTO) {
        log.debug("Request to save Proyecto : {}", proyectoDTO);
        return proyectoRepository.save(proyectoMapper.toEntity(proyectoDTO)).map(proyectoMapper::toDto);
    }

    @Override
    public Mono<ProyectoDTO> update(ProyectoDTO proyectoDTO) {
        log.debug("Request to save Proyecto : {}", proyectoDTO);
        return proyectoRepository.save(proyectoMapper.toEntity(proyectoDTO)).map(proyectoMapper::toDto);
    }

    @Override
    public Mono<ProyectoDTO> partialUpdate(ProyectoDTO proyectoDTO) {
        log.debug("Request to partially update Proyecto : {}", proyectoDTO);

        return proyectoRepository
            .findById(proyectoDTO.getId())
            .map(existingProyecto -> {
                proyectoMapper.partialUpdate(existingProyecto, proyectoDTO);

                return existingProyecto;
            })
            .flatMap(proyectoRepository::save)
            .map(proyectoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ProyectoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Proyectos");
        return proyectoRepository.findAllBy(pageable).map(proyectoMapper::toDto);
    }

    public Flux<ProyectoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return proyectoRepository.findAllWithEagerRelationships(pageable).map(proyectoMapper::toDto);
    }

    public Mono<Long> countAll() {
        return proyectoRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ProyectoDTO> findOne(Long id) {
        log.debug("Request to get Proyecto : {}", id);
        return proyectoRepository.findOneWithEagerRelationships(id).map(proyectoMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Proyecto : {}", id);
        return proyectoRepository.deleteById(id);
    }
}
