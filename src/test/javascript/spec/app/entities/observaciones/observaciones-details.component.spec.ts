/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ObservacionesDetailComponent from '@/entities/observaciones/observaciones-details.vue';
import ObservacionesClass from '@/entities/observaciones/observaciones-details.component';
import ObservacionesService from '@/entities/observaciones/observaciones.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Observaciones Management Detail Component', () => {
    let wrapper: Wrapper<ObservacionesClass>;
    let comp: ObservacionesClass;
    let observacionesServiceStub: SinonStubbedInstance<ObservacionesService>;

    beforeEach(() => {
      observacionesServiceStub = sinon.createStubInstance<ObservacionesService>(ObservacionesService);

      wrapper = shallowMount<ObservacionesClass>(ObservacionesDetailComponent, {
        store,
        localVue,
        router,
        provide: { observacionesService: () => observacionesServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundObservaciones = { id: 123 };
        observacionesServiceStub.find.resolves(foundObservaciones);

        // WHEN
        comp.retrieveObservaciones(123);
        await comp.$nextTick();

        // THEN
        expect(comp.observaciones).toBe(foundObservaciones);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundObservaciones = { id: 123 };
        observacionesServiceStub.find.resolves(foundObservaciones);

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
