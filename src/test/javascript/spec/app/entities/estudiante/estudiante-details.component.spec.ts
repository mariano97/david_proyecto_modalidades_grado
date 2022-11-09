/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import EstudianteDetailComponent from '@/entities/estudiante/estudiante-details.vue';
import EstudianteClass from '@/entities/estudiante/estudiante-details.component';
import EstudianteService from '@/entities/estudiante/estudiante.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Estudiante Management Detail Component', () => {
    let wrapper: Wrapper<EstudianteClass>;
    let comp: EstudianteClass;
    let estudianteServiceStub: SinonStubbedInstance<EstudianteService>;

    beforeEach(() => {
      estudianteServiceStub = sinon.createStubInstance<EstudianteService>(EstudianteService);

      wrapper = shallowMount<EstudianteClass>(EstudianteDetailComponent, {
        store,
        localVue,
        router,
        provide: { estudianteService: () => estudianteServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundEstudiante = { id: 123 };
        estudianteServiceStub.find.resolves(foundEstudiante);

        // WHEN
        comp.retrieveEstudiante(123);
        await comp.$nextTick();

        // THEN
        expect(comp.estudiante).toBe(foundEstudiante);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundEstudiante = { id: 123 };
        estudianteServiceStub.find.resolves(foundEstudiante);

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
