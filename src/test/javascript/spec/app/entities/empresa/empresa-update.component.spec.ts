/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import EmpresaUpdateComponent from '@/entities/empresa/empresa-update.vue';
import EmpresaClass from '@/entities/empresa/empresa-update.component';
import EmpresaService from '@/entities/empresa/empresa.service';

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
  describe('Empresa Management Update Component', () => {
    let wrapper: Wrapper<EmpresaClass>;
    let comp: EmpresaClass;
    let empresaServiceStub: SinonStubbedInstance<EmpresaService>;

    beforeEach(() => {
      empresaServiceStub = sinon.createStubInstance<EmpresaService>(EmpresaService);

      wrapper = shallowMount<EmpresaClass>(EmpresaUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          empresaService: () => empresaServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.empresa = entity;
        empresaServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(empresaServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.empresa = entity;
        empresaServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(empresaServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundEmpresa = { id: 123 };
        empresaServiceStub.find.resolves(foundEmpresa);
        empresaServiceStub.retrieve.resolves([foundEmpresa]);

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
