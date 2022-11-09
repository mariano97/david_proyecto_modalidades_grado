package com.usco.ingenieria.software.pagina.modalidades.grado.service.impl;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Arl;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.ArlRepository;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.ArlService;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.ArlDTO;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.mapper.ArlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Arl}.
 */
@Service
@Transactional
public class ArlServiceImpl implements ArlService {

    private final Logger log = LoggerFactory.getLogger(ArlServiceImpl.class);

    private final ArlRepository arlRepository;

    private final ArlMapper arlMapper;

    public ArlServiceImpl(ArlRepository arlRepository, ArlMapper arlMapper) {
        this.arlRepository = arlRepository;
        this.arlMapper = arlMapper;
    }

    @Override
    public Mono<ArlDTO> save(ArlDTO arlDTO) {
        log.debug("Request to save Arl : {}", arlDTO);
        return arlRepository.save(arlMapper.toEntity(arlDTO)).map(arlMapper::toDto);
    }

    @Override
    public Mono<ArlDTO> update(ArlDTO arlDTO) {
        log.debug("Request to save Arl : {}", arlDTO);
        return arlRepository.save(arlMapper.toEntity(arlDTO)).map(arlMapper::toDto);
    }

    @Override
    public Mono<ArlDTO> partialUpdate(ArlDTO arlDTO) {
        log.debug("Request to partially update Arl : {}", arlDTO);

        return arlRepository
            .findById(arlDTO.getId())
            .map(existingArl -> {
                arlMapper.partialUpdate(existingArl, arlDTO);

                return existingArl;
            })
            .flatMap(arlRepository::save)
            .map(arlMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ArlDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Arls");
        return arlRepository.findAllBy(pageable).map(arlMapper::toDto);
    }

    public Mono<Long> countAll() {
        return arlRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ArlDTO> findOne(Long id) {
        log.debug("Request to get Arl : {}", id);
        return arlRepository.findById(id).map(arlMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Arl : {}", id);
        return arlRepository.deleteById(id);
    }
}
