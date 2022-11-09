package com.usco.ingenieria.software.pagina.modalidades.grado.service.impl;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Empresa;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.EmpresaRepository;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.EmpresaService;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.EmpresaDTO;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.mapper.EmpresaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Empresa}.
 */
@Service
@Transactional
public class EmpresaServiceImpl implements EmpresaService {

    private final Logger log = LoggerFactory.getLogger(EmpresaServiceImpl.class);

    private final EmpresaRepository empresaRepository;

    private final EmpresaMapper empresaMapper;

    public EmpresaServiceImpl(EmpresaRepository empresaRepository, EmpresaMapper empresaMapper) {
        this.empresaRepository = empresaRepository;
        this.empresaMapper = empresaMapper;
    }

    @Override
    public Mono<EmpresaDTO> save(EmpresaDTO empresaDTO) {
        log.debug("Request to save Empresa : {}", empresaDTO);
        return empresaRepository.save(empresaMapper.toEntity(empresaDTO)).map(empresaMapper::toDto);
    }

    @Override
    public Mono<EmpresaDTO> update(EmpresaDTO empresaDTO) {
        log.debug("Request to save Empresa : {}", empresaDTO);
        return empresaRepository.save(empresaMapper.toEntity(empresaDTO)).map(empresaMapper::toDto);
    }

    @Override
    public Mono<EmpresaDTO> partialUpdate(EmpresaDTO empresaDTO) {
        log.debug("Request to partially update Empresa : {}", empresaDTO);

        return empresaRepository
            .findById(empresaDTO.getId())
            .map(existingEmpresa -> {
                empresaMapper.partialUpdate(existingEmpresa, empresaDTO);

                return existingEmpresa;
            })
            .flatMap(empresaRepository::save)
            .map(empresaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<EmpresaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Empresas");
        return empresaRepository.findAllBy(pageable).map(empresaMapper::toDto);
    }

    public Mono<Long> countAll() {
        return empresaRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<EmpresaDTO> findOne(Long id) {
        log.debug("Request to get Empresa : {}", id);
        return empresaRepository.findById(id).map(empresaMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Empresa : {}", id);
        return empresaRepository.deleteById(id);
    }
}
