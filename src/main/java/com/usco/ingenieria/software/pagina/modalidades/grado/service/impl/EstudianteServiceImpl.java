package com.usco.ingenieria.software.pagina.modalidades.grado.service.impl;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Estudiante;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.EstudianteRepository;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.EstudianteService;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.EstudianteDTO;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.mapper.EstudianteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Estudiante}.
 */
@Service
@Transactional
public class EstudianteServiceImpl implements EstudianteService {

    private final Logger log = LoggerFactory.getLogger(EstudianteServiceImpl.class);

    private final EstudianteRepository estudianteRepository;

    private final EstudianteMapper estudianteMapper;

    public EstudianteServiceImpl(EstudianteRepository estudianteRepository, EstudianteMapper estudianteMapper) {
        this.estudianteRepository = estudianteRepository;
        this.estudianteMapper = estudianteMapper;
    }

    @Override
    public Mono<EstudianteDTO> save(EstudianteDTO estudianteDTO) {
        log.debug("Request to save Estudiante : {}", estudianteDTO);
        return estudianteRepository.save(estudianteMapper.toEntity(estudianteDTO)).map(estudianteMapper::toDto);
    }

    @Override
    public Mono<EstudianteDTO> update(EstudianteDTO estudianteDTO) {
        log.debug("Request to save Estudiante : {}", estudianteDTO);
        return estudianteRepository.save(estudianteMapper.toEntity(estudianteDTO)).map(estudianteMapper::toDto);
    }

    @Override
    public Mono<EstudianteDTO> partialUpdate(EstudianteDTO estudianteDTO) {
        log.debug("Request to partially update Estudiante : {}", estudianteDTO);

        return estudianteRepository
            .findById(estudianteDTO.getId())
            .map(existingEstudiante -> {
                estudianteMapper.partialUpdate(existingEstudiante, estudianteDTO);

                return existingEstudiante;
            })
            .flatMap(estudianteRepository::save)
            .map(estudianteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<EstudianteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Estudiantes");
        return estudianteRepository.findAllBy(pageable).map(estudianteMapper::toDto);
    }

    public Flux<EstudianteDTO> findAllWithEagerRelationships(Pageable pageable) {
        return estudianteRepository.findAllWithEagerRelationships(pageable).map(estudianteMapper::toDto);
    }

    public Mono<Long> countAll() {
        return estudianteRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<EstudianteDTO> findOne(Long id) {
        log.debug("Request to get Estudiante : {}", id);
        return estudianteRepository.findOneWithEagerRelationships(id).map(estudianteMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Estudiante : {}", id);
        return estudianteRepository.deleteById(id);
    }
}
