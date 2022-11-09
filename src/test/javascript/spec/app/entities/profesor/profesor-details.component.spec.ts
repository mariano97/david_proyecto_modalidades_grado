/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ProfesorDetailComponent from '@/entities/profesor/profesor-details.vue';
import ProfesorClass from '@/entities/profesor/profesor-details.component';
import ProfesorService from '@/entities/profesor/profesor.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Profesor Management Detail Component', () => {
    let wrapper: Wrapper<ProfesorClass>;
    let comp: ProfesorClass;
    let profesorServiceStub: SinonStubbedInstance<ProfesorService>;

    beforeEach(() => {
      profesorServiceStub = sinon.createStubInstance<ProfesorService>(ProfesorService);

      wrapper = shallowMount<ProfesorClass>(ProfesorDetailComponent, {
        store,
        localVue,
        router,
        provide: { profesorService: () => profesorServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundProfesor = { id: 123 };
        profesorServiceStub.find.resolves(foundProfesor);

        // WHEN
        comp.retrieveProfesor(123);
        await comp.$nextTick();

        // THEN
        expect(comp.profesor).toBe(foundProfesor);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundProfesor = { id: 123 };
        profesorServiceStub.find.resolves(foundProfesor);

        // WHEN
        comp.beforeRouteEnter({ params: { profesorId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.profesor).toBe(foundProfesor);
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
