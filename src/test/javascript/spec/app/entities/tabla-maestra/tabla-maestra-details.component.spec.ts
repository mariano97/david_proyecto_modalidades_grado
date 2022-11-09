/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import TablaMaestraDetailComponent from '@/entities/tabla-maestra/tabla-maestra-details.vue';
import TablaMaestraClass from '@/entities/tabla-maestra/tabla-maestra-details.component';
import TablaMaestraService from '@/entities/tabla-maestra/tabla-maestra.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('TablaMaestra Management Detail Component', () => {
    let wrapper: Wrapper<TablaMaestraClass>;
    let comp: TablaMaestraClass;
    let tablaMaestraServiceStub: SinonStubbedInstance<TablaMaestraService>;

    beforeEach(() => {
      tablaMaestraServiceStub = sinon.createStubInstance<TablaMaestraService>(TablaMaestraService);

      wrapper = shallowMount<TablaMaestraClass>(TablaMaestraDetailComponent, {
        store,
        localVue,
        router,
        provide: { tablaMaestraService: () => tablaMaestraServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundTablaMaestra = { id: 123 };
        tablaMaestraServiceStub.find.resolves(foundTablaMaestra);

        // WHEN
        comp.retrieveTablaMaestra(123);
        await comp.$nextTick();

        // THEN
        expect(comp.tablaMaestra).toBe(foundTablaMaestra);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTablaMaestra = { id: 123 };
        tablaMaestraServiceStub.find.resolves(foundTablaMaestra);

        // WHEN
        comp.beforeRouteEnter({ params: { tablaMaestraId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.tablaMaestra).toBe(foundTablaMaestra);
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
