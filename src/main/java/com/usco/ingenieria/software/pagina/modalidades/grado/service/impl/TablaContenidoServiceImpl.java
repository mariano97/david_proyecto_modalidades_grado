package com.usco.ingenieria.software.pagina.modalidades.grado.service.impl;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.TablaContenido;
import com.usco.ingenieria.software.pagina.modalidades.grado.repository.TablaContenidoRepository;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.TablaContenidoService;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.TablaContenidoDTO;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.mapper.TablaContenidoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link TablaContenido}.
 */
@Service
@Transactional
public class TablaContenidoServiceImpl implements TablaContenidoService {

    private final Logger log = LoggerFactory.getLogger(TablaContenidoServiceImpl.class);

    private final TablaContenidoRepository tablaContenidoRepository;

    private final TablaContenidoMapper tablaContenidoMapper;

    public TablaContenidoServiceImpl(TablaContenidoRepository tablaContenidoRepository, TablaContenidoMapper tablaContenidoMapper) {
        this.tablaContenidoRepository = tablaContenidoRepository;
        this.tablaContenidoMapper = tablaContenidoMapper;
    }

    @Override
    public Mono<TablaContenidoDTO> save(TablaContenidoDTO tablaContenidoDTO) {
        log.debug("Request to save TablaContenido : {}", tablaContenidoDTO);
        return tablaContenidoRepository.save(tablaContenidoMapper.toEntity(tablaContenidoDTO)).map(tablaContenidoMapper::toDto);
    }

    @Override
    public Mono<TablaContenidoDTO> update(TablaContenidoDTO tablaContenidoDTO) {
        log.debug("Request to save TablaContenido : {}", tablaContenidoDTO);
        return tablaContenidoRepository.save(tablaContenidoMapper.toEntity(tablaContenidoDTO)).map(tablaContenidoMapper::toDto);
    }

    @Override
    public Mono<TablaContenidoDTO> partialUpdate(TablaContenidoDTO tablaContenidoDTO) {
        log.debug("Request to partially update TablaContenido : {}", tablaContenidoDTO);

        return tablaContenidoRepository
            .findById(tablaContenidoDTO.getId())
            .map(existingTablaContenido -> {
                tablaContenidoMapper.partialUpdate(existingTablaContenido, tablaContenidoDTO);

                return existingTablaContenido;
            })
            .flatMap(tablaContenidoRepository::save)
            .map(tablaContenidoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<TablaContenidoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TablaContenidos");
        return tablaContenidoRepository.findAllBy(pageable).map(tablaContenidoMapper::toDto);
    }

    public Flux<TablaContenidoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return tablaContenidoRepository.findAllWithEagerRelationships(pageable).map(tablaContenidoMapper::toDto);
    }

    public Mono<Long> countAll() {
        return tablaContenidoRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<TablaContenidoDTO> findOne(Long id) {
        log.debug("Request to get TablaContenido : {}", id);
        return tablaContenidoRepository.findOneWithEagerRelationships(id).map(tablaContenidoMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete TablaContenido : {}", id);
        return tablaContenidoRepository.deleteById(id);
    }
}
