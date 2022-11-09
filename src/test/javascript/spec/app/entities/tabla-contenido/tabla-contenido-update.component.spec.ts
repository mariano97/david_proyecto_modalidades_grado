/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import TablaContenidoUpdateComponent from '@/entities/tabla-contenido/tabla-contenido-update.vue';
import TablaContenidoClass from '@/entities/tabla-contenido/tabla-contenido-update.component';
import TablaContenidoService from '@/entities/tabla-contenido/tabla-contenido.service';

import TablaMaestraService from '@/entities/tabla-maestra/tabla-maestra.service';
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
  describe('TablaContenido Management Update Component', () => {
    let wrapper: Wrapper<TablaContenidoClass>;
    let comp: TablaContenidoClass;
    let tablaContenidoServiceStub: SinonStubbedInstance<TablaContenidoService>;

    beforeEach(() => {
      tablaContenidoServiceStub = sinon.createStubInstance<TablaContenidoService>(TablaContenidoService);

      wrapper = shallowMount<TablaContenidoClass>(TablaContenidoUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          tablaContenidoService: () => tablaContenidoServiceStub,
          alertService: () => new AlertService(),

          tablaMaestraService: () =>
            sinon.createStubInstance<TablaMaestraService>(TablaMaestraService, {
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
        comp.tablaContenido = entity;
        tablaContenidoServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(tablaContenidoServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.tablaContenido = entity;
        tablaContenidoServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(tablaContenidoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTablaContenido = { id: 123 };
        tablaContenidoServiceStub.find.resolves(foundTablaContenido);
        tablaContenidoServiceStub.retrieve.resolves([foundTablaContenido]);

        // WHEN
        comp.beforeRouteEnter({ params: { tablaContenidoId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.tablaContenido).toBe(foundTablaContenido);
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
