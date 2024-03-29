/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import TablaContenidoComponent from '@/entities/tabla-contenido/tabla-contenido.vue';
import TablaContenidoClass from '@/entities/tabla-contenido/tabla-contenido.component';
import TablaContenidoService from '@/entities/tabla-contenido/tabla-contenido.service';
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
  describe('TablaContenido Management Component', () => {
    let wrapper: Wrapper<TablaContenidoClass>;
    let comp: TablaContenidoClass;
    let tablaContenidoServiceStub: SinonStubbedInstance<TablaContenidoService>;

    beforeEach(() => {
      tablaContenidoServiceStub = sinon.createStubInstance<TablaContenidoService>(TablaContenidoService);
      tablaContenidoServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<TablaContenidoClass>(TablaContenidoComponent, {
        store,
        localVue,
        stubs: { jhiItemCount: true, bPagination: true, bModal: bModalStub as any },
        provide: {
          tablaContenidoService: () => tablaContenidoServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      tablaContenidoServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllTablaContenidos();
      await comp.$nextTick();

      // THEN
      expect(tablaContenidoServiceStub.retrieve.called).toBeTruthy();
      expect(comp.tablaContenidos[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should load a page', async () => {
      // GIVEN
      tablaContenidoServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();

      // THEN
      expect(tablaContenidoServiceStub.retrieve.called).toBeTruthy();
      expect(comp.tablaContenidos[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should not load a page if the page is the same as the previous page', () => {
      // GIVEN
      tablaContenidoServiceStub.retrieve.reset();
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(tablaContenidoServiceStub.retrieve.called).toBeFalsy();
    });

    it('should re-initialize the page', async () => {
      // GIVEN
      tablaContenidoServiceStub.retrieve.reset();
      tablaContenidoServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();
      comp.clear();
      await comp.$nextTick();

      // THEN
      expect(tablaContenidoServiceStub.retrieve.callCount).toEqual(3);
      expect(comp.page).toEqual(1);
      expect(comp.tablaContenidos[0]).toEqual(expect.objectContaining({ id: 123 }));
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
      tablaContenidoServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(tablaContenidoServiceStub.retrieve.callCount).toEqual(1);

      comp.removeTablaContenido();
      await comp.$nextTick();

      // THEN
      expect(tablaContenidoServiceStub.delete.called).toBeTruthy();
      expect(tablaContenidoServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
