package com.usco.ingenieria.software.pagina.modalidades.grado.service.impl;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Profesor;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.ProfesorRepository;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.ProfesorService;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.ProfesorDTO;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.mapper.ProfesorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Profesor}.
 */
@Service
@Transactional
public class ProfesorServiceImpl implements ProfesorService {

    private final Logger log = LoggerFactory.getLogger(ProfesorServiceImpl.class);

    private final ProfesorRepository profesorRepository;

    private final ProfesorMapper profesorMapper;

    public ProfesorServiceImpl(ProfesorRepository profesorRepository, ProfesorMapper profesorMapper) {
        this.profesorRepository = profesorRepository;
        this.profesorMapper = profesorMapper;
    }

    @Override
    public Mono<ProfesorDTO> save(ProfesorDTO profesorDTO) {
        log.debug("Request to save Profesor : {}", profesorDTO);
        return profesorRepository.save(profesorMapper.toEntity(profesorDTO)).map(profesorMapper::toDto);
    }

    @Override
    public Mono<ProfesorDTO> update(ProfesorDTO profesorDTO) {
        log.debug("Request to save Profesor : {}", profesorDTO);
        return profesorRepository.save(profesorMapper.toEntity(profesorDTO)).map(profesorMapper::toDto);
    }

    @Override
    public Mono<ProfesorDTO> partialUpdate(ProfesorDTO profesorDTO) {
        log.debug("Request to partially update Profesor : {}", profesorDTO);

        return profesorRepository
            .findById(profesorDTO.getId())
            .map(existingProfesor -> {
                profesorMapper.partialUpdate(existingProfesor, profesorDTO);

                return existingProfesor;
            })
            .flatMap(profesorRepository::save)
            .map(profesorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ProfesorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Profesors");
        return profesorRepository.findAllBy(pageable).map(profesorMapper::toDto);
    }

    public Mono<Long> countAll() {
        return profesorRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ProfesorDTO> findOne(Long id) {
        log.debug("Request to get Profesor : {}", id);
        return profesorRepository.findById(id).map(profesorMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Profesor : {}", id);
        return profesorRepository.deleteById(id);
    }
}
