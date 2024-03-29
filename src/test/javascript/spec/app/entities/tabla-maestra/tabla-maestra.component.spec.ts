/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import TablaMaestraComponent from '@/entities/tabla-maestra/tabla-maestra.vue';
import TablaMaestraClass from '@/entities/tabla-maestra/tabla-maestra.component';
import TablaMaestraService from '@/entities/tabla-maestra/tabla-maestra.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(ToastPlugin);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.component('jhi-sort-indicator', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('TablaMaestra Management Component', () => {
    let wrapper: Wrapper<TablaMaestraClass>;
    let comp: TablaMaestraClass;
    let tablaMaestraServiceStub: SinonStubbedInstance<TablaMaestraService>;

    beforeEach(() => {
      tablaMaestraServiceStub = sinon.createStubInstance<TablaMaestraService>(TablaMaestraService);
      tablaMaestraServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<TablaMaestraClass>(TablaMaestraComponent, {
        store,
        localVue,
        stubs: { jhiItemCount: true, bPagination: true, bModal: bModalStub as any },
        provide: {
          tablaMaestraService: () => tablaMaestraServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      tablaMaestraServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllTablaMaestras();
      await comp.$nextTick();

      // THEN
      expect(tablaMaestraServiceStub.retrieve.called).toBeTruthy();
      expect(comp.tablaMaestras[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should load a page', async () => {
      // GIVEN
      tablaMaestraServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();

      // THEN
      expect(tablaMaestraServiceStub.retrieve.called).toBeTruthy();
      expect(comp.tablaMaestras[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should not load a page if the page is the same as the previous page', () => {
      // GIVEN
      tablaMaestraServiceStub.retrieve.reset();
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(tablaMaestraServiceStub.retrieve.called).toBeFalsy();
    });

    it('should re-initialize the page', async () => {
      // GIVEN
      tablaMaestraServiceStub.retrieve.reset();
      tablaMaestraServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();
      comp.clear();
      await comp.$nextTick();

      // THEN
      expect(tablaMaestraServiceStub.retrieve.callCount).toEqual(3);
      expect(comp.page).toEqual(1);
      expect(comp.tablaMaestras[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,asc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // GIVEN
      comp.propOrder = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,asc', 'id']);
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      tablaMaestraServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(tablaMaestraServiceStub.retrieve.callCount).toEqual(1);

      comp.removeTablaMaestra();
      await comp.$nextTick();

      // THEN
      expect(tablaMaestraServiceStub.delete.called).toBeTruthy();
      expect(tablaMaestraServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
