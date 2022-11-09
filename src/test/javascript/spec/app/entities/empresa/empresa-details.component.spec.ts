/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import EmpresaDetailComponent from '@/entities/empresa/empresa-details.vue';
import EmpresaClass from '@/entities/empresa/empresa-details.component';
import EmpresaService from '@/entities/empresa/empresa.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Empresa Management Detail Component', () => {
    let wrapper: Wrapper<EmpresaClass>;
    let comp: EmpresaClass;
    let empresaServiceStub: SinonStubbedInstance<EmpresaService>;

    beforeEach(() => {
      empresaServiceStub = sinon.createStubInstance<EmpresaService>(EmpresaService);

      wrapper = shallowMount<EmpresaClass>(EmpresaDetailComponent, {
        store,
        localVue,
        router,
        provide: { empresaService: () => empresaServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundEmpresa = { id: 123 };
        empresaServiceStub.find.resolves(foundEmpresa);

        // WHEN
        comp.retrieveEmpresa(123);
        await comp.$nextTick();

        // THEN
        expect(comp.empresa).toBe(foundEmpresa);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundEmpresa = { id: 123 };
        empresaServiceStub.find.resolves(foundEmpresa);

        // WHEN
        comp.beforeRouteEnter({ params: { empresaId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.empresa).toBe(foundEmpresa);
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
