/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import EstudianteUpdateComponent from '@/entities/estudiante/estudiante-update.vue';
import EstudianteClass from '@/entities/estudiante/estudiante-update.component';
import EstudianteService from '@/entities/estudiante/estudiante.service';

import TablaContenidoService from '@/entities/tabla-contenido/tabla-contenido.service';

import ProyectoService from '@/entities/proyecto/proyecto.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.use(ToastPlugin);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('Estudiante Management Update Component', () => {
    let wrapper: Wrapper<EstudianteClass>;
    let comp: EstudianteClass;
    let estudianteServiceStub: SinonStubbedInstance<EstudianteService>;

    beforeEach(() => {
      estudianteServiceStub = sinon.createStubInstance<EstudianteService>(EstudianteService);

      wrapper = shallowMount<EstudianteClass>(EstudianteUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          estudianteService: () => estudianteServiceStub,
          alertService: () => new AlertService(),

          tablaContenidoService: () =>
            sinon.createStubInstance<TablaContenidoService>(TablaContenidoService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          proyectoService: () =>
            sinon.createStubInstance<ProyectoService>(ProyectoService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.estudiante = entity;
        estudianteServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(estudianteServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.estudiante = entity;
        estudianteServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(estudianteServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundEstudiante = { id: 123 };
        estudianteServiceStub.find.resolves(foundEstudiante);
        estudianteServiceStub.retrieve.resolves([foundEstudiante]);

        // WHEN
        comp.beforeRouteEnter({ params: { estudianteId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.estudiante).toBe(foundEstudiante);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
