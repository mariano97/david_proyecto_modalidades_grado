/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ProyectoDetailComponent from '@/entities/proyecto/proyecto-details.vue';
import ProyectoClass from '@/entities/proyecto/proyecto-details.component';
import ProyectoService from '@/entities/proyecto/proyecto.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Proyecto Management Detail Component', () => {
    let wrapper: Wrapper<ProyectoClass>;
    let comp: ProyectoClass;
    let proyectoServiceStub: SinonStubbedInstance<ProyectoService>;

    beforeEach(() => {
      proyectoServiceStub = sinon.createStubInstance<ProyectoService>(ProyectoService);

      wrapper = shallowMount<ProyectoClass>(ProyectoDetailComponent, {
        store,
        localVue,
        router,
        provide: { proyectoService: () => proyectoServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundProyecto = { id: 123 };
        proyectoServiceStub.find.resolves(foundProyecto);

        // WHEN
        comp.retrieveProyecto(123);
        await comp.$nextTick();

        // THEN
        expect(comp.proyecto).toBe(foundProyecto);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundProyecto = { id: 123 };
        proyectoServiceStub.find.resolves(foundProyecto);

        // WHEN
        comp.beforeRouteEnter({ params: { proyectoId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.proyecto).toBe(foundProyecto);
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
