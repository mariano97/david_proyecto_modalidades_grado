import { Component, Provide, Vue } from 'vue-property-decorator';

import UserService from '@/entities/user/user.service';
import TablaMaestraService from './tabla-maestra/tabla-maestra.service';
import TablaContenidoService from './tabla-contenido/tabla-contenido.service';
import ProyectoService from './proyecto/proyecto.service';
import EmpresaService from './empresa/empresa.service';
import ArlService from './arl/arl.service';
import ObservacionesService from './observaciones/observaciones.service';
import EstudianteService from './estudiante/estudiante.service';
import ProfesorService from './profesor/profesor.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

@Component
export default class Entities extends Vue {
  @Provide('userService') private userService = () => new UserService();
  @Provide('tablaMaestraService') private tablaMaestraService = () => new TablaMaestraService();
  @Provide('tablaContenidoService') private tablaContenidoService = () => new TablaContenidoService();
  @Provide('proyectoService') private proyectoService = () => new ProyectoService();
  @Provide('empresaService') private empresaService = () => new EmpresaService();
  @Provide('arlService') private arlService = () => new ArlService();
  @Provide('observacionesService') private observacionesService = () => new ObservacionesService();
  @Provide('estudianteService') private estudianteService = () => new EstudianteService();
  @Provide('profesorService') private profesorService = () => new ProfesorService();
  // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
}
