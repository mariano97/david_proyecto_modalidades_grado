/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import TablaContenidoDetailComponent from '@/entities/tabla-contenido/tabla-contenido-details.vue';
import TablaContenidoClass from '@/entities/tabla-contenido/tabla-contenido-details.component';
import TablaContenidoService from '@/entities/tabla-contenido/tabla-contenido.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('TablaContenido Management Detail Component', () => {
    let wrapper: Wrapper<TablaContenidoClass>;
    let comp: TablaContenidoClass;
    let tablaContenidoServiceStub: SinonStubbedInstance<TablaContenidoService>;

    beforeEach(() => {
      tablaContenidoServiceStub = sinon.createStubInstance<TablaContenidoService>(TablaContenidoService);

      wrapper = shallowMount<TablaContenidoClass>(TablaContenidoDetailComponent, {
        store,
        localVue,
        router,
        provide: { tablaContenidoService: () => tablaContenidoServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundTablaContenido = { id: 123 };
        tablaContenidoServiceStub.find.resolves(foundTablaContenido);

        // WHEN
        comp.retrieveTablaContenido(123);
        await comp.$nextTick();

        // THEN
        expect(comp.tablaContenido).toBe(foundTablaContenido);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTablaContenido = { id: 123 };
        tablaContenidoServiceStub.find.resolves(foundTablaContenido);

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
