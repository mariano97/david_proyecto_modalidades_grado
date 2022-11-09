import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

// prettier-ignore
const TablaMaestra = () => import('@/entities/tabla-maestra/tabla-maestra.vue');
// prettier-ignore
const TablaMaestraUpdate = () => import('@/entities/tabla-maestra/tabla-maestra-update.vue');
// prettier-ignore
const TablaMaestraDetails = () => import('@/entities/tabla-maestra/tabla-maestra-details.vue');
// prettier-ignore
const TablaContenido = () => import('@/entities/tabla-contenido/tabla-contenido.vue');
// prettier-ignore
const TablaContenidoUpdate = () => import('@/entities/tabla-contenido/tabla-contenido-update.vue');
// prettier-ignore
const TablaContenidoDetails = () => import('@/entities/tabla-contenido/tabla-contenido-details.vue');
// prettier-ignore
const Proyecto = () => import('@/entities/proyecto/proyecto.vue');
// prettier-ignore
const ProyectoUpdate = () => import('@/entities/proyecto/proyecto-update.vue');
// prettier-ignore
const ProyectoDetails = () => import('@/entities/proyecto/proyecto-details.vue');
// prettier-ignore
const Empresa = () => import('@/entities/empresa/empresa.vue');
// prettier-ignore
const EmpresaUpdate = () => import('@/entities/empresa/empresa-update.vue');
// prettier-ignore
const EmpresaDetails = () => import('@/entities/empresa/empresa-details.vue');
// prettier-ignore
const Arl = () => import('@/entities/arl/arl.vue');
// prettier-ignore
const ArlUpdate = () => import('@/entities/arl/arl-update.vue');
// prettier-ignore
const ArlDetails = () => import('@/entities/arl/arl-details.vue');
// prettier-ignore
const Observaciones = () => import('@/entities/observaciones/observaciones.vue');
// prettier-ignore
const ObservacionesUpdate = () => import('@/entities/observaciones/observaciones-update.vue');
// prettier-ignore
const ObservacionesDetails = () => import('@/entities/observaciones/observaciones-details.vue');
// prettier-ignore
const Estudiante = () => import('@/entities/estudiante/estudiante.vue');
// prettier-ignore
const EstudianteUpdate = () => import('@/entities/estudiante/estudiante-update.vue');
// prettier-ignore
const EstudianteDetails = () => import('@/entities/estudiante/estudiante-details.vue');
// prettier-ignore
const Profesor = () => import('@/entities/profesor/profesor.vue');
// prettier-ignore
const ProfesorUpdate = () => import('@/entities/profesor/profesor-update.vue');
// prettier-ignore
const ProfesorDetails = () => import('@/entities/profesor/profesor-details.vue');
// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'tabla-maestra',
      name: 'TablaMaestra',
      component: TablaMaestra,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tabla-maestra/new',
      name: 'TablaMaestraCreate',
      component: TablaMaestraUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tabla-maestra/:tablaMaestraId/edit',
      name: 'TablaMaestraEdit',
      component: TablaMaestraUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tabla-maestra/:tablaMaestraId/view',
      name: 'TablaMaestraView',
      component: TablaMaestraDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tabla-contenido',
      name: 'TablaContenido',
      component: TablaContenido,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tabla-contenido/new',
      name: 'TablaContenidoCreate',
      component: TablaContenidoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tabla-contenido/:tablaContenidoId/edit',
      name: 'TablaContenidoEdit',
      component: TablaContenidoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tabla-contenido/:tablaContenidoId/view',
      name: 'TablaContenidoView',
      component: TablaContenidoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'proyecto',
      name: 'Proyecto',
      component: Proyecto,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'proyecto/new',
      name: 'ProyectoCreate',
      component: ProyectoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'proyecto/:proyectoId/edit',
      name: 'ProyectoEdit',
      component: ProyectoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'proyecto/:proyectoId/view',
      name: 'ProyectoView',
      component: ProyectoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'empresa',
      name: 'Empresa',
      component: Empresa,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'empresa/new',
      name: 'EmpresaCreate',
      component: EmpresaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'empresa/:empresaId/edit',
      name: 'EmpresaEdit',
      component: EmpresaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'empresa/:empresaId/view',
      name: 'EmpresaView',
      component: EmpresaDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'arl',
      name: 'Arl',
      component: Arl,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'arl/new',
      name: 'ArlCreate',
      component: ArlUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'arl/:arlId/edit',
      name: 'ArlEdit',
      component: ArlUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'arl/:arlId/view',
      name: 'ArlView',
      component: ArlDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'observaciones',
      name: 'Observaciones',
      component: Observaciones,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'observaciones/new',
      name: 'ObservacionesCreate',
      component: ObservacionesUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'observaciones/:observacionesId/edit',
      name: 'ObservacionesEdit',
      component: ObservacionesUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'observaciones/:observacionesId/view',
      name: 'ObservacionesView',
      component: ObservacionesDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'estudiante',
      name: 'Estudiante',
      component: Estudiante,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'estudiante/new',
      name: 'EstudianteCreate',
      component: EstudianteUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'estudiante/:estudianteId/edit',
      name: 'EstudianteEdit',
      component: EstudianteUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'estudiante/:estudianteId/view',
      name: 'EstudianteView',
      component: EstudianteDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'profesor',
      name: 'Profesor',
      component: Profesor,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'profesor/new',
      name: 'ProfesorCreate',
      component: ProfesorUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'profesor/:profesorId/edit',
      name: 'ProfesorEdit',
      component: ProfesorUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'profesor/:profesorId/view',
      name: 'ProfesorView',
      component: ProfesorDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
