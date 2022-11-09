/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ObservacionesUpdateComponent from '@/entities/observaciones/observaciones-update.vue';
import ObservacionesClass from '@/entities/observaciones/observaciones-update.component';
import ObservacionesService from '@/entities/observaciones/observaciones.service';

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
  describe('Observaciones Management Update Component', () => {
    let wrapper: Wrapper<ObservacionesClass>;
    let comp: ObservacionesClass;
    let observacionesServiceStub: SinonStubbedInstance<ObservacionesService>;

    beforeEach(() => {
      observacionesServiceStub = sinon.createStubInstance<ObservacionesService>(ObservacionesService);

      wrapper = shallowMount<ObservacionesClass>(ObservacionesUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          observacionesService: () => observacionesServiceStub,
          alertService: () => new AlertService(),

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
        comp.observaciones = entity;
        observacionesServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(observacionesServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.observaciones = entity;
        observacionesServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(observacionesServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundObservaciones = { id: 123 };
        observacionesServiceStub.find.resolves(foundObservaciones);
        observacionesServiceStub.retrieve.resolves([foundObservaciones]);

        // WHEN
        comp.beforeRouteEnter({ params: { observacionesId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.observaciones).toBe(foundObservaciones);
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
